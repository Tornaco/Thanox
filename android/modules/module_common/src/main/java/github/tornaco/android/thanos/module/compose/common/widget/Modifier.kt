package github.tornaco.android.thanos.module.compose.common.widget

import androidx.compose.ui.Modifier

fun Modifier.thenIf(modifier: Modifier, boolean: Boolean): Modifier {
    return if (boolean) return this.then(modifier) else this
}