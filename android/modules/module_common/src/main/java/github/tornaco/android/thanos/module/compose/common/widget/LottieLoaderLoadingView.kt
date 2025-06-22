package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.widget.LottieLoadingView

@Composable
fun LottieLoaderLoadingView() {
    LottieLoadingView(
        file = "77686-loader.json",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun LottieLoaderSuccessView() {
    LottieLoadingView(
        file = "64787-success.json",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        iterations = 1
    )
}

@Composable
fun LottieLoaderFailView() {
    LottieLoadingView(
        file = "68783-bad-emoji.json",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        iterations = 1
    )
}