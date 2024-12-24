package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Contact
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.data.model.realtime.Customer
import it.giovanni.hub.data.model.realtime.Message
import it.giovanni.hub.domain.entity.UserEntity
import it.giovanni.hub.utils.Globals.colorList
import it.giovanni.hub.utils.SwipeActionType
import it.giovanni.hub.utils.swipeactions.SwipeAction
import it.giovanni.hub.utils.swipeactions.SwipeActionsBox

@Preview
@Composable
fun HubCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
            .padding(all = 12.dp)
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Card"
        )
    }
}

@Preview
@Composable
fun HubFilledCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Filled Card"
        )
    }
}

@Preview
@Composable
fun HubElevatedCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
            .padding(all = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Elevated Card"
        )
    }
}

@Preview
@Composable
fun HubOutlinedCard() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Outlined Card"
        )
    }
}

@Composable
fun HubHeader(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 48.dp)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            modifier = Modifier.padding(all = 12.dp),
            text = text,
            fontSize = 24.sp
        )
    }
}

@Composable
fun PersonItem(person: Person) {

    val randomColor: Color by remember { mutableStateOf(colorList.random()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(size = 48.dp)
                    .clip(shape = CircleShape)
                    .background(color = randomColor)
                    .padding(all = 6.dp),
                imageVector = Icons.Default.Person,
                colorFilter = ColorFilter.tint(color = Color.White),
                contentDescription = "Person Icon"
            )
            Spacer(modifier = Modifier.width(width = 12.dp))
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = person.firstName + " " + person.lastName,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Image(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.Phone,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                contentDescription = "Person Icon",
            )
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {

    val randomColor: Color by remember { mutableStateOf(colorList.random()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(size = 48.dp)
                    .clip(shape = CircleShape)
                    .background(color = randomColor)
                    .padding(all = 6.dp),
                imageVector = Icons.Default.Person,
                colorFilter = ColorFilter.tint(color = Color.White),
                contentDescription = "Person Icon"
            )
            Spacer(modifier = Modifier.width(width = 12.dp))
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = contact.firstName + " " + contact.lastName,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Image(
                modifier = Modifier.size(size = 36.dp),
                imageVector = Icons.Default.Phone,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                contentDescription = "Phone Icon",
            )
        }
    }
}

@Composable
fun SwipeActionsItem(
    contact: Contact,
    onSwipe: (String) -> Unit,
    onIconClick: (String) -> Unit
) {
    val leftActions: ArrayList<SwipeAction> =
        swipeActionsBuilder(contactActions = contact.leftActions, onSwipe = onSwipe, onIconClick = onIconClick)
    val rightActions: ArrayList<SwipeAction> =
        swipeActionsBuilder(contactActions = contact.rightActions, onSwipe = onSwipe, onIconClick = onIconClick)

    SwipeActionsBox(
        leftActions = leftActions,
        rightActions = rightActions
    ) {
        ContactItem(contact = contact)
    }
}

fun swipeActionsBuilder(
    contactActions: List<String>,
    onSwipe: (String) -> Unit,
    onIconClick: (String) -> Unit
): ArrayList<SwipeAction> {

    val swipeActions: ArrayList<SwipeAction> = ArrayList()

    for (action in contactActions) {

        var actionName = ""
        var backgroundColor: Color = Color.Transparent
        var tintColor: Color = Color.White
        var imageVector: ImageVector = Icons.Default.Person

        when (action) {
            // Left actions:
            SwipeActionType.Email.name -> {
                actionName = SwipeActionType.Email.name
                backgroundColor = Color.Cyan
                tintColor = Color.Magenta
                imageVector = Icons.Default.Email
            }
            SwipeActionType.Share.name -> {
                actionName = SwipeActionType.Share.name
                backgroundColor = Color.Blue
                tintColor = Color.Yellow
                imageVector = Icons.Default.Share
            }
            SwipeActionType.Favorite.name -> {
                actionName = SwipeActionType.Favorite.name
                backgroundColor = Color.Green
                tintColor = Color.Red
                imageVector = Icons.Default.Favorite
            }

            // Right actions:
            SwipeActionType.Info.name -> {
                actionName = SwipeActionType.Info.name
                backgroundColor = Color.Magenta
                tintColor = Color.Cyan
                imageVector = Icons.Default.Info
            }
            SwipeActionType.Edit.name -> {
                actionName = SwipeActionType.Edit.name
                backgroundColor = Color.Yellow
                tintColor = Color.Blue
                imageVector = Icons.Default.Edit
            }
            SwipeActionType.Delete.name -> {
                actionName = SwipeActionType.Delete.name
                backgroundColor = Color.Red
                tintColor = Color.Green
                imageVector = Icons.Default.Delete
            }
        }

        val swipeAction = SwipeAction(
            onSwipe = {
                onSwipe("$it $actionName")
            },
            icon = {
                Box(
                    modifier = Modifier
                        .size(size = 64.dp)
                        .background(color = backgroundColor)
                ) {
                    IconButton(
                        modifier = Modifier.padding(all = 16.dp),
                        onClick = {
                            onIconClick(actionName)
                        }
                    ) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "Swipe Action",
                            tint = tintColor
                        )
                    }
                }
            }
        )

        swipeActions.add(swipeAction)
    }

    return swipeActions
}

