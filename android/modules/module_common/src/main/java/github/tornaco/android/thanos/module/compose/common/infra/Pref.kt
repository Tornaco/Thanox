package github.tornaco.android.thanos.module.compose.common.infra

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import github.tornaco.android.thanos.module.compose.common.theme.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")

private val UI_THEME_DARK_MODE_CONFIG =
    intPreferencesKey("UI_THEME_DARK_MODE_CONFIG")
private val UI_THEME_DYNAMIC_COLOR =
    booleanPreferencesKey("UI_THEME_DYNAMIC_COLOR")

class Pref(val context: Context) {
    val uiThemeDarkModeConfig: Flow<ThemeSettings.DarkThemeConfig> =
        context.dataStore.data.map { preferences ->
            preferences[UI_THEME_DARK_MODE_CONFIG]?.let { config ->
                ThemeSettings.DarkThemeConfig.entries.firstOrNull { it.ordinal == config }
                    ?: ThemeSettings.DarkThemeConfig.FOLLOW_SYSTEM
            } ?: ThemeSettings.DarkThemeConfig.FOLLOW_SYSTEM
        }

    val uiThemeDynamicColor: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[UI_THEME_DYNAMIC_COLOR] != false
    }

    suspend fun setUiThemeDynamicColor(enable: Boolean) {
        context.dataStore.edit {
            it[UI_THEME_DYNAMIC_COLOR] = enable
        }
    }

    suspend fun serUiThemeDarkModeConfig(mode: ThemeSettings.DarkThemeConfig) {
        context.dataStore.edit {
            it[UI_THEME_DARK_MODE_CONFIG] = mode.ordinal
        }
    }
}
