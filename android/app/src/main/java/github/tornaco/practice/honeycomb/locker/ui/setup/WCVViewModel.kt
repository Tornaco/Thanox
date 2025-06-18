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

package github.tornaco.practice.honeycomb.locker.ui.setup


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class WCVState(
    val components: List<ComponentName> = emptyList()
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class WCVViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(WCVState())
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun load() {
        viewModelScope.launch {
            _state.value =
                _state.value.copy(components = thanox.activityStackSupervisor.appLockWhiteListComponents)
        }
    }

    fun add(componentName: ComponentName) {
        thanox.activityStackSupervisor.addAppLockWhiteListComponents(listOf(componentName))
        load()
    }

    fun remove(componentName: ComponentName) {
        thanox.activityStackSupervisor.removeAppLockWhiteListComponents(listOf(componentName))
        load()
    }
}