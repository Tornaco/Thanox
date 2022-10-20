package github.tornaco.android.thanos.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.composethemeadapter3.Mdc3Theme
import github.tornaco.android.thanos.main.NavActivity
import github.tornaco.android.thanos.pref.AppPreference
import github.tornaco.android.thanos.theme.ThemeActivity
import github.tornaco.android.thanos.util.ActivityUtils

class OnBoardingActivity : ThemeActivity() {

    object Starter {
        fun start(context: Context?) {
            ActivityUtils.startActivity(context, OnBoardingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = androidx.compose.material.MaterialTheme.colors.isLight
                val colorSurface = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = colorSurface,
                        darkIcons = useDarkIcons
                    )
                }

                val context = LocalContext.current

                OnBoardingScreen {
                    AppPreference.setHasOnBoarding(context, true)
                    NavActivity.Starter.start(thisActivity())
                    finish()
                }
            }
        }
    }
}
