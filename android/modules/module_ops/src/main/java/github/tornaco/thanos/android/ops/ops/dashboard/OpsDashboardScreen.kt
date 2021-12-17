package github.tornaco.thanos.android.ops.ops.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.composethemeadapter3.Mdc3Theme
import github.tornaco.thanos.android.ops.ops.dashboard.pie.CenterText
import github.tornaco.thanos.android.ops.ops.dashboard.pie.ChartItem
import github.tornaco.thanos.android.ops.ops.dashboard.pie.Legend
import github.tornaco.thanos.android.ops.ops.dashboard.pie.PieChart

@Composable
fun OpsDashboardScreen() {
    Mdc3Theme {
        Surface {
            OpsDashboardContent()
        }
    }
}

@Composable
private fun OpsDashboardContent() {
    val viewModel = hiltViewModel<OpsDashboardViewModel>()
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = viewModel.toString())
        Spacer(modifier = Modifier.size(64.dp))

        val items = listOf(
            ChartItem(color = Color(0xFFE57600), value = 1, label = "Contacts"),
            ChartItem(color = Color(0xFF4485AA), value = 2, label = "Camera"),
            ChartItem(color = Color(0xFF94E287),
                value = 3,
                label = "External Photos"),
            ChartItem(color = Color(0xFF0093E5),
                value = 6,
                label = "Device Id"),
            ChartItem(color = Color(0xFFB446C8),
                value = 4,
                label = "Audio recorder"),
            ChartItem(color = Color(0xFF5A5AE6), value = 4, label = "Vib"),
        )

        PieChart(modifier = Modifier
            .fillMaxWidth(0.6f)
            .aspectRatio(1f),
            strokeSize = 38.dp,
            chartItems = items,
            centerText = CenterText(
                text = "Thanox",
                color = Color.DarkGray,
                size = 24.dp
            ))

        Spacer(modifier = Modifier.size(32.dp))

        Legend(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(32.dp),
            chartItems = items,
            columnCount = 1,
        )
    }
}
