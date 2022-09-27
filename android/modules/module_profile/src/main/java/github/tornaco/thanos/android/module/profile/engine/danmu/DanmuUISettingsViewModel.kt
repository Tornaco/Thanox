/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.thanos.android.module.profile.engine.danmu

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.DanmuUISettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UISettingsState(val settings: UISettings)

data class UISettings(
    val alpha: Float = 0f,
    val backgroundColor: Int = 0,
    val textColor: Int = 0,
    val textSizeSp: Int = 0,
    val duration: Long = 0,
)

fun DanmuUISettings.toUISettings() = UISettings(
    alpha = alpha,
    backgroundColor = backgroundColor,
    textColor = textColor,
    textSizeSp = textSizeSp,
    duration = duration,
)

fun UISettings.toDanmuUISettings() = DanmuUISettings(
    alpha, backgroundColor, textColor, textSizeSp, duration
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DanmuUISettingsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val profile by lazy {
        ThanosManager.from(context).profileManager
    }

    private var previewJob: Job? = null

    private val _state = MutableStateFlow(UISettingsState(settings = UISettings(0f, 0, 0, 0)))
    val state = _state.asStateFlow()

    fun loadUISettings() {
        profile.danmuUISettings?.let {
            _state.value = UISettingsState(it.toUISettings())
        }
    }

    fun updateUISettings(settings: UISettings) {
        previewJob?.cancel()
        profile.danmuUISettings = settings.toDanmuUISettings()
        _state.value = UISettingsState(settings)
        previewJob = viewModelScope.launch {
            delay(1000)
            profile.executeAction("ui.showDanmu(null, \"Hello world!\", true)")
        }
    }
}