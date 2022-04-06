package github.tornaco.android.thanos;

import android.app.Application;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;

class DeveloperDiag {

    static void diag(Application application) {
        ThanosManager.from(application).ifServiceInstalled(thanosManager ->
                XLog.w("DeveloperDiag, CachedAppsFreezer supported: " + thanosManager.getActivityManager().isCachedAppsFreezerSupported()));
    }
}
