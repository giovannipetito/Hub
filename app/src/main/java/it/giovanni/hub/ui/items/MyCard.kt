package it.giovanni.hub.ui.items

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.data.model.User
import it.giovanni.hub.ui.theme.MyShapes

@Composable
fun Card1(user: User, modifier: Modifier) {

    val avatar: AsyncImagePainter = rememberAsyncImagePainter(model = user.avatar)

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                contentScale = ContentScale.FillBounds,
            )

            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = user.firstName + " " + user.lastName)
                    Text(text = user.email)
                }
            }
        }
    }
}

@Composable
fun Card2(character: Character) {

    val avatar: AsyncImagePainter = rememberAsyncImagePainter(model = character.image)

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                contentScale = ContentScale.FillBounds,
            )

            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(text = character.name + ": " + character.type)
                    Text(text = character.species)
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
) {
    val expandedState = remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (expandedState.value) 180f else 0f,
        label = ""
    )
    Card(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize(
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        ), shape = MyShapes.medium
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(.3f)
                        .weight(1f)
                        .rotate(rotationState.value),
                    onClick = {
                        expandedState.value = expandedState.value.not()
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-down Arrow"
                    )
                }
            }
            if (expandedState.value) {
                Text(text = "Someone falls to pieces, sleeping all alone\n" +
                        "Someone kills the pain, spinning in the silence\n" +
                        "She finally drifts away\n" +
                        "Someone gets excited in a chapel yard, catches a bouquet\n" +
                        "Another lays a dozen white roses on a grave\n" +
                        "Yeah, and to be yourself is all that you can do\n" +
                        "Hey, to be yourself is all that you can do\n" +
                        "Someone finds salvation in everyone, another only pain\n" +
                        "Someone tries to hide himself, down inside himself he prays\n" +
                        "Someone swears his true love until the end of time, another runs away\n" +
                        "Separate or united, healthy or insane\n" +
                        "To be yourself is all that you can do\n" +
                        "And even when you've paid enough\n" +
                        "Been pulled apart or been held up\n" +
                        "Every single memory of the good or bad\n" +
                        "Faces of luck, don't lose any sleep tonight\n" +
                        "I'm sure everything will end up alright\n" +
                        "You may win or lose\n" +
                        "But to be yourself is all that you can do, yeah\n" +
                        "To be yourself is all that you can do, oh\n" +
                        "Be yourself is all that you can do",
                    fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 26,
                overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Card1Preview() {
    Card1(
        user = User(2, "janet.weaver@gmail.com", "Janet", "Weaver", "https://reqres.in/img/faces/2-image.jpg"),
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun Card2Preview() {
    Card2(character = Character(
        id = 1,
        name = "Giovanni",
        status = "single",
        species = "umano",
        type = "socevole",
        gender = "maschio",
        image = "",
        episode = emptyList(),
        url = "",
        created = "06/02/1988")
    )
}

@Preview(showBackground = true)
@Composable
fun ExpandableCardPreview() {
    ExpandableCard("My Title")
}