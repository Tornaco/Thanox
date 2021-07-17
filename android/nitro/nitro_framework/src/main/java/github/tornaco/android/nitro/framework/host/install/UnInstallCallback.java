package github.tornaco.android.nitro.framework.host.install;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;

public interface UnInstallCallback {
    @WorkerThread
    void onUnInstallSuccess(@NonNull InstalledPlugin plugin);

    @WorkerThread
    void onUnInstallFail(int code, @NonNull Throwable err);

    abstract class MainAdapter implements UnInstallCallback {
        private final Handler h = new Handler(Looper.getMainLooper());

        @Override
        @WorkerThread
        public void onUnInstallSuccess(@NonNull InstalledPlugin plugin) {
            h.post(() -> onPostUnInstallSuccess(plugin));
        }

        @Override
        @WorkerThread
        public void onUnInstallFail(int code, @NonNull Throwable err) {
            h.post(() -> onPostUnInstallFail(code, err));
        }

        public abstract void onPostUnInstallSuccess(@NonNull InstalledPlugin plugin);

        public abstract void onPostUnInstallFail(int code, @NonNull Throwable err);
    }
}
