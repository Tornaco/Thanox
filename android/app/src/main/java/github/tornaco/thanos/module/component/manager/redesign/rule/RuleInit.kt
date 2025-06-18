package github.tornaco.thanos.module.component.manager.redesign.rule

import android.content.Context
import java.util.concurrent.atomic.AtomicBoolean

object RuleInit {
    private val isRuleInited: AtomicBoolean = AtomicBoolean(false)

    fun init(context: Context) {
        if (isRuleInited.compareAndSet(false, true)) {
            LCRules.initRules(context)
            BlockerRules.initPrebuiltRules(context)
        }
    }
}