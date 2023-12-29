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

@Composable
fun CharacterCard(character: Character, modifier: Modifier) {

    val avatar: AsyncImagePainter = rememberAsyncImagePainter(model = character.image)

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                contentScale = ContentScale.FillBounds
            )

            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colorScheme.surface
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
fun HubCardPreview() {
    CharacterCard(
        character = Character(
            id = 1,
            name = "Giovanni Petito",
            status = "Impegnato",
            species = "Umano",
            type = "Simpatico",
            gender = "Maschio",
            image = "",
            episode = emptyList(),
            url = "",
            created = "06/02/1988"
        ),
        modifier = Modifier
    )
}