package github.tornaco.android.thanos.core.pm;

import android.os.Handler;
import android.os.Looper;

import java.util.List;

public class PackageEnableStateChangeListener {

    private final Handler handler = new Handler(Looper.getMainLooper());

    final IPackageEnableStateChangeListener stub = new IPackageEnableStateChangeListener.Stub() {
        @Override
        public void onPackageEnableStateChanged(List<Pkg> pkgs) {
            handler.post(() -> PackageEnableStateChangeListener.this.onPackageEnableStateChanged(pkgs));
        }
    };

    public void onPackageEnableStateChanged(List<Pkg> pkgs) {

    }
}
