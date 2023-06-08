package it.giovanni.hub.ui.items

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
import it.giovanni.hub.Data

@Composable
fun MyCard(data: Data) {

    val avatar: AsyncImagePainter = rememberAsyncImagePainter(model = data.avatar)

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = avatar,
                contentDescription = null,
                modifier = Modifier.width(300.dp).height(300.dp),
                contentScale = ContentScale.FillBounds,
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
                    Text(text = data.firstName + " " + data.lastName)
                    Text(text = data.email)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyCardPreview() {
    MyCard(data = Data(2, "janet.weaver@gmail.com", "Janet", "Weaver", "https://reqres.in/img/faces/2-image.jpg"))
}