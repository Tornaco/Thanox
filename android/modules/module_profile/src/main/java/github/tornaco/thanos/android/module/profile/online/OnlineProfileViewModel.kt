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

package github.tornaco.thanos.android.module.profile.online

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.thanos.android.module.profile.repo.OnlineProfile
import github.tornaco.thanos.android.module.profile.repo.OnlineProfileRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class OnlineProfileState(
    val isLoading: Boolean = true,
    val files: List<OnlineProfile> = emptyList()
)

sealed interface Event {
    data class ImportFailRuleWithSameNameAlreadyExists(val name: String) : Event
    data class ImportFail(val error: String) : Event
    data class ImportSuccess(val name: String) : Event
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class OnlineProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: OnlineProfileRepoImpl
) :
    ViewModel() {
    private val _state =
        MutableStateFlow(OnlineProfileState())
    val state = _state.asStateFlow()

    val events = MutableSharedFlow<Event>()

    private val thanox by lazy { ThanosManager.from(context) }

    fun loadOnlineProfiles() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val files = repo.getProfiles()
                    _state.value = _state.value.copy(files = files, isLoading = false)
                }.onFailure {
                    XLog.e("loadOnlineProfiles error", it)
                }
            }
        }
    }

    fun import(example: OnlineProfile) {

    }

    fun refresh() {
        loadOnlineProfiles()
    }
}