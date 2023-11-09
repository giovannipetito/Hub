package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import it.giovanni.hub.ui.items.ScreenSize
import it.giovanni.hub.ui.items.rememberScreenSize
import it.giovanni.hub.utils.ScreenType

@Composable
fun MultiSizeCard(user: User, screenSize: ScreenSize) {

    val maxLines = remember(key1 = screenSize) {
        mutableIntStateOf(if (screenSize.width == ScreenType.Compact) 4 else 10)
    }

    when (screenSize.height) {
        ScreenType.Expanded -> {
            Column {
                ColumnContent(
                    user = user,
                    screenSize = screenSize,
                    maxLines = maxLines.intValue
                )
            }
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RowContent(
                    user = user,
                    screenSize = screenSize,
                    maxLines = maxLines.intValue
                )
            }
        }
    }
}

@Composable
fun ColumnContent(
    user: User,
    screenSize: ScreenSize,
    maxLines: Int
) {
    val showIcons = remember(key1 = screenSize) {
        mutableStateOf(screenSize.height == ScreenType.Expanded)
    }

    AsyncImage(
        modifier = Modifier.fillMaxWidth().height(400.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = user.avatar)
            .crossfade(enable = true)
            .build(),
        contentDescription = "Image",
        contentScale = ContentScale.Crop
    )

    Column {
        Text(
            text = user.firstName + " " + user.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize =
                when (screenSize.height) {
                    ScreenType.Expanded -> MaterialTheme.typography.titleLarge.fontSize
                    else -> MaterialTheme.typography.titleMedium.fontSize
                },
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.disabled),
            text = user.description,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize =
                when (screenSize.height) {
                    ScreenType.Expanded -> MaterialTheme.typography.bodyLarge.fontSize
                    else -> MaterialTheme.typography.bodyMedium.fontSize
                }
            )
        )
        if (showIcons.value) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                user.badges.forEach {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = it,
                        contentDescription = "Badge Icon"
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.RowContent(
    user: User,
    screenSize: ScreenSize,
    maxLines: Int
) {
    val showIcons = remember(key1 = screenSize) {
        mutableStateOf(screenSize.width == ScreenType.Expanded || screenSize.height == ScreenType.Compact)
    }

    AsyncImage(
        modifier = Modifier
            .weight(1f),
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = user.avatar)
            .crossfade(enable = true)
            .build(),
        contentDescription = "Image",
        contentScale = ContentScale.Crop
    )

    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = user.firstName + " " + user.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize =
                when (screenSize.width) {
                    ScreenType.Expanded -> MaterialTheme.typography.titleLarge.fontSize
                    ScreenType.Medium -> MaterialTheme.typography.titleMedium.fontSize
                    else -> MaterialTheme.typography.titleSmall.fontSize
                },
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.disabled),
            text = user.description,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize =
                when (screenSize.width) {
                    ScreenType.Expanded -> MaterialTheme.typography.bodyLarge.fontSize
                    ScreenType.Medium -> MaterialTheme.typography.bodyMedium.fontSize
                    else -> MaterialTheme.typography.bodySmall.fontSize
                }
            )
        )
        if (showIcons.value) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                user.badges.forEach {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                        imageVector = it,
                        contentDescription = "Badge Icon"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultiScreenCardPreview() {
    MultiSizeCard(
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
        screenSize = rememberScreenSize()
    )
}

@Preview(showBackground = true)
@Composable
fun ColumnContentPreview() {
    ColumnContent(
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
        screenSize = rememberScreenSize(),
        maxLines = 10
    )
}