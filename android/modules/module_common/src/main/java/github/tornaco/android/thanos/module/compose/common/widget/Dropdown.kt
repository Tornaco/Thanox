package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DropdownItem<T>(
    val labelLines: List<String>,
    val data: T,
    val badge: String? = null,
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
    widthFraction: Float = 0.76f,
    state: DropdownSelectorState = rememberDropdownSelectorState(),
    items: List<DropdownItem<T>>,
    onSelect: (DropdownItem<T>) -> Unit,
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(extraSmall = ThanoxCardRoundedCornerShape)
    ) {
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .clip(ThanoxCardRoundedCornerShape),
            expanded = state.isMenuOpen,
            onDismissRequest = {
                state.close()
            },
        ) {
            items.forEach { dropdownItem ->
                DropdownMenuItem(
                    modifier = Modifier.clip(ThanoxCardRoundedCornerShape),
                    text = {
                        Row(verticalAlignment = CenterVertically) {
                            dropdownItem.icon.invoke()
                            Spacer(modifier = Modifier.size(8.dp))
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = dropdownItem.labelLines[0],
                                    style = MaterialTheme.typography.titleSmall
                                )
                                dropdownItem.badge?.let { MD3Badge(text = it) }
                                if (dropdownItem.labelLines.size > 1) {
                                    dropdownItem.labelLines.subList(1, dropdownItem.labelLines.size)
                                        .forEach {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 10.sp
                                                )
                                            )
                                        }
                                }
                            }
                        }
                    }, onClick = {
                        state.close()
                        onSelect(dropdownItem)
                    })
            }
        }
    }
}

@Composable
fun <T> DropdownSelector(
    state: DropdownSelectorState = rememberDropdownSelectorState(),
    items: Map<String, List<DropdownItem<T>>>,
    onSelect: (DropdownItem<T>) -> Unit,
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(.68f),
        expanded = state.isMenuOpen,
        onDismissRequest = {
            state.close()
        },
    ) {
        items.forEach {
            val sectionTitle = it.key

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 4.dp),
                text = sectionTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W600)
            )

            val dropItems = it.value

            dropItems.forEach {
                DropdownMenuItem(text = {
                    Row(verticalAlignment = CenterVertically) {
                        it.icon.invoke()
                        Spacer(modifier = Modifier.size(8.dp))
                        Column {
                            Text(
                                text = it.labelLines[0],
                                style = MaterialTheme.typography.titleSmall
                            )
                            if (it.labelLines.size > 1) {
                                it.labelLines.subList(1, it.labelLines.size).forEach {
                                    Text(text = it, style = MaterialTheme.typography.titleSmall)
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
}