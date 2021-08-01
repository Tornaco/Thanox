package github.tornaco.android.thanos;

import android.content.Context;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.app.Init;
import github.tornaco.android.thanos.core.app.AppGlobals;
import io.reactivex.plugins.RxJavaPlugins;

public class ThanosApp extends MultipleModulesApp {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppGlobals.setContext(base);
    }

    @Override
    public void onCreate() {
        AppGlobals.setContext(this.getApplicationContext());

        super.onCreate();

        // Install error handler.
        // Error handler default print error info.
        RxJavaPlugins.setErrorHandler(throwable -> {
            XLog.e("\n");
            XLog.e("==== App un-handled error, please file a bug ====");
            XLog.e(throwable);
            XLog.e("\n");
        });

        if (BuildConfig.DEBUG) {
            DeveloperDiag.diag(this);
        }

        Init.init(this);
        CrashHandler.install(this);
    }

    public static boolean isPrc() {
        return Init.isPrc();
    }
}
