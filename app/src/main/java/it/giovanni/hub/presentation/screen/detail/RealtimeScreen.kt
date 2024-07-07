package it.giovanni.hub.presentation.screen.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import it.giovanni.hub.R
import it.giovanni.hub.data.model.realtime.Message
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun RealtimeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.realtime_database),
    topics = listOf("Realtime Database")
) { paddingValues ->

    val database: DatabaseReference = FirebaseDatabase
        .getInstance("https://myhub-06-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
        .child("Customers")

    val context: Context = LocalContext.current
    var text: String by remember { mutableStateOf("") }
    var messages: List<Message> by remember { mutableStateOf(emptyList()) }
    var names: List<String> by remember { mutableStateOf(emptyList()) }

    val user = mainViewModel.getSignedInUser()

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newMessages: List<Message> = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                messages = newMessages
                names = newMessages.map { user?.displayName + " - " + it.text }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        // contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter message") }
        )
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    val newMessage = Message(
                        id = database.push().key ?: "",
                        text = text,
                        timestamp = System.currentTimeMillis()
                    )
                    database
                        .child(user?.displayName + " - " + user?.uid)
                        .child(newMessage.id)
                        .setValue(newMessage)
                    text = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Send")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            items(names) { name ->
                Text(name, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RealtimeScreenPreview() {
    RealtimeScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}