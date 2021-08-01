package github.tornaco.android.thanos;

import android.app.Application;
import android.widget.Toast;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;

class DeveloperDiag {

    static void diag(Application application) {
        ThanosManager.from(application)
                .ifServiceInstalled(thanosManager ->
                        XLog.w("Platform app idle enabled: %s", (thanosManager.getActivityManager().isPlatformAppIdleEnabled())));

        Toast.makeText(application, "Hello, Thanox developer!", Toast.LENGTH_LONG).show();
    }
}
