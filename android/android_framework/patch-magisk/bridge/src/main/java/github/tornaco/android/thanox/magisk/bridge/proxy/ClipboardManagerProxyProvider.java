package github.tornaco.android.thanox.magisk.bridge.proxy;

import static github.tornaco.android.thanox.magisk.bridge.Logging.logging;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.ServiceManager;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import util.Consumer;

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
        private final Context context;

        public ClipboardManagerProxy(Context context)
                throws ServiceManager.ServiceNotFoundException {
            super(context, new Handler(Looper.getMainLooper()));
            this.context = context;
        }

        @Override
        public ClipData getPrimaryClip() {
            logging("ClipboardManagerProxy getPrimaryClip");
            ThanosManager.from(context).ifServiceInstalled(new Consumer<ThanosManager>() {
                @Override
                public void accept(ThanosManager thanosManager) {
                    thanosManager.getAppOpsManager()
                            .onStartOp(new Binder(),
                                    AppOpsManager.OP_READ_CLIPBOARD,
                                    Binder.getCallingUid(),
                                    context.getPackageName());
                }
            });
            return super.getPrimaryClip();
        }

        @Override
        public void setPrimaryClip(ClipData clip) {
            logging("ClipboardManagerProxy setPrimaryClip");
            ThanosManager.from(context).ifServiceInstalled(new Consumer<ThanosManager>() {
                @Override
                public void accept(ThanosManager thanosManager) {
                    thanosManager.getAppOpsManager()
                            .onStartOp(new Binder(),
                                    AppOpsManager.OP_WRITE_CLIPBOARD,
                                    Binder.getCallingUid(),
                                    context.getPackageName());
                }
            });
            super.setPrimaryClip(clip);
        }
    }
}
