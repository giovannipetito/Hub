package it.giovanni.hub.ui.widgets

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

class ImageWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ImageWidget()
}

class ImageWidget : GlanceAppWidget() {

    companion object {
        val sourceKey = stringPreferencesKey("image_source")
        val sourceUrlKey = stringPreferencesKey("image_source_url")

        fun getImageKey(size: DpSize) = getImageKey(size.width.value.toPx, size.height.value.toPx)

        fun getImageKey(width: Float, height: Float) = stringPreferencesKey("uri-$width-$height")

        val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
    }

    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ImageWidgetContent()
        }
    }

    @Composable
    fun ImageWidgetContent() {
        val context = LocalContext.current
        val size = LocalSize.current
        val imagePath = "https://picsum.photos/400/400" // currentState(getImageKey(size))
        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background),
                    // .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = if (imagePath == null) {
                    Alignment.Center
                } else {
                    Alignment.BottomEnd
                }
            ) {
                if (imagePath != null) {
                    Image(
                        provider = getImageProvider(imagePath),
                        contentDescription = "Image Path",
                        contentScale = ContentScale.FillBounds,
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .clickable(actionRunCallback<RefreshAction>())
                    )
                    Text(
                        text = "Source: ${currentState(sourceKey)}",
                        style = TextStyle(
                            color = GlanceTheme.colors.onPrimary,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.End,
                            textDecoration = TextDecoration.Underline
                        )/*,
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(GlanceTheme.colors.primary)
                            .clickable(
                                actionStartActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        currentState(sourceUrlKey)?.toUri()
                                    )
                                )
                            )
                        */
                    )
                } else {
                    CircularProgressIndicator()

                    // Enqueue the worker after the composition is completed using the glanceId as
                    // tag so we can cancel all jobs in case the widget instance is deleted
                    val glanceId = LocalGlanceId.current
                    SideEffect {
                        ImageWorker.enqueue(context, size, glanceId)
                    }
                }
            }
        }
    }

    /**
     * Called when the widget instance is deleted. We can then clean up any ongoing task.
     */
    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)
        ImageWorker.cancel(context, glanceId)
    }

    /**
     * Create an ImageProvider using an URI if it's a "content://" type, otherwise load
     * the bitmap from the cache file
     *
     * Note: When using bitmaps directly your might reach the memory limit for RemoteViews.
     * If you do reach the memory limit, you'll need to generate a URI granting permissions
     * to the launcher.
     *
     * More info:
     * https://developer.android.com/training/secure-file-sharing/share-file#GrantPermissions
     */
    private fun getImageProvider(path: String): ImageProvider {
        if (path.startsWith("content://")) {
            return ImageProvider(path.toUri())
        }
        val bitmap = BitmapFactory.decodeFile(path)
        return ImageProvider(bitmap)
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        // Clear the state to show loading screen
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs.clear()
        }
        ImageWidget().update(context, glanceId)

        // Enqueue a job for each size the widget can be shown in the current state
        // (i.e landscape/portrait)
        GlanceAppWidgetManager(context).getAppWidgetSizes(glanceId).forEach { size ->
            ImageWorker.enqueue(context, size, glanceId, force = true)
        }
    }
}