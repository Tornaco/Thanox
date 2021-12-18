package github.tornaco.android.thanos.widget.pie

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> Legend(
    modifier: Modifier = Modifier,
    itemPadding: PaddingValues = PaddingValues(2.dp),
    columnCount: Int = 1,
    dotSize: Dp = 12.dp,
    shape: Shape = CircleShape,
    chartItems: List<ChartItem<T>>,
) {

    val stateList = chartItems.toStateList()

    LazyVerticalGrid(modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        cells = GridCells.Fixed(columnCount)) {
        items(stateList) { state ->
            Row(modifier = Modifier
                .padding(itemPadding),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(shape)
                        .background(state.chartItem.color)
                )
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = state.chartItem.label,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}