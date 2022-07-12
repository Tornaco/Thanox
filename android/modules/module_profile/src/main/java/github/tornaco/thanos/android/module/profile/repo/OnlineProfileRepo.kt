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

package github.tornaco.thanos.android.module.profile.repo

import android.content.Context
import com.elvishew.xlog.XLog
import com.google.android.vending.licensing.util.Base64
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface OnlineProfileRepo {
    suspend fun getProfiles(): List<OnlineProfile>
}

class OnlineProfileRepoImpl @Inject constructor(@ApplicationContext val context: Context) :
    OnlineProfileRepo {
    private val service by lazy {
        OnlineProfileService.Factory.create(context)
    }
    private val gson by lazy {
        Gson()
    }

    override suspend fun getProfiles(): List<OnlineProfile> {
        return service.getProfileFiles().filter {
            it.type == "file"
        }.mapNotNull {
            kotlin.runCatching {
                service.getProfileFileContent(it.name).let {
                    val decodedContent = Base64.decode(it.content)
                    gson.fromJson(String(decodedContent), OnlineProfile::class.java)
                }
            }.getOrElse {
                XLog.e("getProfileFileContent error", it)
                null
            }
        }
    }
}