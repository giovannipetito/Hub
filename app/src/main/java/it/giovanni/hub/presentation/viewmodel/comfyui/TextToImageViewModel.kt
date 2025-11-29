package it.giovanni.hub.presentation.viewmodel.comfyui

import android.Manifest
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.App
import it.giovanni.hub.R
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.buildTextToImageRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.io.OutputStream
import java.net.URLEncoder
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class TextToImageViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: ComfyRepository
) : ViewModel() {

    var imageUrl by mutableStateOf<String?>(null)
        private set

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    private val notificationId: AtomicInteger = AtomicInteger(0)

    fun generateImage(comfyUrl: String, prompt: String, onResult: (Result<Unit>) -> Unit) = viewModelScope.launch {
        try {
            val body = buildTextToImageRequestBody(context, prompt)

            val startResponse: JsonObject = repository.startRun(body)
            val promptId = startResponse["prompt_id"].asString
            Log.d("ComfyUI", "Queued promptId=$promptId")

            withTimeoutOrNull(120_000) { // 2 minutes
                while (isActive) {
                    val historyRoot: JsonObject = repository.getRun(promptId = promptId)
                    Log.d("ComfyUI", "history($promptId) = $historyRoot")

                    // Pick the entry for this promptId
                    val entry = historyRoot.getAsJsonObject(promptId)
                    if (entry == null) {
                        // history not ready yet
                        delay(1_000)
                        continue
                    }

                    // Read status
                    val statusObj = entry.getAsJsonObject("status")
                    val completedFlag = statusObj?.get("completed")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: false

                    // Read outputs -> node "10" (SaveImage)
                    val outputs = entry.getAsJsonObject("outputs")
                    val saveNode = outputs?.getAsJsonObject("10") // SaveImage node id
                    val imagesArray = saveNode?.getAsJsonArray("images")
                    val firstImage = imagesArray?.firstOrNull()?.asJsonObject

                    if (firstImage != null) {
                        val filename = firstImage["filename"].asString
                        val subfolder = firstImage["subfolder"].asString
                        val type = firstImage["type"].asString  // "output"

                        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                        val encodedSubfolder = URLEncoder.encode(subfolder, StandardCharsets.UTF_8.toString())

                        val baseUrl = comfyUrl.let {
                            if (it.endsWith("/")) it else "$it/"
                        }

                        imageUrl = "${baseUrl}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        // Use this if the URL is fixed and you don't need to read it from DataStoreRepository.
                        // imageUrl = "${Config.COMFY_BASE_URL}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        Log.d("ComfyUI", "Image URL = $imageUrl")

                        onResult(Result.success(Unit))
                        notifyImageReady(imageUrl)

                        // Exit the timeout block cleanly
                        return@withTimeoutOrNull
                    }

                    // Run is marked completed but no images were produced:
                    if (completedFlag) {
                        Log.w("ComfyUI", "Prompt $promptId completed but produced no images")
                        return@withTimeoutOrNull
                    }

                    // If no image yet, just keep polling (even if completedFlag is true, in practice
                    // you'll have outputs together with completed=true)
                    delay(1_000)
                }
            } ?: Log.w("ComfyUI", "Timed-out waiting for prompt $promptId")
        } catch (e: UnknownHostException) {
            Log.e("ComfyUI", "Cannot resolve host", e)
            onResult(Result.failure(Exception("Unable to reach ComfyUI: " + e.message)))
        } catch (e: IOException) {
            Log.e("ComfyUI", "Network error", e)
            onResult(Result.failure(Exception("Network error while calling ComfyUI: " + e.message)))
        } catch (e: Exception) {
            Log.e("ComfyUI", "Unexpected error in generateImage", e)
            onResult(Result.failure(Exception("Unexpected error while generating image: " + e.message)))
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