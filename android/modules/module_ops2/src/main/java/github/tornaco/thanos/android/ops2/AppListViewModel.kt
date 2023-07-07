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
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.ops.PermInfo
import github.tornaco.android.thanos.core.ops.PermState
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.PREBUILT_PACKAGE_SET_ID_3RD
import github.tornaco.android.thanos.core.pm.Pkg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AppItem(
    val appInfo: AppInfo,
    val permInfo: PermInfo,
)

data class AppListState(
    val isLoading: Boolean,
    val appList: List<AppItem>,
    val code: Int = Int.MIN_VALUE,
    val opLabel: String
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AppListViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val _state =
        MutableStateFlow(
            AppListState(
                isLoading = true,
                appList = emptyList(),
                opLabel = ""
            )
        )
    val state = _state.asStateFlow()

    private val thanos by lazy { ThanosManager.from(context) }
    private val opsManager by lazy { thanos.opsManager }

    fun init(code: Int) {
        _state.value = _state.value.copy(
            code = code,
            opLabel = opLabel(context, code, opsManager.opToName(code))
        )
    }

    fun refresh() {
        if (_state.value.code < 0) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            withContext(Dispatchers.IO) {
                val all =
                    thanos.pkgManager.getInstalledPkgsByPackageSetId(PREBUILT_PACKAGE_SET_ID_3RD)
                val appItems = all.mapNotNull { appInfo ->
                    val permInfo = opsManager.getPackagePermInfo(
                        state.value.code,
                        Pkg.fromAppInfo(appInfo)
                    )
                    permInfo?.let { AppItem(appInfo, it) }
                }
                _state.value = _state.value.copy(appList = appItems, isLoading = false)
            }
        }
    }

    fun setMode(appInfo: AppInfo, mode: PermState) {
        opsManager.setMode(state.value.code, Pkg.fromAppInfo(appInfo), mode.name)
        refresh()
    }
}