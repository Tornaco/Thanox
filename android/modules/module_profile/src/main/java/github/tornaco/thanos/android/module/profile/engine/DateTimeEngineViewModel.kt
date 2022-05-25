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

package github.tornaco.thanos.android.module.profile.engine

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.thanos.android.module.profile.engine.work.PeriodicWork
import java.time.Duration
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DateTimeEngineViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val workManager get() = WorkManager.getInstance(context)

    fun schedulePeriodicWork(fact: String, duration: Duration) {
        workManager.enqueueUniquePeriodicWork(
            "Profile-PeriodicWork_$fact",
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequest.Builder(PeriodicWork::class.java, duration)
                .setInputData(Data.Builder().putString("fact", fact).build())
                .build()
        )
    }
}