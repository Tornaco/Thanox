package github.tornaco.android.thanos.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import github.tornaco.android.thanos.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(
        pageCount = onboardingList.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true
    )
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                dragEnabled = false
            ) {
                OnboardingPagerItem(onboardingList[pagerState.currentPage])
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            ) {
                onboardingList.forEachIndexed { index, _ ->
                    OnboardingPagerSlide(
                        selected = index == pagerState.currentPage,
                        MaterialTheme.colorScheme.secondary
                    )
                }
            }
            FilledTonalButton(
                modifier = Modifier
                    .animateContentSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                onClick = {
                    if (pagerState.currentPage != onboardingList.size - 1) {
                        scope.launch {
                            pagerState.scrollToPage(page = pagerState.currentPage + 1)
                        }
                    } else {
                        onComplete()
                    }
                }
            ) {
                Text(
                    text = if (pagerState.currentPage == onboardingList.size - 1) {
                        stringResource(R.string.onboarding_text_complete)
                    } else {
                        stringResource(
                            R.string.onboarding_next
                        )
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}


data class Onboard(val title: Int, val description: Int, val lottieFile: String)

val onboardingList = listOf(
    Onboard(
        R.string.onboarding_github_tips_title,
        R.string.onboarding_github_tips_desc,
        "lottie/28189-github-octocat.json"
    ),
    Onboard(
        R.string.onboarding_guide_tips_title,
        R.string.onboarding_guide_tips_desc,
        "lottie/8617-open-book.json"
    ),
)

