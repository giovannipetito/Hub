package it.giovanni.hub.presentation.screen.detail

import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getContentPadding
import kotlin.random.Random

@Composable
fun DragAndDropScreen(navController: NavController) {

    val topics: List<String> = listOf("Drag & Drop")

    val colors: List<Color> = remember {
        (1..10).map {
            Color(Random.nextLong()).copy(alpha = 1f)
        }
    }

    var dragBoxIndex: Int by remember {
        mutableIntStateOf(0)
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.drag_and_drop),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            /*
            items(items = colors, key = {it.hashCode()}) { color ->
                val index: Int = colors.indexOf(color)
                DragAndDropItem(color, index)
            }
            */
            item {
                for (index in colors.indices) {
                    DragAndDropBox(
                        color = colors[index],
                        index = index,
                        dragBoxIndex = dragBoxIndex,
                        onBoxDropped = { newIndex ->
                            dragBoxIndex = newIndex
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragAndDropBox(
    color: Color,
    index: Int,
    dragBoxIndex: Int,
    onBoxDropped: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
            .background(color)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event
                        .mimeTypes()
                        .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = remember {
                    object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            val text = event.toAndroidDragEvent().clipData?.getItemAt(0)?.text
                            println("Drag data was $text")
                            onBoxDropped(index) // If I don't update dragBoxIndex I get the "Copy & Paste" feature.
                            return true
                        }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = index == dragBoxIndex,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            val text = "Drag me!"
            Text(
                text = text,
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    /*
                    .dragAndDropSource(
                        /*
                        drawDragDecoration = {
                            drawRect(color = Color.Red) // drawCircle(color = Color.Red)
                        }
                        */
                    ) {
                        detectTapGestures(
                            onLongPress = { offset ->
                                startTransfer(
                                    transferData = DragAndDropTransferData(
                                        clipData = ClipData.newPlainText("text", text)
                                    )
                                )
                            }
                        )
                    }
                */
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DragAndDropScreenPreview() {
    DragAndDropScreen(navController = rememberNavController())
}