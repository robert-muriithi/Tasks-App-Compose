package dev.robert.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.theme.Theme
import dev.robert.design_system.presentation.theme.TodoTheme
import dev.robert.onboarding.R
import dev.robert.onboarding.domain.model.OnboardingItem
import dev.robert.onboarding.presentation.components.OnboardingPagerIndicator

@Composable
fun OnBoarding(
    viewModel: OnBoardingViewModel = hiltViewModel(),
    onCompleteOnBoarding: () -> Unit = {},
) {
    val currentValue by viewModel.currentValue.collectAsStateWithLifecycle()
    val pages = onBoardingItems
    val pagerState = rememberPagerState(
        pageCount = { pages.size },
        initialPage = currentValue
    )
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingScreenContent(page = pages[page], modifier = Modifier.fillMaxSize())
        }
        OnboardingPagerIndicator(
            pagerState = pagerState,
            onDoneClick = {
                viewModel.onEvent(OnboardingScreenEvent.OnboardingCompleted)
                onCompleteOnBoarding()
            },
            onboardingItems = pages,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
fun OnboardingScreenContent(
    page: OnboardingItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = "Onboarding Image",
            modifier = Modifier.size(300.dp),
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp),
        )
    }
}
val onBoardingItems @Composable get() = listOf(
    OnboardingItem(
        title = stringResource(R.string.welcome_to_taskify),
        description = stringResource(R.string.item_1_description),
        image = R.drawable.ic_onboarding_item1,
    ),
    OnboardingItem(
        title = stringResource(R.string.create_tasks),
        description = stringResource(R.string.item_2_description),
        image = R.drawable.ic_onboarding_item2,
    ),
    OnboardingItem(
        title = stringResource(R.string.track_progress),
        description = stringResource(R.string.item_3_description),
        image = R.drawable.ic_onboarding_item3,
    ),
)

@Preview
@Composable
private fun OnBoardingScreenPreview() {
    TodoTheme(theme = Theme.LIGHT_THEME.themeValue) {
        OnboardingScreenContent(page = onBoardingItems[0])
    }
}
