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

package github.tornaco.thanos.android.ops2

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class OpItem(
    val code: Int,
    val mode: Int,
    val label: String,
    val description: String,
    val iconRes: Int
)

data class OpsState(
    val isLoading: Boolean,
    val opsItems: List<OpItem>
)

fun Int.isAllowed() = this == AppOpsManager.MODE_ALLOWED

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
            val opItems = withContext(Dispatchers.IO) {
                (0..opsManager.opNum).map { code ->
                    OpItem(
                        code = code,
                        mode = 1,
                        label = "code-$code",
                        description = "desc",
                        iconRes = R.drawable.module_common_ic_settings_fill
                    )
                }
            }
            _state.value = _state.value.copy(opsItems = opItems)
        }
    }


    fun setMode(it: OpItem, checked: Boolean) {
        val mode = if (checked) {
            AppOpsManager.MODE_ALLOWED
        } else {
            AppOpsManager.MODE_IGNORED
        }

    }
}