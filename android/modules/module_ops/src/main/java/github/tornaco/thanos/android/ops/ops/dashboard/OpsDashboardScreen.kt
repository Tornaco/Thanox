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
import github.tornaco.android.thanos.widget.bar.BarChart
import github.tornaco.android.thanos.widget.bar.ChartItem

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
            ChartItem("", Color.Red, 12, "Label", false),
            ChartItem("", Color.Red, 77, "Label", false),
            ChartItem("", Color.Red, 20, "Label", false),
            ChartItem("", Color.Red, 30, "Label", true),
            ChartItem("", Color.Red, 100, "Label", false),
            ChartItem("", Color.Red, 70, "Label", false),
            ChartItem("", Color.Red, 43, "Label", false),
        )
        BarChart(modifier = Modifier
            .fillMaxWidth(),
            items = items)
    }
}
