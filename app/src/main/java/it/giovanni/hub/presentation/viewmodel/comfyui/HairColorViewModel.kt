package it.giovanni.hub.presentation.viewmodel.comfyui

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.App
import it.giovanni.hub.R
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject


@HiltViewModel
class HairColorViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: ComfyRepository,
    private val dataStore: DataStoreRepository
) : ViewModel() {

    private val _comfyUrl = MutableStateFlow("")
    val comfyUrl: StateFlow<String> = _comfyUrl

    var imageUrl by mutableStateOf<String?>(null)
        private set

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    private val notificationId: AtomicInteger = AtomicInteger(0)

    init {
        viewModelScope.launch {
            dataStore.getComfyUrl().collect { savedUrl ->
                if (savedUrl != null) {
                    _comfyUrl.value = savedUrl
                }
            }
        }
    }

    fun setBaseUrl(baseUrl: String) = viewModelScope.launch {
        dataStore.saveComfyUrl(baseUrl)
        _comfyUrl.value = baseUrl
    }

    /**
     * Main entry point: given a hair color name (e.g. "Red")
     * and the local Uri of the picked/taken photo, it:
     * 1. uploads the image to ComfyUI (/upload/image)
     * 2. builds the img2img workflow from img2img_hair_color_api.json
     * 3. overrides:
     *      - node "30".inputs.text = "${hairColor} hair"
     *      - node "29".inputs.image = uploadedImageName
     * 4. starts the run and polls history until the output image is ready
     * 5. exposes imageUrl and sends a notification
     */
    fun generateImage(
        hairColor: String,
        sourceImageUri: Uri
    ) = viewModelScope.launch {
        try {
            val baseUrl = comfyUrl.value.trim()
            require(baseUrl.isNotBlank()) { "Comfy baseUrl is empty" }

            // 1) Upload image to ComfyUI
            val uploadedName = uploadImageToComfy(baseUrl, sourceImageUri)
                ?: throw IllegalStateException("Image upload failed")

            // 2) Build workflow body for img2img hair color
            val hairPrompt = "${hairColor} hair"
            val body = buildHairColorRequestBody(
                hairPrompt = hairPrompt,
                uploadedImageName = uploadedName
            )

            // 3) Start run
            val startResponse: JsonObject = repository.startRun(body)
            val promptId = startResponse["prompt_id"]?.asString
                ?: startResponse.getAsJsonObject("prompt")?.get("id")?.asString
                ?: throw IllegalStateException("promptId not found in startRun response")

            // 4) Poll history until we get an image
            withTimeoutOrNull(120_000L) { // 2 minutes
                while (isActive) {
                    val historyRoot: JsonObject = repository.getRun(promptId = promptId)

                    // entry for this promptId
                    val entry = historyRoot.getAsJsonObject(promptId)
                    if (entry == null) {
                        // history not ready yet
                        delay(1_000)
                        continue
                    }

                    // status
                    val statusObj = entry.getAsJsonObject("status")
                    val statusStr = statusObj?.get("status")?.asString ?: ""
                    val completedFlag =
                        statusObj?.get("completed")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: false

                    // outputs: look for the first node that has "images"
                    val outputs = entry.getAsJsonObject("outputs")
                    var foundUrl: String? = null

                    outputs?.entrySet()?.forEach { (_, value) ->
                        val nodeObj = value.asJsonObject
                        val imagesArray = nodeObj.getAsJsonArray("images") ?: return@forEach
                        if (imagesArray.size() == 0) return@forEach

                        val firstImage = imagesArray[0].asJsonObject
                        val filename = firstImage["filename"].asString
                        val subfolder = firstImage["subfolder"].asString
                        val type = firstImage["type"].asString

                        val base = baseUrl.trimEnd('/')
                        foundUrl = "$base/view?filename=$filename&subfolder=$subfolder&type=$type"
                        return@forEach
                    }

                    if (foundUrl != null) {
                        // SUCCESS: update state and notify, then exit timeout block
                        imageUrl = foundUrl
                        notifyImageReady(foundUrl)
                        return@withTimeoutOrNull
                    }

                    // If run is completed but no images were produced, stop polling
                    if (completedFlag || statusStr == "error") {
                        // optionally log something here
                        return@withTimeoutOrNull
                    }

                    // keep polling
                    delay(1_000)
                }
            } ?: run {
                // timed out
                // optionally log a warning here
            }

        } catch (e: Exception) {
            // log or handle as you prefer (Toast, AlertBar, etc.)
            e.printStackTrace()
        }
    }

    /**
     * Uploads the selected image (gallery/camera Uri) to ComfyUI /upload/image
     * and returns the resulting filename that will be used in the LoadImage node.
     */
    private suspend fun uploadImageToComfy(
        baseUrl: String,
        sourceImageUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        runCatching {
            val resolver = context.contentResolver

            val mimeType = resolver.getType(sourceImageUri) ?: "image/jpeg"
            val bytes = resolver.openInputStream(sourceImageUri)?.use { it.readBytes() }
                ?: return@runCatching null

            val fileName = "hair_${System.currentTimeMillis()}.jpg"

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", fileName, requestBody)
                .build()

            val client = OkHttpClient()
            val url = baseUrl.trimEnd('/') + "/upload/image"

            val request = Request.Builder()
                .url(url)
                .post(multipartBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("ComfyUI", "uploadImageToComfy failed: HTTP ${response.code}")
                    return@runCatching null
                }

                val bodyString = response.body?.string() ?: return@runCatching null
                Log.d("ComfyUI", "uploadImageToComfy response: $bodyString")

                val json = JSONObject(bodyString)

                // ComfyUI can return either:
                // { "name": "...", "subfolder": "", "type": "input" }
                // or { "images": [ { "name": "...", ... } ] }
                val imageObj = if (json.has("images")) {
                    json.getJSONArray("images").getJSONObject(0)
                } else {
                    json
                }

                imageObj.getString("name")
            }
        }.getOrNull()
    }

    /**
     * Builds the request body for the img2img hair color workflow.
     * - Loads img2img_hair_color_api.json from assets
     * - Overrides:
     *     node "30".inputs.text = hairPrompt
     *     node "29".inputs.image = uploadedImageName
     */
    private fun buildHairColorRequestBody(
        hairPrompt: String,
        uploadedImageName: String
    ): JsonObject {
        val jsonStr = context.assets.open("img2img_hair_color_api.json")
            .bufferedReader()
            .use { it.readText() }

        val workflowJson = JsonParser.parseString(jsonStr).asJsonObject

        // Node 30: CLIPTextEncode positive prompt ("red hair" -> "${selected.name} hair")
        workflowJson.getAsJsonObject("30")
            ?.getAsJsonObject("inputs")
            ?.addProperty("text", hairPrompt)

        // Node 29: LoadImage -> use uploaded image filename
        workflowJson.getAsJsonObject("29")
            ?.getAsJsonObject("inputs")
            ?.addProperty("image", uploadedImageName)

        return JsonObject().apply {
            add("prompt", workflowJson)
        }
    }

    fun saveImageToGallery() {
        val url = imageUrl ?: return
        viewModelScope.launch {
            _saveResult.emit(saveViaMediaStore(context, url))
        }
    }

    private suspend fun saveViaMediaStore(context: Context, url: String): Boolean =
        withContext(Dispatchers.IO) {
            runCatching {
                val filename = "comfy_${System.currentTimeMillis()}.jpg"

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/ComfyUI")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                ) ?: return@runCatching false

                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    val drawable = ImageLoader(context).execute(
                        ImageRequest.Builder(context)
                            .data(url)
                            .allowHardware(false)
                            .build()
                    ).drawable ?: return@runCatching false

                    if (outputStream != null)
                        (drawable as BitmapDrawable).bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            92,
                            outputStream
                        )
                }
                true
            }.getOrDefault(false)
        }

    private fun notifyImageReady(url: String?) = viewModelScope.launch(Dispatchers.IO) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        val bitmap = runCatching {
            val drawable = ImageLoader(context).execute(
                ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
            ).drawable as BitmapDrawable
            drawable.bitmap
        }.getOrNull()

        val tapIntent = Intent(Intent.ACTION_VIEW, url?.toUri())
        val contentIntent = PendingIntent.getActivity(
            context, 0, tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_audioslave)
            .setContentTitle("Comfy UI")
            .setContentText("Your image is ready – tap to view")
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .apply {
                bitmap?.let {
                    setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
                }
            }
            .build()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(notificationId.incrementAndGet(), notification)
        }
    }
}