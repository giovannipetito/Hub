package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.entries.WizardEntries
import it.giovanni.hub.ui.items.buttons.ContinueButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WizardScreen(navController: NavHostController) {
    val entries = listOf(
        WizardEntries.First,
        WizardEntries.Second,
        WizardEntries.Third
    )
    val pagerState: PagerState = rememberPagerState(pageCount = {3})

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            modifier = Modifier.weight(9f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(wizardEntries = entries[position])
        }

        HorizontalPagerIndicator(pagerState = pagerState)

        ContinueButton(
            modifier = Modifier.weight(2f),
            pagerState = pagerState
        ) {
            navController.popBackStack()
            navController.navigate(Graph.LOGIN_ROUTE) {
                popUpTo(Graph.LOGIN_ROUTE)
            }
        }
    }
}

@Composable
fun PagerScreen(wizardEntries: WizardEntries) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f),
            painter = painterResource(id = wizardEntries.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = wizardEntries.title,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = wizardEntries.description,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerIndicator(pagerState: PagerState) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color =
                if (pagerState.currentPage == iteration)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FirstWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardEntries = WizardEntries.First)
    }
}

@Composable
@Preview(showBackground = true)
fun SecondWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardEntries = WizardEntries.Second)
    }
}

@Composable
@Preview(showBackground = true)
fun ThirdWizardScreenPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        PagerScreen(wizardEntries = WizardEntries.Third)
    }
}