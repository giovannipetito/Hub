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
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.App
import it.giovanni.hub.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseViewModel(
    @ApplicationContext protected val context: Context
) : ViewModel() {

    var imageUrl by mutableStateOf<String?>(null)
        protected set

    private val notificationId: AtomicInteger = AtomicInteger(0)

    fun saveImageToGallery() {
        val url = imageUrl ?: return
        viewModelScope.launch {
            saveViaMediaStore(context, url)
        }
    }

    private suspend fun saveViaMediaStore(context: Context, url: String) = withContext(Dispatchers.IO) {
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

    fun notifyImageReady(url: String?) = viewModelScope.launch(Dispatchers.IO) {
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