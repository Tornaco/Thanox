package github.tornaco.android.thanox.module.notification.recorder.ui.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxMediumAppBarScaffold
import github.tornaco.android.thanos.module.compose.common.widget.pie.CenterText
import github.tornaco.android.thanos.module.compose.common.widget.pie.ChartItem
import github.tornaco.android.thanos.module.compose.common.widget.pie.Legend
import github.tornaco.android.thanos.module.compose.common.widget.pie.PieChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsChartScreen(
    onBackPressed: () -> Unit,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    val viewModel = hiltViewModel<StatsViewModel>()
    val chartState = viewModel.flow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.startLoading()
    }
    ThanoxMediumAppBarScaffold(title = {
        Text(
            stringResource(id = github.tornaco.android.thanos.res.R.string.module_notification_recorder_stats),
            style = MaterialTheme.typography.titleMedium
        )
    }, onBackPressed = onBackPressed) {
        Box(Modifier.padding(it)) {
            StatsChartContent(
                chartState = chartState.value,
                onItemSelected = onItemSelected,
                onCenterSelected = onCenterSelected
            )
        }
    }
}

@Composable
fun StatsChartContent(
    chartState: StatsChartState,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    if (chartState.isLoading) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
    } else {
        StatChart(chartState, onItemSelected, onCenterSelected)
    }
}

@Composable
private fun StatChart(
    state: StatsChartState,
    onItemSelected: ((ChartItem<String>) -> Unit),
    onCenterSelected: (() -> Unit),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PieChart(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            strokeSize = 38.dp,
            chartItems = state.entries,
            centerText = CenterText(
                text = state.totalCount.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                size = 24.dp
            ),
            onItemSelected = onItemSelected,
            onCenterSelected = onCenterSelected
        )

        Spacer(modifier = Modifier.size(32.dp))

        Legend(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(32.dp),
            chartItems = state.entries,
            columnCount = 1,
        )
    }
}