package it.giovanni.hub.ui.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import it.giovanni.hub.BuildConfig
import it.giovanni.hub.R
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.navigation.routes.Login
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HubModalDrawerSheet(
    drawerState: DrawerState,
    drawerScope: CoroutineScope,
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    credentialManager: CredentialManager
) {
    val context: Context = LocalContext.current
    val repository = DataStoreRepository(context)

    var isLogoutLoading by remember { mutableStateOf(false) }
    var isSignOutLoading by remember { mutableStateOf(false) }

    fun kickOut() {
        drawerScope.launch {
            if (mainViewModel.getSignedInUser() != null) {
                mainViewModel.signOut(credentialManager = credentialManager)
                isLogoutLoading = false
                isSignOutLoading = false
            }

            mainViewModel.saveLoginState(state = false)

            navController.popBackStack()
            navController.navigate(route = Login) {
                popUpTo(route = Login)
            }
        }
    }

    ModalDrawerSheet(
        modifier = Modifier.width(width = 250.dp)
    ) {
        Spacer(modifier = Modifier.height(height = 12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(size = 100.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.drawable.logo_audioslave),
                contentDescription = "Circular Image"
            )
        }

        Spacer(modifier = Modifier.height(height = 12.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(height = 12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ThemeSwitcher(
                darkTheme = darkTheme,
                size = 50.dp,
                padding = 5.dp,
                onClick = onThemeUpdated
            )
        }

        Spacer(modifier = Modifier.height(height = 12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ColorSwitcher(
                dynamicColor = dynamicColor,
                size = 50.dp,
                padding = 5.dp,
                onClick = onColorUpdated
            )
        }

        NavigationDrawerItem(
            label = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Reset Theme",
                    textAlign = TextAlign.Center
                )
            },
            selected = false,
            onClick = {
                drawerScope.launch {
                    repository.resetTheme()
                }
            }
        )

        HorizontalDivider()

        NavigationDrawerItem(
            label = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Logout")
                    if (isLogoutLoading) {
                        HubProgressIndicator(modifier = Modifier.size(size = 32.dp), strokeWidth = 2.dp)
                    }
                }
            },
            selected = false,
            onClick = {
                isLogoutLoading = true
                kickOut()
            }
        )

        NavigationDrawerItem(
            label = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(modifier = Modifier.weight(1f), text = "Sign-out")
                    if (isSignOutLoading) {
                        HubProgressIndicator(modifier = Modifier.size(size = 32.dp), strokeWidth = 2.dp)
                    }
                }
            },
            selected = false,
            onClick = {
                drawerScope.launch(Dispatchers.IO) {
                    repository.resetEmail()
                }
                isSignOutLoading = true
                kickOut()
            }
        )

        NavigationDrawerItem(
            label = { Text(text = "Close") },
            selected = false,
            onClick = {
                // Handle the navigation or action.
                drawerScope.launch {
                    drawerState.close()
                }
            }
        )

        HorizontalDivider()

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha = 0.5f)
                .padding(vertical = 12.dp),
            text = "App version: " + BuildConfig.VERSION_NAME,
            textAlign = TextAlign.Center
        )

        HorizontalDivider()
    }
}