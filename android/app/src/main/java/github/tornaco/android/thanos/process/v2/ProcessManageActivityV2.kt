package github.tornaco.android.thanos.process.v2

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.composethemeadapter3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint
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
            Mdc3Theme {
                // Update the system bars to be translucent
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                val color =
                    androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                SideEffect {
                    systemUiController.setSystemBarsColor(color, darkIcons = useDarkIcons)
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