package github.tornaco.android.thanos.module.compose.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.theme.ThemeActivity

abstract class ComposeThemeActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            val darkTheme =
                if (appTheme.shouldApplyDynamic) isSystemInDarkTheme() else !appTheme.isLight
            ThanoxTheme(darkTheme) {
                // Update the system bars to be translucent
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = !darkTheme
                    )
                }
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}