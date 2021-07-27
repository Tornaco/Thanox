package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.SystemServiceRegistry;
import android.content.Context;
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
                                    XLog.w("ActivityThreadProxy TelephonyManager getNetworkOperator called %s %s", caller, res);
                                    return res;
                                }
                            };
                        }
                    }
            );

            XposedHelpers.setStaticBooleanField(SystemServiceRegistry.class, "sInitializing", false);
        } catch (Throwable e) {
            XLog.e("serviceFetchers error", e);
        }
    }
}
