package github.tornaco.android.thanos.core.profile;

import android.os.Handler;
import android.os.Looper;

import lombok.AccessLevel;
import lombok.Getter;

public class RuleChangeListener {

    protected void onRuleEnabledStateChanged(int ruleId, boolean enabled) {
        // Noop.
    }

    protected void onRuleUpdated(int ruleId) {
        // Noop.
    }

    protected void onRuleRemoved(int ruleId) {
        // Noop.
    }

    protected void onRuleAdd(int ruleId) {
        // Noop.
    }

    @Getter(AccessLevel.PACKAGE)
    private final IRuleChangeListener.Stub stub = new IRuleChangeListener.Stub() {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void onRuleEnabledStateChanged(int ruleId, boolean enabled) {
            handler.post(() -> RuleChangeListener.this.onRuleEnabledStateChanged(ruleId, enabled));
        }

        @Override
        public void onRuleUpdated(int ruleId) {
            handler.post(() -> RuleChangeListener.this.onRuleUpdated(ruleId));
        }

        @Override
        public void onRuleRemoved(int ruleId) {
            handler.post(() -> RuleChangeListener.this.onRuleRemoved(ruleId));
        }

        @Override
        public void onRuleAdd(int ruleId) {
            handler.post(() -> RuleChangeListener.this.onRuleAdd(ruleId));
        }
    };
}
