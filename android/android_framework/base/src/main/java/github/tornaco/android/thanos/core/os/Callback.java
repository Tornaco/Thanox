package github.tornaco.android.thanos.core.os;

import android.os.Handler;
import android.os.Looper;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.plus.ICallback;

public class Callback extends ICallback.Stub {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public final void onRes(RR res) {
        handler.post(() -> handleRes(res));
    }

    protected void handleRes(RR res) {
        XLog.w("Callback handleRes: %s", res);
    }
}
