package github.tornaco.android.thanos.core.plus;

import android.os.Handler;
import android.os.Looper;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.BuildProp;

public class Callback extends ICallback.Stub {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public final void onRes(RR res) {
        handler.post(() -> handleRes(res));
    }

    protected void handleRes(RR res) {
        if (BuildProp.THANOS_BUILD_DEBUG) {
            XLog.w("Callback handleRes: %s", res);
        }
    }
}
