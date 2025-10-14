package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.DeterminateCircularIndicator
import it.giovanni.hub.ui.items.DeterminateLinearIndicator
import it.giovanni.hub.ui.items.HubAlertDialog
import it.giovanni.hub.ui.items.HubAssistChip
import it.giovanni.hub.ui.items.HubFilterChip
import it.giovanni.hub.ui.items.HubInputChip
import it.giovanni.hub.ui.items.HubSuggestionChip
import it.giovanni.hub.ui.items.HubSwitch
import it.giovanni.hub.ui.items.ImageDialog
import it.giovanni.hub.ui.items.IndeterminateCircularIndicator
import it.giovanni.hub.ui.items.IndeterminateLinearIndicator
import it.giovanni.hub.ui.items.SimpleDialog
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.isScrolled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UIScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.ui_components),
    topics = listOf(
        "LazyListState/isScrolled",
        "FocusRequester",
        "Blur",
        "Switch",
        "derivedStateOf",
        "basicMarquee",
        "SnackBar",
        "BottomSheet",
        "SubList",
        "Progress Indicators",
        "Chips"
    )
) {
    val context = LocalContext.current

    val lazyListState: LazyListState = rememberLazyListState()

    var checked: Boolean by rememberSaveable { mutableStateOf(true) }
    val animatedBlur: State<Dp> = animateDpAsState(targetValue = if (checked) 10.dp else 0.dp, label = "Blur")

    var count: Int by remember { mutableIntStateOf(0) }
    val condition: Boolean by remember {
        derivedStateOf {
            Log.i("[derivedStateOf]", "Condition read")
            count > 3
        }
    }

    val focusRequester: FocusRequester = remember {
         FocusRequester()
    }

    val snackBarScope: CoroutineScope = rememberCoroutineScope()
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val sheetState: SheetState = rememberModalBottomSheetState()
    val bottomSheetScope: CoroutineScope = rememberCoroutineScope()
    var showBottomSheet: Boolean by remember { mutableStateOf(false) }

    val showSimpleDialog = remember { mutableStateOf(false) }
    val showAlertDialog = remember { mutableStateOf(false) }
    val showImageDialog = remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.badge_bottom),
                    contentDescription = "Badge Detail 2",
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState,
                contentPadding = getContentPadding(paddingValues = it)
            ) {
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        item {
                            HubSwitch(
                                checked = checked,
                                onCheckedChange = {
                                    checked = !checked
                                }
                            )
                        }

                        item {
                            Text(
                                modifier = Modifier.blur(radius = animatedBlur.value, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                                text = "Blur Effect",
                                color = MaterialTheme.colorScheme.primary,
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                item {
                    HubAssistChip()
                }

                item {
                    HubFilterChip()
                }

                item {
                    HubInputChip(
                        text = "Input chip",
                        onDismiss = {
                            Toast.makeText(context, "Input chip", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    HubSuggestionChip()
                }

                item {
                    DeterminateLinearIndicator()
                }

                item {
                    IndeterminateLinearIndicator()
                }

                item {
                    DeterminateCircularIndicator()
                }

                item {
                    IndeterminateCircularIndicator()
                }

                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            /*
                            scope.launch {
                                snackBarHostState.showSnackbar("SnackBar")
                            }
                            */
                            snackBarScope.launch {
                                val result = snackBarHostState
                                    .showSnackbar(
                                        message = "SnackBar",
                                        actionLabel = "Action",
                                        // Defaults to SnackbarDuration.Short
                                        duration = SnackbarDuration.Indefinite
                                    )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        /* Handle snackBar action performed */
                                    }
                                    SnackbarResult.Dismissed -> {
                                        /* Handle snackBar dismissed */
                                    }
                                }
                            }
                        }
                    ) {
                        Text("SnackBar")
                    }
                }

                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            showBottomSheet = true
                        }
                    ) {
                        Text("BottomSheet")
                    }
                }

                item {
                    /**
                     * By using derivedStateOf we are deriving the state of already existing state
                     * without causing the recomposition on every click because we are saving a
                     * recomposition count and updating the UI only when the count value changes.
                     * In this way we update the UI only when necessary.
                     * Here we log "Count > 3: false" only the first time we draw the content
                     * and then we no longer log this message every time we click the button.
                     * We log the message only when the condition "count > 3" is true.
                     */

                    Log.i("[derivedStateOf]", "Count > 3: $condition")

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            count += 1
                        }
                    ) {
                        Text("(derivedStateOf) Increment $count: $count > 3 $condition")
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.Center) {
                        item {
                            Button(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    showSimpleDialog.value = true
                                }
                            ) {
                                Text("Simple Dialog")
                            }
                        }
                        item {
                            Button(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    showAlertDialog.value = true
                                }
                            ) {
                                Text("Alert Dialog")
                            }
                        }
                        item {
                            Button(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    showImageDialog.value = true
                                }
                            ) {
                                Text("Image Dialog")
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .clickable {
                                focusRequester.requestFocus()
                            }
                            .basicMarquee(
                                animationMode = MarqueeAnimationMode.WhileFocused,
                                velocity = 100.dp
                            )
                            .focusRequester(focusRequester)
                            .focusable()
                    ) {
                        Image(
                            modifier = Modifier.size(size = 40.dp),
                            painter = painterResource(id = R.drawable.ico_locomotive),
                            contentDescription = "Locomotive"
                        )
                        repeat(20) {
                            Image(
                                modifier = Modifier.size(size = 40.dp),
                                painter = painterResource(id = R.drawable.ico_wagon),
                                contentDescription = "Wagon"
                            )
                        }
                    }
                }

                item {
                    SubList()
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = if (lazyListState.isScrolled) "Scrolling..." else "Inactive",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )

            // BottomSheet
            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxSize(),
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ) {
                    // BottomSheet content
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            bottomSheetScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Text("Hide")
                    }
                }
            }
        }
    )

    SimpleDialog(
        showDialog = showSimpleDialog,
        onDismissRequest = {
            showSimpleDialog.value = false
        }
    )

    HubAlertDialog(
        showDialog = showAlertDialog,
        onDismissRequest = {
            showAlertDialog.value = false
        },
        onConfirmation = {
            showAlertDialog.value = false
        }
    )

    ImageDialog(
        showDialog = showImageDialog,
        onDismissRequest = {
            showImageDialog.value = false
        },
        onConfirmation = {
            showImageDialog.value = false
        },
        painter = painterResource(id = R.drawable.logo_audioslave)
    )
}

@Composable
fun SubList() {
    val icons = Constants.icons + Constants.icons
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(count = icons.size) {index ->
            Box(
                modifier = Modifier
                    .size(size = 56.dp)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(all = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icons[index],
                    contentDescription = "Icon Icon",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIScreenPreview() {
    UIScreen(navController = rememberNavController())
}