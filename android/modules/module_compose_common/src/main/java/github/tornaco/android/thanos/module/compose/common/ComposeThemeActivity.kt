package github.tornaco.android.thanos.module.compose.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.google.android.material.composethemeadapter3.Mdc3Theme
import github.tornaco.android.thanos.theme.ThemeActivity

abstract class ComposeThemeActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}