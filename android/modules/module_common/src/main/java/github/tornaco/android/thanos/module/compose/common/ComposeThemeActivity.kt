package github.tornaco.android.thanos.module.compose.common

import android.content.Context
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.trace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.module.compose.common.infra.ContextViewModel
import github.tornaco.android.thanos.module.compose.common.infra.Pref
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.module.compose.common.theme.ThemeSettings
import github.tornaco.android.thanos.module.compose.common.theme.ThemeSettings.DarkThemeConfig
import github.tornaco.android.thanos.module.compose.common.theme.ThemeState
import github.tornaco.android.thanos.module.compose.common.theme.darkScrim
import github.tornaco.android.thanos.module.compose.common.theme.isSystemInDarkTheme
import github.tornaco.android.thanos.module.compose.common.theme.lightScrim
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ThemeActivityUiState(
    val themeSettings: ThemeSettings? = null
) {
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean) =
        when (themeSettings?.darkTheme) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
            null -> false
        }
}

@HiltViewModel
class ThemeActivityVM @Inject constructor(@ApplicationContext context: Context) :
    ContextViewModel<ThemeActivityUiState>(context, initState = { ThemeActivityUiState() }) {
    private val pref by lazy { Pref(context = context) }

    init {
        viewModelScope.launch {
            combine(
                pref.uiThemeDynamicColor,
                pref.uiThemeDarkModeConfig
            ) { dynamicColor, darkMode ->
                ThemeSettings(darkMode, dynamicColor)
            }.collectLatest { value ->
                updateState { copy(themeSettings = value) }
            }
        }
    }
}

abstract class ComposeThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // We keep this as a mutable state, so that we can track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var themeState by mutableStateOf(
            ThemeState(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                disableDynamicTheming = false
            ),
        )

        val viewModel by viewModels<ThemeActivityVM>()

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    viewModel.state.filter { it.themeSettings != null },
                ) { systemDark, uiState ->
                    ThemeState(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        disableDynamicTheming = uiState.themeSettings.disableDynamicTheming,
                    )
                }
                    .onEach { themeState = it }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        trace("thanoxEdgeToEdge") {
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = android.graphics.Color.TRANSPARENT,
                                    darkScrim = android.graphics.Color.TRANSPARENT,
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = lightScrim,
                                    darkScrim = darkScrim,
                                ) { darkTheme },
                            )
                        }
                    }
            }
        }

        setContent {
            ThanoxTheme(
                darkTheme = themeState.darkTheme,
                disableDynamicTheming = themeState.disableDynamicTheming,
            ) {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}