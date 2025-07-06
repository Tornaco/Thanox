package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun <T> MenuDialog(
    state: MenuDialogState<T>,
) {
    if (state.isShowing) {
        ThanoxDialog(
            onDismissRequest = {
                state.dismiss()
            },

            title = {
                DialogTitle(text = state.title(state._data))
            }) {

            state.message?.let {
                DialogMessage(text = it)
                StandardSpacer()
            }
            state.menuItems.forEach { dialogItem ->
                ListItem(
                    title = dialogItem.title,
                    text1 = dialogItem.summary,
                    icon = {
                        dialogItem.iconRes?.let {
                            Icon(painterResource(id = it), contentDescription = null)
                        }
                    },
                    onClick = {
                        state.dismiss()
                        state.onItemSelected(state._data, dialogItem.id)
                    })
            }

        }
    }
}


data class MenuDialogItem(
    val id: String,
    val title: String,
    val summary: String? = null,
    val iconRes: Int? = null
)

open class MenuDialogState<DATA>(
    val title: (DATA?) -> String,
    val message: String? = null,
    val menuItems: List<MenuDialogItem>,
    val onItemSelected: (DATA?, String) -> Unit
) :
    CommonDialogState() {
    var _data: DATA? = null

    fun show(data: DATA? = null) {
        _data = data

        show()
    }
}

@Composable
fun <T> rememberMenuDialogState(
    title: (T?) -> String,
    message: String? = null,
    menuItems: List<MenuDialogItem>,
    onItemSelected: (T?, String) -> Unit
): MenuDialogState<T> {
    return remember {
        MenuDialogState(title, message, menuItems, onItemSelected)
    }
}

@Composable
fun <T> rememberMenuDialogState(
    key1: Any?,
    title: (T?) -> String,
    message: String? = null,
    menuItems: List<MenuDialogItem>,
    onItemSelected: (T?, String) -> Unit
): MenuDialogState<T> {
    return remember(key1) {
        MenuDialogState(title, message, menuItems, onItemSelected)
    }
}


@Composable
fun <T> rememberMenuDialogState(
    key1: Any?,
    key2: Any?,
    title: (T?) -> String,
    message: String? = null,
    menuItems: List<MenuDialogItem>,
    onItemSelected: (T?, String) -> Unit
): MenuDialogState<T> {
    return remember(key1, key2) {
        MenuDialogState(title, message, menuItems, onItemSelected)
    }
}
