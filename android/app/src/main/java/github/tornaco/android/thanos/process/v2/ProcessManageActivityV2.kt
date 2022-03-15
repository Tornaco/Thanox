package github.tornaco.android.thanos.process.v2

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.process.ProcessManageActivity
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

@AndroidEntryPoint
class ProcessManageActivityV2 : ThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, ProcessManageActivityV2::class.java)
        }
    }

    override fun isF(): Boolean {
        return true
    }

    override fun isADVF(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ThanoxTheme {
                // Update the system bars to be translucent
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                ProvideWindowInsets {
                    Surface {
                        ProcessManageScreen(
                            onBackPressed = {
                                finish()
                            },
                            toLegacyUi = {
                                ProcessManageActivity.start(thisActivity())
                            })
                    }
                }
            }
        }
    }
}