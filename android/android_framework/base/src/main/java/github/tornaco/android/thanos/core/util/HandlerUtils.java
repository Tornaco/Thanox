package github.tornaco.android.thanos.core.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerUtils {

    public static Handler newHandlerOfNewThread(String name) {
        HandlerThread hr = new HandlerThread(name);
        hr.start();
        return new Handler(hr.getLooper());
    }

    public static Looper newLooperOfNewThread(String name) {
        HandlerThread hr = new HandlerThread(name);
        hr.start();
        return hr.getLooper();
    }
}
