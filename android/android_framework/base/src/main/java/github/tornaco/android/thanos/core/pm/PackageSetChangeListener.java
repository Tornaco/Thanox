package github.tornaco.android.thanos.core.pm;

import android.os.Handler;
import android.os.Looper;

public class PackageSetChangeListener {

    private final Handler handler = new Handler(Looper.getMainLooper());

    final IPackageSetChangeListener stub = new IPackageSetChangeListener.Stub() {
        @Override
        public void onPackageSetAdded(String pkgSetId) {
            handler.post(() -> PackageSetChangeListener.this.onPackageSetAdded(pkgSetId));
        }

        @Override
        public void onPackageSetRemoved(String pkgSetId) {
            handler.post(() -> PackageSetChangeListener.this.onPackageSetRemoved(pkgSetId));
        }

        @Override
        public void onPackageSetChanged(String pkgSetId) {
            handler.post(() -> PackageSetChangeListener.this.onPackageSetChanged(pkgSetId));
        }
    };

    public void onPackageSetAdded(String pkgSetId) {

    }

    public void onPackageSetRemoved(String pkgSetId) {

    }

    public void onPackageSetChanged(String pkgSetId) {

    }
}
