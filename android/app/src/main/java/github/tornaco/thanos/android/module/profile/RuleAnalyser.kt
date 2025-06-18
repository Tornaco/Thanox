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

package github.tornaco.thanos.android.module.profile

import android.content.Context
import github.tornaco.android.thanos.core.app.ThanosManager

fun formatGlobalVarNames(vars: List<String>, splitter: String = System.lineSeparator()): String {
    return vars.joinToString(splitter) { "[$it]" }
}

fun getMissingGlobalVarNames(context: Context, rule: String?): List<String> {
    val profile = ThanosManager.from(context).profileManager
    val allGlobalVarNames = profile.allGlobalRuleVarNames
    return getGlobalVarName(rule).filterNot {
        allGlobalVarNames.contains(it)
    }
}


fun getGlobalVarName(rule: String?): List<String> = runCatching {
    val globalVar = "globalVarOf\$"
    if (rule.isNullOrBlank()) return emptyList()
    if (!rule.contains(globalVar)) return emptyList()
    val regex = """globalVarOf\$[^\s.)=]+""".toRegex()
    return regex.findAll(rule).map {
        it.value.replace(globalVar, "")
    }.toList().distinct()
}.getOrElse {
    emptyList()
}