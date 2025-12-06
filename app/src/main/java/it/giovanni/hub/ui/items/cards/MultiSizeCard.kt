package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.presentation.model.UiUser
import it.giovanni.hub.ui.items.ScreenSize
import it.giovanni.hub.ui.items.rememberScreenSize
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.DeviceType
import it.giovanni.hub.utils.ScreenType

@Composable
fun MultiSizeCard(user: UiUser, screenSize: ScreenSize) {

    val maxLines = remember(key1 = screenSize) {
        mutableIntStateOf(
            when (screenSize.width) {
                ScreenType.Small -> 4
                ScreenType.Medium -> 6
                else -> 10
            }
        )
    }

    if (screenSize.height == ScreenType.Medium && screenSize.width == ScreenType.Small) {
        // Smartphone in portrait mode.
        Column(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        ) {
            ColumnContent(user = user, screenSize = screenSize, deviceType = DeviceType.SmartphoneInPortraitMode, maxLines = maxLines.intValue)
        }
    }
    if (screenSize.height == ScreenType.Small && screenSize.width == ScreenType.Medium) {
        // Smartphone in landscape mode.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RowContent(user = user, screenSize = screenSize, deviceType = DeviceType.SmartphoneInLandscapeMode, maxLines = maxLines.intValue)
        }
    }
    if (screenSize.height == ScreenType.Large && screenSize.width == ScreenType.Medium) {
        // Tablet in portrait mode.
        Column(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        ) {
            ColumnContent(user = user, screenSize = screenSize, deviceType = DeviceType.TabletInPortraitMode, maxLines = maxLines.intValue)
        }
    }
    if (screenSize.height == ScreenType.Medium && screenSize.width == ScreenType.Large) {
        // Tablet in landscape mode.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RowContent(user = user, screenSize = screenSize, deviceType = DeviceType.TabletInLandscapeMode, maxLines = maxLines.intValue)
        }
    }
}

@Composable
fun ColumnContent(
    user: UiUser,
    screenSize: ScreenSize,
    deviceType: DeviceType,
    maxLines: Int
) {
    val showIcons = remember(key1 = screenSize) {
        mutableStateOf(deviceType == DeviceType.TabletInPortraitMode)
    }

    AsyncImage(
        modifier = Modifier.fillMaxWidth(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = user.avatar)
            .crossfade(enable = true)
            .placeholder(R.drawable.ico_loading_user)
            .error(R.drawable.ico_error_user)
            .build(),
        contentDescription = "MultiSize Card Image",
        contentScale = ContentScale.FillWidth
    )

    Column {
        Spacer(modifier = Modifier.height(height = 6.dp))
        Text(
            text = user.firstName + " " + user.lastName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = when (deviceType) {
                    DeviceType.SmartphoneInPortraitMode -> MaterialTheme.typography.titleSmall.fontSize
                    DeviceType.TabletInPortraitMode -> MaterialTheme.typography.titleLarge.fontSize
                    else -> MaterialTheme.typography.titleMedium.fontSize
                }
            )
        )
        Spacer(modifier = Modifier.height(height = 6.dp))
        Text(
            text = user.description,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = when (deviceType) {
                    DeviceType.SmartphoneInPortraitMode -> MaterialTheme.typography.bodySmall.fontSize
                    DeviceType.TabletInPortraitMode -> MaterialTheme.typography.bodyLarge.fontSize
                    else -> MaterialTheme.typography.bodyMedium.fontSize
                }
            )
        )
        Spacer(modifier = Modifier.height(height = 12.dp))
        if (showIcons.value) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp)
            ) {
                user.badgeIds.forEach {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                        painter = painterResource(it),
                        contentDescription = "Badge Icon"
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.RowContent(
    user: UiUser,
    screenSize: ScreenSize,
    deviceType: DeviceType,
    maxLines: Int
) {
    val showIcons = remember(key1 = screenSize) {
        mutableStateOf(deviceType == DeviceType.SmartphoneInLandscapeMode || deviceType == DeviceType.TabletInLandscapeMode)
    }

    AsyncImage(
        modifier = Modifier.weight(1f),
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
                fontWeight = FontWeight.Bold,
                fontSize = when (deviceType) {
                    DeviceType.SmartphoneInLandscapeMode -> MaterialTheme.typography.titleMedium.fontSize
                    DeviceType.TabletInLandscapeMode -> MaterialTheme.typography.titleLarge.fontSize
                    else -> MaterialTheme.typography.titleSmall.fontSize
                }
            )
        )
        Spacer(modifier = Modifier.height(height = 6.dp))
        Text(
            text = user.description,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = when (deviceType) {
                    DeviceType.SmartphoneInLandscapeMode -> MaterialTheme.typography.bodyMedium.fontSize
                    DeviceType.TabletInLandscapeMode -> MaterialTheme.typography.bodyLarge.fontSize
                    else -> MaterialTheme.typography.bodySmall.fontSize
                }
            )
        )
        if (showIcons.value) {
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp)
            ) {
                user.badgeIds.forEach {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                        painter = painterResource(it),
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
        user = UiUser(
            id = 1,
            email = "janet.weaver@gmail.com",
            fullName = "Janet Weaver",
            firstName = "Janet",
            lastName = "Weaver",
            avatar = "https://reqres.in/img/faces/2-image.jpg",
            description = Constants.LOREM_IPSUM_LONG_TEXT,
            badgeIds = Constants.ICON_IDS
        ),
        screenSize = rememberScreenSize()
    )
}