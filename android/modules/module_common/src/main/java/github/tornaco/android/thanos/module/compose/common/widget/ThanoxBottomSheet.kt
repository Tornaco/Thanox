package github.tornaco.android.thanos.module.compose.common.widget

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import kotlinx.coroutines.launch

class ThanoxBottomSheetState : CommonDialogState() {
    var args by mutableStateOf("")
    fun show(args: String) {
        this.args = args
        show()
    }

    override fun dismiss() {
        super.dismiss()
        ThanoxBottomSheetStoreOwner.viewModelStore.clear()
    }
}

@Composable
fun rememberThanoxBottomSheetState(): ThanoxBottomSheetState {
    return remember {
        ThanoxBottomSheetState()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThanoxBottomSheet(
    state: ThanoxBottomSheetState,
    content: @Composable ColumnScope.(args: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    if (state.isShowing) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { state.dismiss() },
        ) {
            BackHandler {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        state.dismiss()
                    }
                }
            }

            CompositionLocalProvider(value = LocalViewModelStoreOwner provides ThanoxBottomSheetStoreOwner) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    content(state.args)
                }
            }
        }
    }
}

object ThanoxBottomSheetStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()
}