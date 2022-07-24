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

package github.tornaco.thanos.android.module.profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.LogSink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ConsoleState(
    val codeText: String,
    val consoleLogOutput: String,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ConsoleViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(
            ConsoleState(
                codeText = """
                    ui.showShortToast("Running apps: " + thanos.getActivityManager().getRunningAppsCount());
                """.trimIndent(),
                consoleLogOutput = ""
            )
        )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    private val logSink = object : LogSink() {
        override fun log(message: String?) {
            super.log(message)
            _state.value = _state.value.copy(consoleLogOutput = message ?: "null")
        }
    }

    init {
        thanox.profileManager.addConsoleLogSink(logSink)
    }

    override fun onCleared() {
        super.onCleared()
        thanox.profileManager.removeConsoleLogSink(logSink)
    }

    fun input(input: String) {
        _state.value = _state.value.copy(codeText = input)
    }

    fun execute() {
        _state.value = _state.value.copy(consoleLogOutput = "")
        thanox.profileManager.executeAction(state.value.codeText)
    }
}