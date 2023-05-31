package it.giovanni.hub.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Data
import it.giovanni.hub.R
import it.giovanni.hub.viewmodels.RecyclerViewViewModel

@Composable
fun RecyclerViewScreen(navController: NavController, viewModel: RecyclerViewViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        // viewModel.fetchUsers(1)
        Users(viewModel)
    }
}

@Composable
fun Users(viewModel: RecyclerViewViewModel) {
    val users = remember {
        viewModel.users.value
    }

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(
            users!!
        ) {
            MyCard(data = it)
        }
    }
}

@Composable
fun MyCard(data: Data) {
    Card(
        modifier = Modifier
            .background(Color.LightGray)
            .shadow(elevation = 2.dp, shape = RectangleShape)
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp))
    ) {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(12.dp)
        ) {
            Text(
                text = data.firstName + " " + data.lastName,
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecyclerViewScreenPreview() {
    RecyclerViewScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun MyCardPreview() {
    MyCard(data = Data(1, "", "Giovanni", "Petito", ""))
}