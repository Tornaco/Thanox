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

package github.tornaco.android.thanos.core.net

import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.pm.AppInfo

data class UidStats(
    val uid: Int = 0,
    @JvmField var lastRxBytes: Long = 0,
    @JvmField var lastTxBytes: Long = 0,
    var baseRxBytes: Long = 0,
    var baseTxBytes: Long = 0,
)

class TrafficStatsState(val context: Context) {
    private val uidToStatMap: MutableMap<Int, UidStats> = mutableMapOf()

    fun update(thanox: ThanosManager) {
        thanox.activityManager.runningAppPackages.forEach { pkg ->
            val appInfo: AppInfo? = thanox.pkgManager.getAppInfoForUser(pkg.pkgName, pkg.userId)
            appInfo?.let {
                val uid = appInfo.uid
                updateUidStat(uid, thanox)
            }
        }
    }

    fun update(uid: Int, thanox: ThanosManager) {
        updateUidStat(uid, thanox)
    }

    fun getUidStats(uid: Int) = uidToStatMap[uid]

    private fun updateUidStat(uid: Int, thanox: ThanosManager) {
        val trafficStats = thanox.networkManager.getUidTrafficStats(uid)
        val rx = trafficStats.rxBytes
        val tx = trafficStats.txBytes

        var stats = uidToStatMap[uid]

        if (stats == null) {
            stats = UidStats(uid = uid)
        }
        if (stats.baseTxBytes > 0) stats.lastRxBytes = rx - stats.baseRxBytes
        if (stats.baseTxBytes > 0) stats.lastTxBytes = tx - stats.baseTxBytes

        stats.baseTxBytes = tx
        stats.baseRxBytes = rx

        uidToStatMap[uid] = stats
    }
}