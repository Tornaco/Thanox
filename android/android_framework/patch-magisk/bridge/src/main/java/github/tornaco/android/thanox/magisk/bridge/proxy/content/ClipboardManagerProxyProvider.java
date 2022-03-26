package github.tornaco.android.thanox.magisk.bridge.proxy.content;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.ServiceManager;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.secure.ops.AppOpsManager;
import util.Consumer;
import util.XposedHelpers;

public class ClipboardManagerProxyProvider {
    public static ClipboardManager provide(Context context) {
        try {
            return new ClipboardManagerProxy(context);
        } catch (Throwable e) {
            XLog.d("Fail provide ClipboardManager", e);
            throw new IllegalStateException("Fail provide ClipboardManager", e);
        }
    }


    @SuppressWarnings("rawtypes")
    private static class ClipboardManagerProxy extends ClipboardManager {
        private final Context context;
        private final ClipboardManager stockManager;

        public ClipboardManagerProxy(Context context)
                throws ServiceManager.ServiceNotFoundException {
            super(context, new Handler(Looper.getMainLooper()));
            this.context = context;
            this.stockManager = new ClipboardManager(context, new Handler(Looper.getMainLooper()));
        }

        @Override
        public ClipData getPrimaryClip() {
            XLog.d("ClipboardManagerProxy getPrimaryClip");
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
            XLog.d("ClipboardManagerProxy setPrimaryClip");
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

        // ---------------------------------- For MIUI 12.5 ----------------------------------------
        // ava.lang.NoSuchMethodException: github.tornaco.android.thanox.magisk.bridge.proxy.content.ClipboardManagerProxyProvider$ClipboardManagerProxy.getStashPrimaryClip []
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at java.lang.Class.getMethod(Class.java:2103)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at java.lang.Class.getDeclaredMethod(Class.java:2081)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.miui.permission.support.util.ReflectUtil.callObjectMethod(Unknown Source:4)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.lbe.security.ui.ClipboardTipDialog.customReadClipboardDialog(Unknown Source:14)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.lbe.security.ui.SecurityPromptHandler.handleNewRequest(Unknown Source:435)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.lbe.security.ui.SecurityPromptHandler.access$000(Unknown Source:0)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.lbe.security.ui.SecurityPromptHandler$1.handleMessage(Unknown Source:12)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:106)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at android.os.Looper.loopOnce(Looper.java:210)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at android.os.Looper.loop(Looper.java:299)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:8261)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at java.lang.reflect.Method.invoke(Native Method)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:556)
        //2022-03-26 10:01:26.378 18130-18130/? W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1045)
        //2022-03-26 10:01:26.378 18130-18130/? D/AlertController: dialog is not attached to window when dismiss is invoked
        // -----------------------------------------------------------------------------------------
        public ClipData getUserPrimaryClip() {
            return (ClipData) XposedHelpers.callMethod(stockManager, "getUserPrimaryClip");
        }

        public ClipData getStashPrimaryClip() {
            ClipData res = (ClipData) XposedHelpers.callMethod(stockManager, "getStashPrimaryClip");
            XLog.d("getStashPrimaryClip: " + res);
            return res;
        }

        public void pushClipboardRuleData(ParceledListSlice clipboardRuleInfo) {
            XposedHelpers.callMethod(stockManager, "pushClipboardRuleData", clipboardRuleInfo);
        }

        public ClipDescription getPrimaryClipDescription() {
            return super.getPrimaryClipDescription();
        }
        // -----------------------------------------------------------------------------------------
    }
}
