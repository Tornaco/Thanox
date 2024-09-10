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

package now.fortuitous.thanos.process.v2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.common.LifeCycleAwareViewModel
import github.tornaco.android.thanos.core.pm.Pkg
import github.tornaco.android.thanos.core.util.ClipboardUtils
import github.tornaco.android.thanos.support.withThanos
import github.tornaco.android.thanos.util.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class RunningAppDetailState(
    val cpuState: CpuUsageStatsState,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class RunningAppDetailViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    LifeCycleAwareViewModel() {
    private val _state =
        MutableStateFlow(
            RunningAppDetailState(CpuUsageStatsState(emptyMap()))
        )
    val state = _state.asStateFlow()

    val appStateChanged get() = _appStateChanged
    private var _appStateChanged: Boolean = false

    fun copyProcessName(state: RunningProcessState) {
        ClipboardUtils.copyToClipboard(context, "Process name", state.process.processName)
        ToastUtils.copiedToClipboard(context)
    }

    fun stopProcess(state: RunningProcessState): Boolean {
        return context.withThanos {
            (activityManager.killProcessByName(state.process.processName) > 0).also {
                if (it) _appStateChanged = true
            }
        } ?: false
    }

    fun copyServiceName(service: RunningService) {
        ClipboardUtils.copyToClipboard(
            context,
            "Service name",
            service.running.service.flattenToString()
        )
        ToastUtils.copiedToClipboard(context)
    }

    fun stopService(service: RunningService): Boolean {
        return context.withThanos {
            activityManager.stopService(Intent().apply {
                component = service.running.service
                putExtra("uid", service.running.uid)
            }).also {
                if (it) _appStateChanged = true
            }
        } ?: false
    }

    fun forceStop(runningAppState: RunningAppState) {
        context.withThanos {
            activityManager.forceStopPackage(
                Pkg.fromAppInfo(runningAppState.appInfo),
                "RunningApp detail UI forceStop"
            )
        }
    }

    suspend fun startQueryCpuUsage(runningAppState: RunningAppState) {
        context.withThanos {
            XLog.w("startQueryCpuUsage...$runningAppState")
            val pids = runningAppState.processState.map { it.process.pid.toLong() }.toLongArray()
            while (true) {
                if (isResumed) {
                    kotlin.runCatching {
                        val queryProcessCpuUsageStats =
                            activityManager.queryProcessCpuUsageStats(pids, true)
                        _state.value =
                            _state.value.copy(cpuState = CpuUsageStatsState(statsMap = queryProcessCpuUsageStats.associateBy { it.pid }))
                    }.onFailure {
                        XLog.e("startQueryCpuUsage error", it)
                    }
                }
                delay(1000)
            }
        }
    }
}