package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ServiceManager;

public class ClipboardManagerProxyProvider {
    public static ClipboardManager provide(Context context) {
        try {
            return new ClipboardManagerProxy(context);
        } catch (Throwable e) {
            logging("Fail provide ClipboardManager", e);
            throw new IllegalStateException("Fail provide ClipboardManager", e);
        }
    }


    private static class ClipboardManagerProxy extends ClipboardManager {
        public ClipboardManagerProxy(Context context)
                throws ServiceManager.ServiceNotFoundException {
            super(context, new Handler(Looper.getMainLooper()));
        }

        @Override
        public ClipData getPrimaryClip() {
            logging("ClipboardManagerProxy getPrimaryClip");
            return super.getPrimaryClip();
        }

        @Override
        public void setPrimaryClip(ClipData clip) {
            logging("ClipboardManagerProxy setPrimaryClip");
            super.setPrimaryClip(clip);
        }
    }
}
