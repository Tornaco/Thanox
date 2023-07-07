package github.tornaco.thanos.android.ops2

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

val ByPassColor = Color(0xff1A9431)

@Composable
fun PermState.displayColor(): Color {
    return when (this) {
        PermState.NOT_SET -> Color.DarkGray
        PermState.DENY -> Color.Red
        PermState.IGNORE -> Color.Red
        PermState.ALLOW_ALWAYS -> ByPassColor
        PermState.ALLOW_FOREGROUND_ONLY -> ByPassColor
        else -> Color.Unspecified
    }
}


var opLabelArray: Array<String>? = null
fun opLabel(context: Context, code: Int, fallback: String = "$code"): String {
    if (opLabelArray == null) {
        opLabelArray = context.resources.getStringArray(R.array.module_ops2_app_ops_labels)
    }
    return runCatching { opLabelArray!![code] }.getOrElse { fallback }
}

var opSummaryArray: Array<String>? = null
fun opSummary(context: Context, code: Int, fallback: String = "$code"): String {
    if (opSummaryArray == null) {
        opSummaryArray = context.resources.getStringArray(R.array.module_ops2_app_ops_summaries)
    }
    return runCatching { opSummaryArray!![code] }.getOrElse { fallback }
}