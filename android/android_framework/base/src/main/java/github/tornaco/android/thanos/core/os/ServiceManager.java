package github.tornaco.android.thanos.core.os;

import android.os.IBinder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ServiceManager {
    private IServiceManager server;

    @SneakyThrows
    public boolean hasService(String name) {
        return server.hasService(name);
    }

    @SneakyThrows
    public void addService(String name, IBinder binder) {
        server.addService(name, binder);
    }

    @SneakyThrows
    public IBinder getService(String name) {
        return server.getService(name);
    }
}
