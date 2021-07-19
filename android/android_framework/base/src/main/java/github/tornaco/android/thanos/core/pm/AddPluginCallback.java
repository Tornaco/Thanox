package github.tornaco.android.thanos.core.pm;

import android.os.Handler;
import android.os.Looper;

import lombok.Getter;

public class AddPluginCallback {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Getter
    private final IAddPluginCallback.Stub stub = new IAddPluginCallback.Stub() {
        @Override
        public void onPluginAdd() {
            handler.post(AddPluginCallback.this::onPluginAdd);
        }

        @Override
        public void onFail(String message) {
            handler.post(() -> AddPluginCallback.this.onFail(message));
        }

        @Override
        public void onProgress(String progressMessage) {
            handler.post(() -> AddPluginCallback.this.onProgress(progressMessage));
        }
    };

    public void onPluginAdd() {

    }

    public void onFail(String message) {

    }

    public void onProgress(String progressMessage) {

    }
}
