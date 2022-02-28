package github.tornaco.android.thanox.shortcut.stub;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.Pkg;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLog.init();
        try {
            if (!ThanosManager.from(getApplicationContext()).isServiceInstalled()) {
                Toast.makeText(this, "Thanos core is not available!", Toast.LENGTH_LONG).show();
            }
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> {
                        String pkgName = getPackageName();
                        Pkg pkg = thanosManager.getPkgManager().resolveShortcutPackageName(pkgName);
                        if (pkg != null) {
                            thanosManager.getPkgManager().launchSmartFreezePkgThenKillOriginForUser(pkg, pkgName);
                        }
                    });
        } finally {
            finish();
        }
    }
}
