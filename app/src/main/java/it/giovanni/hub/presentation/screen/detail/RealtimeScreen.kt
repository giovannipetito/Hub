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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import it.giovanni.hub.data.model.SignedInUser
import it.giovanni.hub.data.model.realtime.Customer
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

    val signedInUser: SignedInUser? = mainViewModel.getSignedInUser()

    val database: DatabaseReference = FirebaseDatabase
        .getInstance("https://myhub-06-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
        .child("Customers")

    val context: Context = LocalContext.current
    var text: String by remember { mutableStateOf("") }
    val customerList: SnapshotStateList<Customer> = remember { mutableStateListOf() }
    var isCustomerChecked by remember { mutableStateOf(false) }

    fun addCustomer(database: DatabaseReference, customer: Customer) {
        val userValues = mapOf(
            "displayName" to customer.displayName,
            "email" to customer.email,
            "messages" to customer.messages
        )

        database.child(signedInUser?.uid.toString())
            .setValue(userValues)
            .addOnSuccessListener {
                println("Customer added successfully!")
            }
            .addOnFailureListener {
                println("Failed to add customer: ${it.message}")
            }
    }

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                customerList.clear()
                val customers: SnapshotStateList<Customer> = mutableStateListOf()
                snapshot.children.forEach { customerSnapshot ->
                    if (signedInUser?.uid == customerSnapshot.key) {
                        val displayName = customerSnapshot.child("displayName").getValue(String::class.java)
                        val email = customerSnapshot.child("email").getValue(String::class.java)
                        val messagesSnapshot = customerSnapshot.child("messages")
                        val dbMessages = mutableListOf<Message>()
                        messagesSnapshot.children.forEach { messageSnapshot ->
                            val dbText = messageSnapshot.child("text").getValue(String::class.java) ?: ""
                            val timestamp = messageSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                            dbMessages.add(Message(dbText, timestamp))
                        }
                        customers.add(Customer(displayName, email, dbMessages))
                    }
                }
                customerList.addAll(customers)

                // Check if the user is in the database, if not add the user
                if (!isCustomerChecked && signedInUser != null && customerList.none { it.email == signedInUser.email }) {
                    addCustomer(database, Customer(signedInUser.displayName, signedInUser.email, emptyList()))
                }
                isCustomerChecked = true
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun addMessage(database: DatabaseReference, uid: String, message: Message) {

        val key = database.push().key

        if (key != null) {
            val messageValues = mapOf(
                "text" to message.text,
                "timestamp" to message.timestamp
            )

            database
                .child(uid)
                .child("messages")
                .child(key)
                .setValue(messageValues)
                .addOnSuccessListener {
                    // Handle success
                    println("Message added successfully!")
                }
                .addOnFailureListener {
                    // Handle failure
                    println("Failed to add message: ${it.message}")
                }
        }
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
                    addMessage(
                        database,
                        signedInUser?.uid.toString(),
                        Message(text = text, timestamp = System.currentTimeMillis())
                    )
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
            items(customerList) { customer ->
                Text(text = customer.email.toString(), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RealtimeScreenPreview() {
    RealtimeScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}