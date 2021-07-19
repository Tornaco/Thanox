package github.tornaco.android.thanos.core.os;


interface IServiceManager {
    boolean hasService(String name);

    void addService(String name, IBinder binder);

    IBinder getService(String name);
}