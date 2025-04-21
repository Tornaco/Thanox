package github.tornaco.thanos.module.component.manager.redesign.rule

import android.content.Context
import com.elvishew.xlog.XLog
import com.google.gson.reflect.TypeToken
import github.tornaco.android.thanos.core.annotation.DoNotStrip
import github.tornaco.android.thanos.core.util.GsonUtils
import java.util.Locale

@DoNotStrip
data class BlockerRule(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val company: String,
    val searchKeyword: List<String>,
    val useRegexSearch: Boolean,
    val description: String,
    val safeToBlock: Boolean,
    val sideEffect: String,
    val contributors: List<String>
)

data class CompiledRule(val rule: BlockerRule, val searchKeywordRegex: List<Regex>) {
    fun match(className: String): Boolean {
        return searchKeywordRegex.any { it.equals(className) || it.containsMatchIn(className) }
    }
}

object BlockerRules {
    var rules: List<CompiledRule> = emptyList()
        private set

    private val classNameCache = mutableMapOf<String, BlockerRule?>()

    fun String.classNameToRule(): BlockerRule? {
        synchronized(classNameCache) {
            if (classNameCache.contains(this)) return classNameCache[this]
            return rules.firstOrNull {
                it.match(this)
            }?.rule.also {
                classNameCache[this] = it
            }
        }

    }

    fun initPrebuiltRules(context: Context) {
        val locale = context.resources.configuration.locales[0]
        val isSystemLanguageChinese = locale.language == Locale.CHINESE.language
        XLog.w("BlockerRules.initPrebuiltRules: $locale $isSystemLanguageChinese")
        val rulePath = if (isSystemLanguageChinese) {
            "blocker_rules/zh_CN/general.json"
        } else {
            "blocker_rules/en/general.json"
        }
        XLog.w("BlockerRules.rulePath: $rulePath")
        val jsonString = context.assets.open(rulePath).use {
            it.bufferedReader().readText()
        }
        kotlin.runCatching {
            val rawRules = GsonUtils.GSON.fromJson<List<BlockerRule>?>(
                jsonString,
                object : TypeToken<List<BlockerRule>>() {}.type
            )
            rules = rawRules.map { rule ->
                CompiledRule(rule, rule.searchKeyword.map { Regex(it) })
            }
        }.onFailure {
            XLog.e(it, "BlockerRules, parse rules err")
        }
        XLog.w("BlockerRules, rules count: ${rules.size}")
    }
}