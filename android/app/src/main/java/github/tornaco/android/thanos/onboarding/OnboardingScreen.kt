package github.tornaco.android.thanos.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import github.tornaco.android.thanos.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState(
        pageCount = onboardingList.size,
        initialOffscreenLimit = 2,
        infiniteLoop = true
    )
    val scope = rememberCoroutineScope()

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        MaterialTheme.colors.secondary,
                        Icons.Filled.Album
                    )
                }
            }
            Button(
                onClick = {
                    if (pagerState.currentPage != onboardingList.size - 1) {
                        scope.launch {
                            pagerState.scrollToPage(page = pagerState.currentPage + 1)
                        }
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier
                    .animateContentSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            ) {
                Text(
                    text = if (pagerState.currentPage == onboardingList.size - 1) {
                        stringResource(R.string.onboarding_text_complete)
                    } else {
                        stringResource(
                            R.string.onboarding_next
                        )
                    },
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }
    }
}


data class Onboard(val title: Int, val description: Int, val lottieFile: String)

val onboardingList = listOf(
    Onboard(
        R.string.onboarding_xposed_tips_title,
        R.string.onboarding_xposed_tips_desc,
        "lottie/19527-select-option.json"
    ),
    Onboard(
        R.string.onboarding_magisk_tips_title,
        R.string.onboarding_magisk_tips_desc,
        "lottie/11031-download.json"
    ),
    Onboard(
        R.string.onboarding_github_tips_title,
        R.string.onboarding_github_tips_desc,
        "lottie/28189-github-octocat.json"
    ),
)

