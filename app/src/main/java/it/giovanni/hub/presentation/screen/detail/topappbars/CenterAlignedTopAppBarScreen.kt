package it.giovanni.hub.presentation.screen.detail.topappbars

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.LazyColumn1
import it.giovanni.hub.ui.items.backIcon
import it.giovanni.hub.ui.items.menuIcon
import it.giovanni.hub.utils.Constants.HUB_TOP_BAR_LANDSCAPE_HEIGHT
import it.giovanni.hub.utils.Constants.HUB_TOP_BAR_PORTRAIT_HEIGHT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBarScreen(navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var selected by remember { mutableStateOf(false) }

    val topAppBarHeight =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            HUB_TOP_BAR_PORTRAIT_HEIGHT
        else
            HUB_TOP_BAR_LANDSCAPE_HEIGHT

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .height(height = topAppBarHeight)
                    .paint(
                        painter = painterResource(id = R.drawable.badge_top_large),
                        alignment = Alignment.BottomEnd
                    ),
                title = {
                    Text(
                        "Center Aligned TopAppBar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED)
                            navController.popBackStack()
                    }) {
                        Icon(
                            modifier = Modifier.size(size = 24.dp),
                            painter = backIcon(),
                            contentDescription = "Back Icon"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        selected = !selected
                    }) {
                        Icon(
                            modifier = Modifier.size(size = 24.dp),
                            painter = menuIcon(),
                            contentDescription = "Menu Icon"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = if (!selected) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    TopAppBarDefaults.topAppBarColors()
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topAppBarHeight),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.badge_bottom),
                        contentDescription = "Badge Bottom"
                    )
                }

                LazyColumn1(paddingValues = paddingValues)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CenterAlignedTopAppBarScreenPreview() {
    CenterAlignedTopAppBarScreen(navController = rememberNavController())
}