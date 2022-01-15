package github.tornaco.android.thanox.magisk.bridge.proxy;

import android.app.SystemServiceRegistry;
import android.content.ClipboardManager;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.elvishew.xlog.XLog;

import java.util.Map;

import util.XposedHelpers;

@SuppressWarnings("unchecked")
public class SystemServiceRegistryHookInstaller {

    public static void install() {
        XLog.d("SystemServiceRegistryHookInstaller install");

        Map<Class<?>, String> SYSTEM_SERVICE_NAMES
                = (Map<Class<?>, String>) XposedHelpers.getStaticObjectField(SystemServiceRegistry.class, "SYSTEM_SERVICE_NAMES");
        XLog.d("SystemServiceRegistryHookInstaller SYSTEM_SERVICE_NAMES= %s size: %s", SYSTEM_SERVICE_NAMES, SYSTEM_SERVICE_NAMES.size());

        Map<String, Object> SYSTEM_SERVICE_FETCHERS
                = (Map<String, Object>) XposedHelpers.getStaticObjectField(SystemServiceRegistry.class, "SYSTEM_SERVICE_FETCHERS");
        XLog.d("SystemServiceRegistryHookInstaller SYSTEM_SERVICE_FETCHERS= %s size: %s", SYSTEM_SERVICE_FETCHERS, SYSTEM_SERVICE_FETCHERS.size());

        ServiceFetcherProxy.installInto(SYSTEM_SERVICE_FETCHERS);

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
                            return TelephonyManagerProxyProvider.provide(context);
                        }
                    }
            );

            SystemServiceRegistry.registerContextAwareService(Context.CLIPBOARD_SERVICE, ClipboardManager.class,
                    new SystemServiceRegistry.ContextAwareServiceProducerWithoutBinder<ClipboardManager>() {
                        @Override
                        public ClipboardManager createService(Context context) {
                            return ClipboardManagerProxyProvider.provide(context);
                        }
                    });

            XposedHelpers.setStaticBooleanField(SystemServiceRegistry.class, "sInitializing", false);
        } catch (Throwable e) {
            XLog.e("SystemServiceRegistryHookInstaller install error", e);
        }
    }
}
