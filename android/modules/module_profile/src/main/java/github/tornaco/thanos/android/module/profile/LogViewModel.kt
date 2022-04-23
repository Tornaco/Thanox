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
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject

data class LogState(
    val isLoading: Boolean = false,
    val horizontalScrollEnabled: Boolean = false,
    val logs: List<String> = emptyList()
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class LogViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(
            LogState(
                isLoading = true
            )
        )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            delay(500)
            kotlin.runCatching {
                val logPath = thanox.profileManager.logPath
                val fd = thanox.profileManager.logFD
                val br = BufferedReader(InputStreamReader(FileInputStream(fd?.fileDescriptor)))
                val lines = br.readLines()
                _state.value =
                    _state.value.copy(
                        isLoading = false,
                        logs = listOf(
                            ">>>>>>>> START <<<<<<<<",
                            logPath ?: "???",
                            System.lineSeparator()
                        ) + lines + listOf(">>>>>>>> END <<<<<<<<")
                    )
            }.onFailure {
                _state.value =
                    _state.value.copy(isLoading = false, logs = listOf(Log.getStackTraceString(it)))
            }

        }
    }

    fun clearLogs() {
        thanox.profileManager.clearLogs()
        refresh()
    }

    fun setHorizontalScrollEnabled(horizontalScrollEnabled: Boolean) {
        _state.value =
            _state.value.copy(horizontalScrollEnabled = horizontalScrollEnabled)
    }
}