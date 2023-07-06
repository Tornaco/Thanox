package github.tornaco.thanos.android.ops2

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import github.tornaco.android.thanos.core.ops.PermState

@Composable
fun PermState.displayLabel(): String {
    val resId = when (this) {
        PermState.DEFAULT -> R.string.module_ops2_perm_state_default
        PermState.NOT_SET -> R.string.module_ops2_perm_state_not_set
        PermState.DENY -> R.string.module_ops2_perm_state_deny
        PermState.ASK -> R.string.module_ops2_perm_state_ask
        PermState.ALLOW_ALWAYS -> R.string.module_ops2_perm_state_allow
        PermState.ALLOW_FOREGROUND_ONLY -> R.string.module_ops2_perm_state_foreground
        PermState.UNKNOWN -> R.string.module_ops2_perm_state_unknown
        PermState.IGNORE -> R.string.module_ops2_perm_state_ignore
    }
    return stringResource(resId)
}