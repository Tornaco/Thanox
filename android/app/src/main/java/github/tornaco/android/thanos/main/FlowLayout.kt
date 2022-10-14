package github.tornaco.android.thanos.main

import android.view.Gravity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private fun <T> splitList(
    messagesList: List<T>,
    groupSize: Int
): List<List<T>> {
    val length = messagesList.size
    val num = (length + groupSize - 1) / groupSize
    val newList: MutableList<List<T>> = ArrayList(num)
    for (i in 0 until num) {
        val fromIndex = i * groupSize
        val toIndex = if ((i + 1) * groupSize < length) (i + 1) * groupSize else length
        newList.add(messagesList.subList(fromIndex, toIndex))
    }
    return newList
}

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    lineSpacing: Dp = 0.dp,
    gravity: Int = Gravity.TOP,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val columnCount = 4
        val parentWidthSize = constraints.maxWidth
        val allPlaceables: List<List<Placeable>> = splitList(
            messagesList = measurables.map {
                it.measure(constraints)
            },
            groupSize = columnCount
        )
        val totalHeight = allPlaceables.sumOf { list ->
            list.maxOf { it.height }
        } + (if (allPlaceables.size > 1) lineSpacing.toPx().toInt() else 0)

        layout(parentWidthSize, totalHeight) {
            var topOffset = 0
            for (i in allPlaceables.indices) {
                val lineViews = allPlaceables[i]
                val lineHeight = lineViews.maxOf { it.height }
                val padding =
                    (parentWidthSize - (columnCount * lineViews.first().width)) / (columnCount - 1)
                var leftOffset = 0

                for (j in lineViews.indices) {
                    val child = lineViews[j]
                    val childWidth = child.width
                    val childHeight = child.height
                    val childTop = getItemTop(gravity, lineHeight, topOffset, childHeight)
                    child.placeRelative(leftOffset, childTop)
                    leftOffset += childWidth + padding
                }
                topOffset += lineHeight + lineSpacing.toPx().toInt()
            }
        }
    }
}

private fun getItemTop(gravity: Int, lineHeight: Int, topOffset: Int, childHeight: Int): Int {
    return when (gravity) {
        Gravity.CENTER -> topOffset + (lineHeight - childHeight) / 2
        Gravity.BOTTOM -> topOffset + lineHeight - childHeight
        else -> topOffset
    }
}
