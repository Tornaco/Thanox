package github.tornaco.android.thanos.module.compose.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.theme.ThemeActivity

abstract class ComposeThemeActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ThanoxTheme() {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}