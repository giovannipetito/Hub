package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.data.model.User

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

@Preview(showBackground = true)
@Composable
fun Card1Preview() {
    Card1(
        user = User(
            1,
            "janet.weaver@gmail.com",
            "Janet",
            "Weaver",
            "https://reqres.in/img/faces/2-image.jpg",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                    "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            emptyList()
        ),
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