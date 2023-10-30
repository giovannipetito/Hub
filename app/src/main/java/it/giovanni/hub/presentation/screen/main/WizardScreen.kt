package it.giovanni.hub.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import it.giovanni.hub.Graph
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.util.WizardPage
import it.giovanni.hub.presentation.viewmodel.WizardViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun WizardScreen(
    navController: NavHostController,
    mainActivity: MainActivity,
    viewModel: WizardViewModel = hiltViewModel()
) {
    val pages = listOf(
        WizardPage.First,
        WizardPage.Second,
        WizardPage.Third
    )
    val pagerState = rememberPagerState(pageCount = {3})

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(wizardPage = pages[position])
        }
        /*
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            pagerState = pagerState
        )
        */
        FinishButton(
            modifier = Modifier.weight(1f),
            pagerState = pagerState
        ) {
            viewModel.saveWizardState(state = true)
            navController.popBackStack()
            navController.navigate(Graph.LOGIN_ROUTE) {
                popUpTo(Graph.LOGIN_ROUTE)
            }
        }
    }
}

@Composable
fun PagerScreen(wizardPage: WizardPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = wizardPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = wizardPage.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = wizardPage.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Finish")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FirstWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardPage = WizardPage.First)
    }
}

@Composable
@Preview(showBackground = true)
fun SecondWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardPage = WizardPage.Second)
    }
}

@Composable
@Preview(showBackground = true)
fun ThirdWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardPage = WizardPage.Third)
    }
}