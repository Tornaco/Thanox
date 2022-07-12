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

package github.tornaco.thanos.android.module.profile.example

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.ProfileManager
import github.tornaco.android.thanos.core.profile.RuleAddCallback
import github.tornaco.android.thanos.core.profile.RuleInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.AssetUtils
import javax.inject.Inject

data class ExampleState(val examples: List<Example> = emptyList())

data class Example(val ruleInfo: RuleInfo)

sealed interface Event {
    data class ImportFailRuleWithSameNameAlreadyExists(val name: String) : Event
    data class ImportFail(val error: String) : Event
    data class ImportSuccess(val name: String) : Event
}

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProfileExampleViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(ExampleState())
    val state = _state.asStateFlow()

    val events = MutableSharedFlow<Event>()

    private val thanox by lazy { ThanosManager.from(context) }

    fun loadAssetExamples() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val ruleFiles: Array<String> =
                        context.assets.list("prebuilt_profile") ?: return@runCatching

                    val rules = mutableListOf<Example>()
                    for (file in ruleFiles) {
                        var type = ProfileManager.RULE_FORMAT_JSON
                        if (file.endsWith("yml")) {
                            type = ProfileManager.RULE_FORMAT_YAML
                        }
                        val ruleString =
                            AssetUtils.readFileToString(context, "prebuilt_profile/$file")

                        thanox.profileManager.parseRuleOrNull(ruleString, type)?.let {
                            rules.add(Example(it))
                        }
                    }

                    _state.value = _state.value.copy(examples = rules)
                }.onFailure {
                    XLog.e("loadExamples error")
                }
            }
        }
    }

    fun import(example: Example) {
        val scope = viewModelScope
        viewModelScope.launch {
            val ruleByName = thanox.profileManager.getRuleByName(example.ruleInfo.name)
            if (ruleByName != null) {
                events.emit(Event.ImportFailRuleWithSameNameAlreadyExists(example.ruleInfo.name))
            } else {
                thanox.profileManager.addRuleIfNotExists(
                    example.ruleInfo.ruleString,
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
                                events.emit(Event.ImportSuccess(example.ruleInfo.name))
                            }
                        }
                    },
                    example.ruleInfo.format
                )
            }
        }
    }
}