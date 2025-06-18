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

package github.tornaco.practice.honeycomb.locker.ui.verify

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.module.compose.common.infra.LifeCycleAwareViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PatternState(
    val step: Step = Step.First,
    val pattern: String = ""
)

sealed interface Step {
    data object First : Step
    data object Second : Step
    data object Done : Step
}

sealed interface PatternSettingsEvent {
    data object PatternMisMatch : PatternSettingsEvent
    data object Done : PatternSettingsEvent
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class PatternSettingsVM @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state = MutableStateFlow(
        PatternState()
    )
    val state = _state.asStateFlow()

    val events = MutableSharedFlow<PatternSettingsEvent>()

    private val thanox by lazy { ThanosManager.from(context) }

    fun drawPattern(pattern: String) {
        when (state.value.step) {
            Step.First -> {
                _state.update {
                    it.copy(pattern = pattern, step = Step.Second)
                }
            }

            Step.Second -> {
                if (pattern != state.value.pattern) {
                    _state.update {
                        it.copy(pattern = "", step = Step.First)
                    }
                    viewModelScope.launch { events.emit(PatternSettingsEvent.PatternMisMatch) }
                } else {
                    _state.update {
                        it.copy(step = Step.Done)
                    }
                    thanox.activityStackSupervisor.lockPattern = pattern
                    viewModelScope.launch { events.emit(PatternSettingsEvent.Done) }
                }
            }

            else -> {

            }
        }

    }
}