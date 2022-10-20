package github.tornaco.android.thanos.services.patch.common;

import com.elvishew.xlog.XLog;

import java.util.Arrays;
import java.util.List;

import github.tornaco.android.thanos.core.util.Optional;
import util.CollectionUtils;
import util.Consumer;
import util.XposedHelpers;

public class LocalServices {
    private final ClassLoader classLoader;

    public LocalServices(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Optional<Object> getService(Class<?> serviceClass) {
        List<Object> services = getServices();
        if (services != null) {
            for (Object service : services) {
                if (serviceClass == service.getClass()) {
                    return Optional.of(service);
                }
            }
        }
        return Optional.empty();
    }


    public void allServices(Consumer<Object> consumer) {
        List<Object> services = getServices();
        if (services != null) {
            CollectionUtils.consumeRemaining(services, consumer);
        }
    }


    @SuppressWarnings("unchecked")
    private List<Object> getServices() {
        Object systemServiceManager = getSystemServiceManager();
        if (systemServiceManager == null) {
            XLog.w("LocalServices.getService, systemServiceManager is null.");
            return null;
        }
        // private final ArrayList<SystemService> mServices = new ArrayList();
        List<Object> mServices = (List<Object>) XposedHelpers.getObjectField(systemServiceManager, "mServices");
        XLog.d("LocalServices.getService, mServices= %s", Arrays.toString(mServices.toArray()));
        return mServices;
    }


    // LocalServices.addService(SystemServiceManager.class, mSystemServiceManager);
    private Object getSystemServiceManager() {
        return LocalServicesHelper.INSTANCE.getService(SystemServiceManagerHelper.INSTANCE.systemServiceManagerClass(classLoader), classLoader);
    }
}
