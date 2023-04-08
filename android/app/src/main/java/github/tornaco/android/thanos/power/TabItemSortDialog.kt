package github.tornaco.android.thanos.power

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.module.compose.common.widget.DialogTitle
import github.tornaco.android.thanos.module.compose.common.widget.ListItem
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxDialog
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

class TabItemSortState(val applySorted: (List<TabItem>) -> Unit) {
    private var _isShow by mutableStateOf(false)
    val isShow get() = _isShow

    var actions: List<TabItem> = emptyList()

    fun show(actions: List<TabItem>) {
        this.actions = actions
        _isShow = true
    }

    fun dismiss() {
        _isShow = false
    }
}

@Composable
fun rememberTabItemSortState(
    apply: (List<TabItem>) -> Unit
): TabItemSortState {
    return remember {
        TabItemSortState(apply)
    }
}

@Composable
fun TabItemSortDialog(state: TabItemSortState) {
    if (state.isShow) {
        val data = remember(state.actions) {
            mutableStateOf(state.actions)
        }
        ThanoxDialog(onDismissRequest = { state.dismiss() }, title = {
            DialogTitle(text = stringResource(id = github.tornaco.android.thanos.R.string.common_darg_sort))
        }, buttons = {
            TextButton(onClick = {
                state.applySorted(data.value)
                state.dismiss()
            }) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        }, content = {
            val sortState = rememberReorderableLazyListState(onMove = { from, to ->
                data.value = data.value.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            })

            LazyColumn(
                state = sortState.listState,
                modifier = Modifier
                    .fillMaxHeight(0.42f)
                    .reorderable(sortState)
                    .detectReorderAfterLongPress(sortState)
            ) {
                items(items = data.value, key = { it }) { item ->
                    ReorderableItem(
                        reorderableState = sortState,
                        key = item
                    ) { isDragging ->
                        val scale = animateFloatAsState(if (isDragging) 1.10f else 1f)
                        ListItem(
                            modifier = Modifier.scale(scale.value),
                            title = item.pkgSet.label
                        )
                    }
                }
            }
        })
    }
}