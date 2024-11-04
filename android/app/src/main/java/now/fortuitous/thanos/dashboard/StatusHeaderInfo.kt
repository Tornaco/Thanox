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

package now.fortuitous.thanos.dashboard

import github.tornaco.android.thanos.core.pm.AppInfo

data class StatusHeaderInfo(
    val runningAppsCount: Int,
    val memory: MemUsage,
    val swap: MemUsage,
    val cpu: CpuUsage
)

data class MemUsage(
    val memType: MemType = MemType.MEMORY,
    val memTotalSizeString: String = "10240M",
    // 0-100
    val memUsagePercent: Int = 88,
    val memUsageSizeString: String = "1024M",
    val memAvailableSizeString: String = "1024M",
    val isEnabled: Boolean = true
)

data class CpuUsage(
    // 0-100
    val totalPercent: Int = 1,
    val topNPkgs: List<AppCpuUsage> = emptyList()
)

data class AppCpuUsage(
    val appInfo: AppInfo,
    val percent: String,
)

enum class MemType {
    MEMORY,
    SWAP
}

val defaultStatusHeaderInfo = StatusHeaderInfo(
    999,
    MemUsage(MemType.MEMORY),
    MemUsage(MemType.SWAP),
    CpuUsage(88)
)