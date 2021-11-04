package github.tornaco.android.thanos.onboarding

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
            AppCompatTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                val colorSurface = MaterialTheme.colors.surface

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = colorSurface,
                        darkIcons = useDarkIcons
                    )
                }

                val context = LocalContext.current

                OnBoardingScreen {
                    AppPreference.setHasOnBoarding(context, true)
                    NavActivity.start(thisActivity())
                    finish()
                }
            }
        }
    }
}
