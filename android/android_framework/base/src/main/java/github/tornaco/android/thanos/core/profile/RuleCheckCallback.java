package github.tornaco.android.thanos.core.profile;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import lombok.AccessLevel;
import lombok.Getter;

public class RuleCheckCallback {

    protected void onValid() {

    }

    protected void onInvalid(int errorCode, String errorMessage) {

    }

    @Getter(AccessLevel.PACKAGE)
    private IRuleCheckCallback.Stub stub = new IRuleCheckCallback.Stub() {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override

        public void onValid() throws RemoteException {
            handler.post(RuleCheckCallback.this::onValid);
        }

        @Override
        public void onInvalid(int errorCode, String errorMessage) throws RemoteException {
            handler.post(() -> RuleCheckCallback.this.onInvalid(errorCode, errorMessage));
        }

    };
}
