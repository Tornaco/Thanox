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
import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.absinthe.rulesbundle.ACTIVITY
import com.absinthe.rulesbundle.LCLocale
import com.absinthe.rulesbundle.LCRemoteRepo
import com.absinthe.rulesbundle.LCRules
import com.absinthe.rulesbundle.PROVIDER
import com.absinthe.rulesbundle.RECEIVER
import com.absinthe.rulesbundle.Rule
import com.absinthe.rulesbundle.SERVICE
import com.elvishew.xlog.XLog
import github.tornaco.android.thanos.core.app.AppGlobals
import github.tornaco.thanos.module.component.manager.redesign.toCategory
import kotlinx.coroutines.runBlocking
import java.util.Locale

fun initRules(context: Context) = runCatching {
    LCRules.init(context)

    val locale = context.resources.configuration.locales[0]
    val isSystemLanguageChinese = locale.language == Locale.CHINESE.language
    XLog.w("initRules: $locale $isSystemLanguageChinese")

    LCRules.setLocale(if (isSystemLanguageChinese) LCLocale.ZH else LCLocale.EN)
    LCRules.setRemoteRepo(if (isSystemLanguageChinese) LCRemoteRepo.Gitlab else LCRemoteRepo.Github)
}.onFailure { XLog.e(it) }

fun getActivityRule(name: ComponentName) =
    getRule(name = name, type = ACTIVITY)

fun getServiceRule(name: ComponentName) =
    getRule(name = name, type = SERVICE)

fun getReceiverRule(name: ComponentName) =
    getRule(name = name, type = RECEIVER)

fun getProviderRule(name: ComponentName) =
    getRule(name = name, type = PROVIDER)

private fun getRule(name: ComponentName, type: Int): ComponentRule =
    runBlocking {
        val rule: Rule? = LCRules.getRule(libName = name.className, type = type, useRegex = true)
        val wrap = rule?.let {
            val isValidRes = isResource(AppGlobals.context, it.iconRes)
            ComponentRule(
                it.label,
                it.iconRes.takeIf { isValidRes } ?: 0,
                it.descriptionUrl,
                it.regexName,
                it.isSimpleColorIcon
            )
        }
        if (BuildConfig.DEBUG) {
            XLog.v("getRule: ${name.className} ${wrap?.label}")
        }
        wrap ?: fallbackRule
    }

data class ComponentRule(
    val label: String,
    @DrawableRes val iconRes: Int,
    val descriptionUrl: String?,
    val regexName: String?,
    val isSimpleColorIcon: Boolean,
)

val fallbackRule = ComponentRule(
    label = "Others",
    iconRes = 0,
    descriptionUrl = null,
    regexName = null,
    isSimpleColorIcon = false,
)

val fallbackRuleCategory = fallbackRule.toCategory()

private fun isResource(context: Context, resId: Int): Boolean {
    try {
        return context.resources.getResourceName(resId) != null
    } catch (ignore: Resources.NotFoundException) {
    }
    return false
}