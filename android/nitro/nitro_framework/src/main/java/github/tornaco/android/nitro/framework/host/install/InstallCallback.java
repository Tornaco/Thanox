package github.tornaco.android.nitro.framework.host.install;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;

public interface InstallCallback {
    @WorkerThread
    void onInstallSuccess(@NonNull InstalledPlugin plugin);

    @WorkerThread
    void onInstallFail(int code, @NonNull Throwable err);

    abstract class MainAdapter implements InstallCallback {
        private final Handler h = new Handler(Looper.getMainLooper());

        @Override
        @WorkerThread
        public void onInstallSuccess(@NonNull InstalledPlugin plugin) {
            h.post(() -> onPostInstallSuccess(plugin));
        }

        @Override
        @WorkerThread
        public void onInstallFail(int code, @NonNull Throwable err) {
            h.post(() -> onPostInstallFail(code, err));
        }

        public abstract void onPostInstallSuccess(@NonNull InstalledPlugin plugin);

        public abstract void onPostInstallFail(int code, @NonNull Throwable err);
    }
}
