package github.tornaco.android.thanos.core.profile;

import android.os.Handler;
import android.os.Looper;

import lombok.AccessLevel;
import lombok.Getter;

public class RuleAddCallback {

    protected void onRuleAddSuccess() {

    }

    protected void onRuleAddFail(int errorCode, String errorMessage) {

    }

    @Getter(AccessLevel.PACKAGE)
    private IRuleAddCallback.Stub stub = new IRuleAddCallback.Stub() {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void onRuleAddSuccess() {
            handler.post(RuleAddCallback.this::onRuleAddSuccess);
        }

        @Override
        public void onRuleAddFail(int errorCode, String errorMessage) {
            handler.post(() -> RuleAddCallback.this.onRuleAddFail(errorCode, errorMessage));
        }
    };
}
