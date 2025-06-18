package github.tornaco.android.thanos.module.compose.common

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme

abstract class ComposeThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThanoxTheme() {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}