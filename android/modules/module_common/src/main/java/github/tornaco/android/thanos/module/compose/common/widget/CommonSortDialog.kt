@file:OptIn(ExperimentalMaterial3Api::class)

package github.tornaco.android.thanos.module.compose.common.widget

import android.os.Parcelable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.icon.remix.R
import kotlinx.parcelize.Parcelize
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.io.Serializable

@Parcelize
data class SortItem(
    val index: Int,
    val title: String,
    val description: String? = null,
    val icon: String? = null
) : Parcelable, Serializable

class CommonSortState(
    val applySorted: (List<SortItem>) -> Unit
) :
    CommonDialogState()

@Composable
fun rememberCommonSortState(
    apply: (List<SortItem>) -> Unit
): CommonSortState {
    return remember {
        CommonSortState(apply)
    }
}

@Composable
fun CommonSortDialog(
    state: CommonSortState,
    items: List<SortItem>
) {
    if (state.isShowing) {
        ShortFullScreenXDialog(onDismissRequest = { state.dismiss() }) {
            DialogContent(state, items)
        }
    }
}


@Composable
private fun DialogContent(
    state: CommonSortState,
    menus: List<SortItem>
) {
    val data = remember(menus) {
        mutableStateOf(menus)
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(title = {
                Text(stringResource(id = github.tornaco.android.thanos.res.R.string.sort))
            }, actions = {
                IconButton(onClick = {
                    state.applySorted(data.value)
                    state.dismiss()
                }) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(
                            R.drawable.ic_remix_check_fill
                        ),
                        contentDescription = null
                    )
                }
            })
        }) { paddingValues ->
        val sortState = rememberReorderableLazyListState(onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        })

        LazyColumn(
            state = sortState.listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 64.dp)
                .reorderable(sortState)
                .detectReorderAfterLongPress(sortState)
        ) {
            items(data.value, { it }) { item ->
                ReorderableItem(sortState, key = item) { isDragging ->
                    val scale = animateFloatAsState(
                        if (isDragging) 1.06f else 1f,
                        label = "ScaleAnim"
                    )
                    ListItem(
                        modifier = Modifier.scale(scale.value),
                        title = item.title
                    )
                }
            }
        }
    }
}