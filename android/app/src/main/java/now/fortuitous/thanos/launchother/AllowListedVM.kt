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

package now.fortuitous.thanos.launchother

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.core.pm.Pkg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AllowListedState(
    val appInfo: AppInfo = AppInfo.dummy(),
    val apps: List<AppInfo> = emptyList()
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AllowListedVM @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {

    private val _state = MutableStateFlow(
        AllowListedState()
    )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    fun init(appInfo: AppInfo) {
        _state.update { it.copy(appInfo = appInfo) }
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val apps = thanox.activityStackSupervisor.getLaunchOtherAppAllowListOrNull(
                    Pkg.fromAppInfo(
                        state.value.appInfo
                    )
                )?.mapNotNull {
                    thanox.pkgManager.getAppInfo(it)
                } ?: emptyList()
                XLog.d("apps: $apps")
                _state.update {
                    it.copy(apps = apps)
                }
            }
        }
    }

    fun addApp(appInfo: List<AppInfo>) {
        appInfo.forEach {
            thanox.activityStackSupervisor.addPkgToLaunchOtherAppAllowList(
                Pkg.fromAppInfo(state.value.appInfo),
                Pkg.fromAppInfo(it)
            )
        }
        loadApps()
    }

    fun removeApp(appInfo: AppInfo) {
        thanox.activityStackSupervisor.removePkgFromLaunchOtherAppAllowList(
            Pkg.fromAppInfo(state.value.appInfo),
            Pkg.fromAppInfo(appInfo)
        )
        loadApps()
    }
}