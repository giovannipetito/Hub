package it.giovanni.hub.ui.items.cards

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.data.model.User

@Composable
fun AdaptiveCard(user: User, modifier: Modifier) {
    Row(modifier = modifier.border(width = 1.dp, color = Color.LightGray)) {
        AsyncImage(
            modifier = modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = user.avatar)
                .crossfade(true)
                .build(),
            contentDescription = "Adaptive Card Image",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = modifier.width(12.dp))

        BoxWithConstraints(
            modifier = modifier
                .weight(1.5f)
                .padding(vertical = 12.dp)
        ) {
            AdaptiveContent(user = user)
        }
    }
}

@Composable
fun BoxWithConstraintsScope.AdaptiveContent(user: User) {
    val badgeSize = 24.dp
    val padding = 24.dp
    val numberOfBadgesToShow = maxWidth.div(badgeSize + padding).toInt().minus(1)
    val remainingBadges = user.badges.size - numberOfBadgesToShow

    Log.d("[Adaptive]", "${this.maxWidth}")
    Column(verticalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = user.firstName + " " + user.lastName,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(end = 12.dp),
            text = user.description,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
            maxLines = if (this@AdaptiveContent.maxWidth > 250.dp) 10 else 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(space = padding)) {
            user.badges.take(numberOfBadgesToShow).forEach {
                Icon(
                    modifier = Modifier.size(badgeSize),
                    tint = MaterialTheme.colorScheme.secondary,
                    imageVector = it,
                    contentDescription = "Badge Icon"
                )
            }
            if (remainingBadges > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "+$remainingBadges",
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdaptiveCardPreview() {
    AdaptiveCard(
        user = User(
            id = 1,
            email = "janet.weaver@gmail.com",
            firstName = "Janet",
            lastName = "Weaver",
            avatar = "https://reqres.in/img/faces/2-image.jpg",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                    "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            badges = listOf(
                Icons.Default.Check,
                Icons.Default.Edit,
                Icons.Default.Face,
                Icons.Default.Email,
                Icons.Default.List,
                Icons.Default.Home
            )
        ),
        modifier = Modifier)
}

@Preview(showBackground = true)
@Composable
fun BoxWithConstraintsScope.AdaptiveContentPreview() {
    AdaptiveContent(
        user = User(
            id = 1,
            email = "janet.weaver@gmail.com",
            firstName = "Janet",
            lastName = "Weaver",
            avatar = "https://reqres.in/img/faces/2-image.jpg",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                    "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                    "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
            badges = listOf(
                Icons.Default.Check,
                Icons.Default.Edit,
                Icons.Default.Face,
                Icons.Default.Email,
                Icons.Default.List,
                Icons.Default.Home
            )
        )
    )
}