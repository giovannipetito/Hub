package it.giovanni.hub.presentation.viewmodel

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
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.App
import it.giovanni.hub.R
import it.giovanni.hub.data.datasource.remote.ComfyDataSource
import it.giovanni.hub.data.model.comfyui.HistoryItem
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.buildTextToImageRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class ComfyUIViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val dataSource: ComfyDataSource
) : ViewModel() {

    companion object {
        const val TXT2IMG_WORKFLOW_ID = "QfPQGkm_3sklkqeotTb_L"
        const val STATUS_COMPLETED = "COMPLETED"
    }

    var imageUrl by mutableStateOf<String?>(null)
        private set

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val notificationId: AtomicInteger = AtomicInteger(0)

    fun generateImage(promptText: String) = viewModelScope.launch {

        val body = buildTextToImageRequestBody(context, promptText)
        val run: JsonObject = dataSource.startRun(TXT2IMG_WORKFLOW_ID, body)
        val runId = run["id"].asString

        // La GET viene ripetuta finché lo stato è COMPLETED
        withTimeoutOrNull(120_000) { // 2 min budget
            while (isActive) {
                val response: JsonObject = dataSource.getRun(TXT2IMG_WORKFLOW_ID, runId)
                if (response["status"].asString == STATUS_COMPLETED) {
                    val outputs = response["output"].asJsonArray
                    outputs.firstOrNull()?.let { json: JsonElement ->
                        imageUrl = json.asJsonObject["url"].asString
                        notifyImageReady(imageUrl)
                    }
                    cancel() // leave the loop
                }
                delay(1_000)
            }
        } ?: Log.w("ComfyUI", "Timed-out waiting for workflow $runId")
    }

    fun getHistory(limit: Int = 50) = viewModelScope.launch {
        val jsonArray: JsonArray = dataSource.fetchRuns(TXT2IMG_WORKFLOW_ID, limit)

        val completedRuns: List<HistoryItem> = jsonArray
            .filter { it.asJsonObject["status"].asString == STATUS_COMPLETED }
            .map { element -> Gson().fromJson(element, HistoryItem::class.java) }

        _history.value = completedRuns
    }

    fun saveImageToGallery() {
        val url = imageUrl ?: return
        viewModelScope.launch {
            _saveResult.emit( saveViaMediaStore(context, url) )
        }
    }

    private suspend fun saveViaMediaStore(context: Context, url: String): Boolean = withContext(Dispatchers.IO) {
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
                // download & decode with Coil (already on class-path)
                val drawable = ImageLoader(context).execute(
                    ImageRequest.Builder(context)
                        .data(url)
                        .allowHardware(false)
                        .build()
                ).drawable ?: return@runCatching false

                if (outputStream != null)
                    (drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
            }
            true
        }.getOrDefault(false)
    }

    private fun notifyImageReady(url: String?) = viewModelScope.launch(Dispatchers.IO) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        // Download a bitmap for a BigPictureStyle (optional)
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