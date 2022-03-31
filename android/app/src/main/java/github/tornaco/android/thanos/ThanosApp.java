package github.tornaco.android.thanos;

import static github.tornaco.android.thanos.CrashHandlerKt.installCrashHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import dagger.hilt.android.HiltAndroidApp;
import github.tornaco.android.thanos.app.Init;
import github.tornaco.android.thanos.core.app.AppGlobals;
import github.tornaco.android.thanos.util.ActivityLifecycleCallbacksAdapter;
import github.tornaco.thanos.android.noroot.NoRootSupport;
import io.reactivex.plugins.RxJavaPlugins;

@HiltAndroidApp
public class ThanosApp extends MultipleModulesApp {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppGlobals.setContext(base);
        installCrashHandler(this);
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
        FeatureAccessStats.init(this);
        NoRootSupport.INSTANCE.install();
    }

    public static boolean isPrc() {
        return Init.isPrc();
    }
}