@Composable
fun RoomItem(
    user: UserEntity,
    isUserById: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .border(width = 1.dp, color = Color.LightGray)
        .background(color = MaterialTheme.colorScheme.surface)
    ) {
        if (!isUserById) {
            Image(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(size = 56.dp)
                    .clip(shape = CircleShape)
                    .align(alignment = Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.logo_audioslave),
                contentDescription = "Logo Icon"
            )
        } else {
            Image(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(size = 56.dp)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .align(alignment = Alignment.CenterVertically),
                painter = rememberVectorPainter(image = Icons.Rounded.Star),
                colorFilter = ColorFilter.tint(color = Color.Yellow),
                contentDescription = "Star Icon"
            )
        }

        Column(
            modifier = Modifier
                .weight(3f)
                .padding(all = 12.dp)
                .align(alignment = Alignment.CenterVertically),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Age: ${user.age}",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Id: ${user.id}",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.tertiary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .height(height = 56.dp)
                .align(alignment = Alignment.CenterVertically),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clickable {
                        onEditClick()
                    },
                imageVector = Icons.Default.Edit,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                contentDescription = "Edit Icon",
            )
            Image(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clickable {
                        onDeleteClick()
                    },
                imageVector = Icons.Default.Delete,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                contentDescription = "Delete Icon",
            )
        }
    }
}

@Composable
fun RealtimeItem(
    customer: Customer,
    message: Message,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .border(width = 1.dp, color = Color.LightGray)
        .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Image(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(size = 56.dp)
                .clip(shape = CircleShape)
                .align(alignment = Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.logo_audioslave),
            contentDescription = "Logo Icon"
        )

        Column(
            modifier = Modifier
                .weight(3f)
                .padding(all = 12.dp)
                .align(alignment = Alignment.CenterVertically),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "${customer.displayName}",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "${customer.email}",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Message: ${message.text}",
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.tertiary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .height(height = 56.dp)
                .align(alignment = Alignment.CenterVertically),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clickable {
                        onEditClick()
                    },
                imageVector = Icons.Default.Edit,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                contentDescription = "Edit Icon",
            )
            Image(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clickable {
                        onDeleteClick()
                    },
                imageVector = Icons.Default.Delete,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
                contentDescription = "Delete Icon",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubHeaderPreview() {
    HubHeader(text = "Header")
}

@Preview(showBackground = true)
@Composable
fun PersonItemPreview() {
    PersonItem(
        person = Person(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            visibility = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactItem(
        contact = Contact(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            leftActions = emptyList(),
            rightActions = emptyList()
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SwipeActionsItemPreview() {
    SwipeActionsItem(
        contact = Contact(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            leftActions = emptyList(),
            rightActions = emptyList()
        ),
        onSwipe = {},
        onIconClick = {}
    )
}

@Preview(showBackground = false)
@Composable
fun RoomItemPreview() {
    RoomItem(
        user = UserEntity(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            age = "36",
        ),
        isUserById = false,
        onEditClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = false)
@Composable
fun RealtimeItemPreview() {
    RealtimeItem(
        customer = Customer(
            displayName = "Giovanni",
            email = "gi.petito@gmail.com",
            messages = emptyList()
        ),
        message = Message(
            text = "Hello World!",
            timestamp = System.currentTimeMillis()
        ),
        onEditClick = {},
        onDeleteClick = {}
    )
}