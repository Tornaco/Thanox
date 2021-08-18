package github.tornaco.thanox.android.server.patch.framework;

import com.android.server.SystemService;
import com.android.server.SystemServiceManager;
import com.elvishew.xlog.XLog;

import java.util.Arrays;
import java.util.List;

import github.tornaco.android.thanos.core.util.Optional;
import lombok.val;
import util.CollectionUtils;
import util.Consumer;
import util.XposedHelpers;

public class LocalServices {

    public static Optional<SystemService> getService(Class<?> serviceClass) {
        val services = getServices();
        if (services != null) {
            for (SystemService service : services) {
                if (serviceClass == service.getClass()) {
                    return Optional.of(service);
                }
            }
        }
        return Optional.empty();
    }


    public static void allServices(Consumer<SystemService> consumer) {
        val services = getServices();
        if (services != null) {
            CollectionUtils.consumeRemaining(services, consumer);
        }
    }


    @SuppressWarnings("unchecked")
    private static List<SystemService> getServices() {
        SystemServiceManager systemServiceManager = getSystemServiceManager();
        if (systemServiceManager == null) {
            XLog.d("LocalServices.getService, systemServiceManager is null.");
            return null;
        }
        // private final ArrayList<SystemService> mServices = new ArrayList();
        List<SystemService> mServices = (List<SystemService>) XposedHelpers.getObjectField(systemServiceManager, "mServices");
        XLog.d("LocalServices.getService, mServices= %s", Arrays.toString(mServices.toArray()));
        return mServices;
    }


    // LocalServices.addService(SystemServiceManager.class, mSystemServiceManager);
    private static SystemServiceManager getSystemServiceManager() {
        return com.android.server.LocalServices.getService(SystemServiceManager.class);
    }

}
