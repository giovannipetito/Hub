package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.CircularIndicator
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.isScrolled

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UIScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "LazyListState/isScrolled",
        "FocusRequester",
        "CircularIndicator",
        "Blur",
        "Switch",
        "derivedStateOf",
        "basicMarquee",
        "SubList"
    )

    val lazyListState: LazyListState = rememberLazyListState()

    val checked:MutableState<Boolean> = remember { mutableStateOf(true) }
    val animatedBlur: State<Dp> = animateDpAsState(targetValue = if (checked.value) 10.dp else 0.dp, label = "Blur")

    var count: Int by remember { mutableStateOf(0) }
    val condition: Boolean by remember {
        derivedStateOf {
            Log.i("[derivedStateOf]", "Condition read")
            count > 3
        }
    }

    val focusRequester: FocusRequester = remember {
         FocusRequester()
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.ui_components),
        topics = topics
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState,
                contentPadding = PaddingValues(bottom = it.calculateBottomPadding())
            ) {
                item {
                    var value by remember {
                        mutableIntStateOf(0)
                    }
                    CircularIndicator(
                        indicatorValue = value
                    )
                    TextField(
                        value = value.toString(),
                        onValueChange = { input ->
                            value = if (input.isNotEmpty()) {
                                input.toInt()
                            } else {
                                0
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.blur(radius = animatedBlur.value, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                        text = "Blur Effect",
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Switch(checked = checked.value, onCheckedChange = {checked.value = !checked.value})

                    Spacer(modifier = Modifier.height(12.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .basicMarquee(
                                animationMode = MarqueeAnimationMode.WhileFocused,
                                velocity = 100.dp
                            )
                            .focusRequester(focusRequester)
                            .focusable(),
                    ) {
                        Image(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.ico_locomotive),
                            contentDescription = "Locomotive"
                        )
                        repeat(20) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = R.drawable.ico_wagon),
                                contentDescription = "Wagon"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            focusRequester.requestFocus()
                        }
                    ) {
                        Text("Start train")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    SubList()
                }
            }

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = if (lazyListState.isScrolled) "Scrolling..." else "Inactive",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun SubList() {
    val icons = Constants.icons + Constants.icons
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(count = icons.size) {index ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icons[index],
                    contentDescription = "Icon",
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