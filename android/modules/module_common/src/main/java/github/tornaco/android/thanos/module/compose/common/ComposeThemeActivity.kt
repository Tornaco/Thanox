package github.tornaco.android.thanos.module.compose.common

import android.content.Context
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.trace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import github.tornaco.android.thanos.module.compose.common.infra.Pref
import github.tornaco.android.thanos.module.compose.common.infra.WithStateImpl
import github.tornaco.android.thanos.module.compose.common.theme.ThanoxTheme
import github.tornaco.android.thanos.module.compose.common.theme.ThemeSettings
import github.tornaco.android.thanos.module.compose.common.theme.ThemeSettings.DarkThemeConfig
import github.tornaco.android.thanos.module.compose.common.theme.ThemeState
import github.tornaco.android.thanos.module.compose.common.theme.darkScrim
import github.tornaco.android.thanos.module.compose.common.theme.isSystemInDarkTheme
import github.tornaco.android.thanos.module.compose.common.theme.lightScrim
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

object ThemeActivityVM {
    private val stateImpl: WithStateImpl<ThemeActivityUiState> =
        WithStateImpl { ThemeActivityUiState() }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val state get() = stateImpl.state

    fun init(context: Context) {
        val pref by lazy { Pref(context = context) }
        scope.launch {
            combine(
                pref.uiThemeDynamicColor,
                pref.uiThemeDarkModeConfig
            ) { dynamicColor, darkMode ->
                ThemeSettings(darkMode, !dynamicColor)
            }.collectLatest { value ->
                stateImpl.updateState { copy(themeSettings = value) }
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

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    ThemeActivityVM.state.filter { it.themeSettings != null },
                ) { systemDark, uiState ->
                    uiState.themeSettings?.let {
                        ThemeState(
                            darkTheme = uiState.shouldUseDarkTheme(systemDark),
                            disableDynamicTheming = it.disableDynamicTheming,
                        )
                    }
                }.filterNotNull().onEach { themeState = it }
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