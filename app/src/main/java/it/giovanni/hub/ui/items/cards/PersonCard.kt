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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person

@Composable
fun PersonCard(person: Person, modifier: Modifier) {

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(all = 24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.giovanni),
                contentDescription = null,
                modifier = Modifier
                    .width(width = 300.dp)
                    .height(height = 300.dp),
                contentScale = ContentScale.Crop
            )

            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .3f),
                modifier = Modifier.align(Alignment.BottomCenter),
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp)
                ) {
                    Text(text = person.firstName)
                    Text(text = person.lastName)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonCardPreview() {
    PersonCard(
        person = Person(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            visibility = true
        ),
        modifier = Modifier
    )
}