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
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.core.profile.RuleAddCallback
import github.tornaco.android.thanos.core.profile.RuleInfo
import github.tornaco.thanos.android.module.profile.repo.OnlineProfile
import github.tornaco.thanos.android.module.profile.repo.OnlineProfileRepoImpl
import github.tornaco.thanos.android.module.profile.repo.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.JsonFormatter
import javax.inject.Inject

data class OnlineProfileState(
    val isLoading: Boolean = true,
    val files: List<OnlineProfileItem> = emptyList()
)

data class OnlineProfileItem(
    val onlineProfile: OnlineProfile,
    val ruleInfo: RuleInfo,
    val rawProfileJson: String
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
    private val gson by lazy {
        GsonBuilder().registerTypeAdapter(Profile::class.java, ProfileJsonSerializer()).create()
    }

    fun loadOnlineProfiles() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val files = repo.getProfiles()
                    val rules = files.mapNotNull { op ->
                        kotlin.runCatching {
                            val jsonString =
                                JsonFormatter.format(gson.toJson(listOf(op.profile)))
                            thanox.profileManager.parseRuleOrNull(
                                jsonString,
                                ProfileManager.RULE_FORMAT_JSON
                            )?.let { ruleInfo ->
                                OnlineProfileItem(op, ruleInfo, jsonString)
                            }
                        }.getOrElse {
                            XLog.e("Parse one profile error", it)
                            null
                        }
                    }
                    _state.value = _state.value.copy(files = rules, isLoading = false)
                }.onFailure {
                    XLog.e("loadOnlineProfiles error", it)
                }
            }
        }
    }

    fun import(profile: OnlineProfileItem) {
        val scope = viewModelScope
        viewModelScope.launch {
            val ruleByName = thanox.profileManager.getRuleByName(profile.ruleInfo.name)
            if (ruleByName != null) {
                events.emit(Event.ImportFailRuleWithSameNameAlreadyExists(profile.ruleInfo.name))
            } else {
                thanox.profileManager.addRuleIfNotExists(
                    profile.rawProfileJson,
                    object : RuleAddCallback() {
                        override fun onRuleAddFail(errorCode: Int, errorMessage: String?) {
                            super.onRuleAddFail(errorCode, errorMessage)
                            scope.launch {
                                events.emit(Event.ImportFail(errorMessage ?: "$errorCode"))
                            }
                        }

                        override fun onRuleAddSuccess() {
                            super.onRuleAddSuccess()
                            scope.launch {
                                events.emit(Event.ImportSuccess(profile.ruleInfo.name))
                            }
                        }
                    },
                    ProfileManager.RULE_FORMAT_JSON
                )
            }
        }
    }

    fun refresh() {
        loadOnlineProfiles()
    }
}