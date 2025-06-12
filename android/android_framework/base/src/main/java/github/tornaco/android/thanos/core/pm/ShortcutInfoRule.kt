package github.tornaco.android.thanos.core.pm

import github.tornaco.android.thanos.core.annotation.Keep
import github.tornaco.android.thanos.core.util.GsonUtils

@Keep
data class ShortcutInfoBlockRules(
    val commonRule: CommonRule,
    val appRules: List<AppRule>,
) {
    @Keep
    data class CommonRule(
        val labelRegexes: List<String>
    )

    @Keep
    data class AppRule(
        val pkgName: String,
        val labels: List<String>,
        val labelRegexes: List<String>,
    )
}

fun main() {
    val rules = ShortcutInfoBlockRules(
        commonRule = ShortcutInfoBlockRules.CommonRule(listOf("卸载", "福气兑红包")),
        appRules = listOf(
            ShortcutInfoBlockRules.AppRule(
                "com.tmall.wireless",
                labels = listOf("一键清理加速"),
                labelRegexes = emptyList()
            ),
            ShortcutInfoBlockRules.AppRule(
                "com.shizhuang.duapp",
                labels = listOf("空间清理"),
                labelRegexes = emptyList()
            ),
            ShortcutInfoBlockRules.AppRule(
                "com.xunmeng.pinduoduo",
                labels = listOf("现金可提现", "好礼免费拿"),
                labelRegexes = emptyList()
            ),
            ShortcutInfoBlockRules.AppRule(
                "me.ele",
                labels = listOf("清理缓存", "桌面组件中心", "我的红包"),
                labelRegexes = emptyList()
            ),
        )
    )

    println(
        GsonUtils.GSON.toJson(rules)
    )
}

