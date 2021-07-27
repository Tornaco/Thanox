package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.SystemServiceRegistry;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ServiceManager;
import android.telephony.TelephonyManager;

import com.elvishew.xlog.XLog;

import util.XposedHelpers;

public class SystemServiceRegistryProxy {

    public static void install() {
        // private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =
        //            new ArrayMap<String, ServiceFetcher<?>>();
        try {
            // sInitializing
            XposedHelpers.setStaticBooleanField(SystemServiceRegistry.class, "sInitializing", true);
            // SystemServiceRegistry.ContextAwareServiceProducerWithoutBinder
            SystemServiceRegistry.registerContextAwareService(
                    Context.TELEPHONY_SERVICE,
                    TelephonyManager.class,
                    new SystemServiceRegistry.ContextAwareServiceProducerWithoutBinder<TelephonyManager>() {
                        @Override
                        public TelephonyManager createService(Context context) {
                            return new TelephonyManager(context) {
                                @Override
                                public String getNetworkOperator() {
                                    String res = super.getNetworkOperator();
                                    String caller = context.getPackageName();
                                    XLog.w("SystemServiceRegistryProxy TelephonyManager getNetworkOperator called %s %s", caller, res);
                                    return res;
                                }
                            };
                        }
                    }
            );

            SystemServiceRegistry.registerContextAwareService(Context.CLIPBOARD_SERVICE, ClipboardManager.class,
                    (context, serviceBinder) -> {
                        try {
                            return new ClipboardManager(context,
                                    new Handler(Looper.getMainLooper())) {
                                @Override
                                public void setPrimaryClip(ClipData clip) {
                                    String caller = context.getPackageName();
                                    XLog.w("SystemServiceRegistryProxy ClipboardManager setPrimaryClip called %s", caller);
                                    super.setPrimaryClip(clip);
                                }

                                @Override
                                public ClipData getPrimaryClip() {
                                    String caller = context.getPackageName();
                                    XLog.w("SystemServiceRegistryProxy ClipboardManager getPrimaryClip called %s", caller);
                                    return super.getPrimaryClip();
                                }
                            };
                        } catch (ServiceManager.ServiceNotFoundException e) {
                            XLog.e("SystemServiceRegistryProxy error create ClipboardManager", e);
                            return null;
                        }
                    });

            XposedHelpers.setStaticBooleanField(SystemServiceRegistry.class, "sInitializing", false);
        } catch (Throwable e) {
            XLog.e("serviceFetchers error", e);
        }
    }
}
