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

package github.tornaco.thanos.android.ops2.byop

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.ops.OpsManager.Companion.APP_OP_DEPRECATED_1
import github.tornaco.thanos.android.ops2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class OpItem(
    val code: Int,
    val label: String,
    val description: String,
    val iconRes: Int,
    val appsCount: Int
)

data class OpsState(
    val isLoading: Boolean,
    val opsItems: List<OpItem>
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class OpsViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(
            OpsState(
                isLoading = true,
                opsItems = emptyList()
            )
        )
    val state = _state.asStateFlow()

    private val opsManager by lazy { ThanosManager.from(context).opsManager }

    fun refresh() {
        viewModelScope.launch {
            val array = context.resources.obtainTypedArray(R.array.module_ops2_op_icon)
            val opItems = withContext(Dispatchers.IO) {
                (0 until opsManager.opNum).filter { it != APP_OP_DEPRECATED_1 }
                    .filter { opsManager.opToSwitch(it) == it }
                    .map { code ->
                        OpItem(
                            code = code,
                            appsCount = 2,
                            label = opLabel(context, code, opsManager.opToName(code)),
                            description = opSummary(
                                context,
                                code,
                                opsManager.opToName(code)
                            ),
                            iconRes = kotlin.runCatching {
                                array.getResourceId(
                                    code,
                                    R.drawable.ic_remix_settings_2_fill /* fallback */
                                )
                            }.getOrElse {
                                R.drawable.ic_remix_settings_2_fill
                            }.also {
                                XLog.d("Res: $it")
                            }
                        )
                    }
            }
            array.recycle()
            _state.value = _state.value.copy(opsItems = opItems)
        }
    }
}