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

package github.tornaco.thanos.android.module.profile.engine.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.ProfileManager

class PeriodicWork(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val thanox get() = ThanosManager.from(context)

    override fun doWork(): Result {
        thanox.profileManager.publishStringFact(
            ProfileManager.FACT_SOURCE_DATE_TIME,
            inputData.getString("tag"),
            0L
        )
        return Result.success()
    }
}