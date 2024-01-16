package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.utils.Globals.colorList
import it.giovanni.hub.utils.swipeactions.SwipeAction
import it.giovanni.hub.utils.swipeactions.SwipeActionsBox

@Preview
@Composable
fun HubCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 12.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
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
            .height(100.dp)
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
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
            .height(100.dp)
            .padding(all = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
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
            .height(100.dp)
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
            modifier = Modifier.padding(16.dp),
            text = "Outlined Card"
        )
    }
}

@Composable
fun HubHeader(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = text,
            fontSize = 24.sp
        )
    }
}

@Composable
fun ContactCard(contact: Person) {

    val randomColor: Color by remember { mutableStateOf(colorList.random()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(randomColor)
                    .padding(6.dp),
                imageVector = Icons.Default.Person,
                colorFilter = ColorFilter.tint(color = Color.White),
                contentDescription = "Contact Image"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = contact.firstName + " " + contact.lastName,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Image(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Default.Phone,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                contentDescription = "Contact Image",
            )
        }
    }
}

@Composable
fun SwipeActionsCard(
    contact: Person,
    onSwipe: () -> Unit,
    onIconClick: () -> Unit
) {
    val emailAction = SwipeAction(
        // onSwipe = onSwipe,
        icon = {
            Box(
                modifier = Modifier
                    .size(size = 64.dp)
                    .background(color = Color.Green)
            ) {
                IconButton(
                    modifier = Modifier.padding(all = 16.dp),
                    onClick = onIconClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Action",
                        tint = Color.White
                    )
                }
            }
        }
    )

    val deleteAction = SwipeAction(
        // onSwipe = onSwipe,
        icon = {
            Box(
                modifier = Modifier
                    .size(size = 64.dp)
                    .background(color = Color.Red)
            ) {
                IconButton(
                    modifier = Modifier.padding(all = 16.dp),
                    onClick = onIconClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Action",
                        tint = Color.White
                    )
                }
            }
        }
    )

    SwipeActionsBox(
        leftActions = listOf(emailAction, deleteAction),
        rightActions = listOf(deleteAction, emailAction)
    ) {
        ContactCard(contact = contact)
    }
}

@Preview(showBackground = true)
@Composable
fun HubHeaderPreview() {
    HubHeader(text = "Header")
}

@Preview(showBackground = true)
@Composable
fun ContactCardPreview() {
    ContactCard(
        contact = Person(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            visibility = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SwipeActionsCardPreview() {
    SwipeActionsCard(
        contact = Person(
            id = 1,
            firstName = "Giovanni",
            lastName = "Petito",
            visibility = true
        ),
        onSwipe = {},
        onIconClick = {}
    )
}