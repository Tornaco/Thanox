package github.tornaco.android.thanos.core.profile;

import android.os.Handler;
import android.os.Looper;

import lombok.AccessLevel;
import lombok.Getter;

public class RuleCheckCallback {

    protected void onValid(RuleInfo ruleInfo) {

    }

    protected void onInvalid(int errorCode, String errorMessage) {

    }

    @Getter(AccessLevel.PACKAGE)
    private IRuleCheckCallback.Stub stub = new IRuleCheckCallback.Stub() {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override

        public void onValid(RuleInfo ruleInfo) {
            handler.post(() -> RuleCheckCallback.this.onValid(ruleInfo));
        }

        @Override
        public void onInvalid(int errorCode, String errorMessage) {
            handler.post(() -> RuleCheckCallback.this.onInvalid(errorCode, errorMessage));
        }

    };
}
