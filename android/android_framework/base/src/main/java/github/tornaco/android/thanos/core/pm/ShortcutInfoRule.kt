package github.tornaco.android.thanos.core.pm

import github.tornaco.android.thanos.core.annotation.Keep
import github.tornaco.android.thanos.core.util.GsonUtils

@Keep
data class ShortcutInfoBlockRules(
    val commonRules: List<CommonRule>,
    val appRules: List<AppRule>,
) {
    @Keep
    data class CommonRule(
        val labelRegex: String
    )

    @Keep
    data class AppRule(
        val pkgName: String,
        val label: String?,
        val labelRegex: String?,
    )
}

fun main() {
    val rules = ShortcutInfoBlockRules(
        commonRules = listOf(
            ShortcutInfoBlockRules.CommonRule("Regex1"),
            ShortcutInfoBlockRules.CommonRule("Regex2"),
        ),
        appRules = listOf(
            ShortcutInfoBlockRules.AppRule(
                "com.xx.xx",
                label = "Label",
                labelRegex = null
            )
        )
    )

    println(
        GsonUtils.GSON.toJson(rules)
    )
}

