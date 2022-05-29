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
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.elvishew.xlog.XLog
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.thanos.android.module.profile.engine.work.PeriodicWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.*
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toJavaDuration

enum class Type {
    Periodic,
    Unknown
}

data class DateTimeState(
    val workStates: List<WorkState> = listOf()
)

data class WorkState(
    val id: UUID,
    val tag: String,
    val type: Type,
    val value: Long
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DateTimeEngineViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    private val workManager get() = WorkManager.getInstance(context)

    private val _state =
        MutableStateFlow(
            DateTimeState()
        )
    val state = _state.asStateFlow()


    fun schedulePeriodicWork(tag: String, duration: kotlin.time.Duration) {
        XLog.w("schedulePeriodicWork: $tag $duration")
        workManager.enqueue(
            PeriodicWorkRequest.Builder(PeriodicWork::class.java, duration.toJavaDuration())
                .setInitialDelay(Duration.ZERO)
                // Use duration as tag.
                .addTag("${Type.Periodic.name}-$tag-${duration.toLong(DurationUnit.MILLISECONDS)}")
                .setInputData(Data.Builder().putString("tag", tag).build())
                .build()
        )

        loadPendingWorks()
    }

    fun loadPendingWorks() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    val workStates = workManager.getWorkInfos(
                        WorkQuery.Builder.fromStates(
                            listOf(
                                WorkInfo.State.ENQUEUED,
                                WorkInfo.State.RUNNING
                            )
                        ).build()
                    ).get().map { workInfo ->
                        val payload = workInfo.tags.first()
                        val stateSplit = payload.split("-")
                        if (stateSplit.size == 3) {
                            kotlin.runCatching {
                                WorkState(
                                    id = workInfo.id,
                                    tag = stateSplit[1],
                                    type = Type.valueOf(stateSplit[0]),
                                    value = stateSplit[2].toLong()
                                )
                            }.getOrElse {
                                XLog.e("map to WorkState error", it)
                                WorkState(
                                    id = workInfo.id,
                                    tag = stateSplit[1],
                                    type = Type.Unknown,
                                    value = 0L
                                )
                            }
                        } else {
                            WorkState(
                                id = workInfo.id,
                                tag = payload,
                                type = Type.Unknown,
                                value = 0L
                            )
                        }
                    }
                    _state.value = _state.value.copy(workStates = workStates)
                }.onFailure {
                    XLog.e("loadPendingWorks error", it)
                }
            }
        }
    }

    fun deleteById(id: UUID) {
        XLog.w("deleteById: $id")
        workManager.cancelWorkById(id)
        loadPendingWorks()
    }
}