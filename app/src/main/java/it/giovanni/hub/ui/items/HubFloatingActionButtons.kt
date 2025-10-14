package it.giovanni.hub.ui.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.domain.entity.UserEntity
import it.giovanni.hub.utils.Globals

@Composable
fun ExpandableRoomFAB(
    paddingValues: PaddingValues,
    users: List<UserEntity>,
    onShowCreateUserDialog: (Boolean) -> Unit,
    onShowDeleteUsersDialog: (Boolean) -> Unit,
    onResetUserInfo: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotateAnimation = animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = Globals.getFloatingActionButtonPadding(paddingValues = paddingValues)),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowCreateUserDialog(true)
                        onResetUserInfo()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = painterResource(id = R.drawable.ico_add),
                        contentDescription = "Add Icon"
                    )
                }
            }

            if (users.isNotEmpty()) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    FloatingActionButton(
                        onClick = {
                            onShowDeleteUsersDialog(true)
                            onResetUserInfo()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(size = 24.dp),
                            painter = painterResource(id = R.drawable.ico_delete),
                            contentDescription = "Delete Icon"
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    isExpanded = !isExpanded
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 24.dp)
                        .rotate(degrees = rotateAnimation.value),
                    painter = painterResource(id = R.drawable.ico_rotate),
                    contentDescription = "Rotate Icon"
                )
            }
        }
    }
}

@Composable
fun ExpandableRealtimeFAB(
    paddingValues: PaddingValues,
    onShowCreateMessageDialog: (Boolean) -> Unit,
    onShowDeleteMessageDialog: (Boolean) -> Unit,
    onResetMessage: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotateAnimation = animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = Globals.getFloatingActionButtonPadding(paddingValues = paddingValues)),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowCreateMessageDialog(true)
                        onResetMessage()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = painterResource(id = R.drawable.ico_add),
                        contentDescription = "Add Icon"
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowDeleteMessageDialog(true)
                        onResetMessage()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = painterResource(id = R.drawable.ico_delete),
                        contentDescription = "Delete Icon"
                    )
                }
            }

            FloatingActionButton(
                onClick = {
                    isExpanded = !isExpanded
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    modifier = Modifier
                        .size(size = 24.dp)
                        .rotate(degrees = rotateAnimation.value),
                    painter = painterResource(id = R.drawable.ico_rotate),
                    contentDescription = "Rotate Icon"
                )
            }
        }
    }
}