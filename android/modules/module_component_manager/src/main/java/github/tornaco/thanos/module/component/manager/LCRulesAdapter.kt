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

package github.tornaco.thanos.module.component.manager

import android.content.ComponentName
import android.content.Context
import androidx.annotation.DrawableRes
import com.absinthe.rulesbundle.*
import com.elvishew.xlog.XLog
import kotlinx.coroutines.runBlocking

fun initRules(context: Context) = runCatching {
    LCRules.init(context)
}.onFailure { XLog.e(it) }

fun getActivityRule(name: ComponentName) =
    getRule(name = name, type = ACTIVITY)

fun getServiceRule(name: ComponentName) =
    getRule(name = name, type = SERVICE)

fun getReceiverRule(name: ComponentName) =
    getRule(name = name, type = RECEIVER)

fun getProviderRule(name: ComponentName) =
    getRule(name = name, type = PROVIDER)

private fun getRule(name: ComponentName, type: Int): ComponentRule? =
    runBlocking {
        val rule: Rule? = LCRules.getRule(libName = name.className, type = type, useRegex = false)
        val wrap = rule?.let {
            ComponentRule(
                it.label,
                it.iconRes,
                it.descriptionUrl,
                it.regexName,
                it.isSimpleColorIcon)
        }
        wrap
    }

data class ComponentRule(
    val label: String,
    @DrawableRes val iconRes: Int,
    val descriptionUrl: String?,
    val regexName: String?,
    val isSimpleColorIcon: Boolean,
)