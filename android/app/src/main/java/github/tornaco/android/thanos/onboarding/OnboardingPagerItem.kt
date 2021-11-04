package github.tornaco.android.thanos.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.module.compose.common.LottieLoadingView

@Composable
fun OnboardingPagerItem(item: Onboard) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieLoadingView(
            file = item.lottieFile,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            text = stringResource(id = item.title),
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = stringResource(id = item.description),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.onSurface
        )
    }
}
