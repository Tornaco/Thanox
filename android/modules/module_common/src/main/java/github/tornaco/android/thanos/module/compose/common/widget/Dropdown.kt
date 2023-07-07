package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val DROPDOWN_WIDTH_FRACTION = 0.6f

data class DropdownItem<T>(
    val labelLines: List<String>,
    val data: T,
    val icon: @Composable () -> Unit = {}
)

class DropdownSelectorState {
    private var _isMenuOpen by mutableStateOf(false)
    val isMenuOpen get() = _isMenuOpen

    fun open() {
        _isMenuOpen = true
    }

    fun close() {
        _isMenuOpen = false
    }
}

@Composable
fun rememberDropdownSelectorState(): DropdownSelectorState {
    return remember {
        DropdownSelectorState()
    }
}

@Composable
fun <T> DropdownSelector(
    widthFraction: Float = 0.6f,
    state: DropdownSelectorState = rememberDropdownSelectorState(),
    items: List<DropdownItem<T>>,
    onSelect: (DropdownItem<T>) -> Unit,
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(widthFraction),
        expanded = state.isMenuOpen,
        onDismissRequest = {
            state.close()
        },
    ) {
        items.forEach {
            DropdownMenuItem(text = {
                Row(verticalAlignment = CenterVertically) {
                    it.icon.invoke()
                    Spacer(modifier = Modifier.size(8.dp))
                    Column {
                        Text(text = it.labelLines[0], style = MaterialTheme.typography.titleSmall)
                        if (it.labelLines.size > 1) {
                            it.labelLines.subList(1, it.labelLines.size).forEach {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                                )
                            }
                        }
                    }
                }
            }, onClick = {
                state.close()
                onSelect(it)
            })
        }
    }
}
