package github.tornaco.android.thanos.core.app.activity;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import github.tornaco.android.thanos.core.pm.Pkg;
import lombok.Getter;

public class TopPackageChangeListener implements ITopPackageChangeListener {
    private final Handler h = new Handler(Looper.getMainLooper());
    @Getter
    private final ITopPackageChangeListener listener = new ITopPackageChangeListener.Stub() {
        @Override
        public void onChange(Pkg from, Pkg to) {
            h.post(() -> TopPackageChangeListener.this.onChange(from, to));
        }
    };

    @Override
    public void onChange(Pkg from, Pkg to) {
        // Noop.
    }

    @Override
    public final IBinder asBinder() {
        return listener.asBinder();
    }
}
