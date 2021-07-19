package github.tornaco.android.thanos.core.app.infinite;

import android.os.Handler;
import android.os.Looper;

import github.tornaco.android.thanos.core.annotation.UiThread;

@SuppressWarnings("WeakerAccess")
public abstract class EnableCallback extends IEnableCallback.Stub {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public final void onSuccess(int userId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccessMain(userId);
            }
        });
    }

    @Override
    public final void onError(String errorMessage, int errorCode) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onErrorMain(errorMessage, errorCode);
            }
        });
    }

    @UiThread
    public abstract void onSuccessMain(int userId);

    @UiThread
    public abstract void onErrorMain(String errorMessage, int errorCode);
}
