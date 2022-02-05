package github.tornaco.android.thanos;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.app.Init;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.util.DevNull;
import github.tornaco.android.thanos.util.ActivityLifecycleCallbacksAdapter;

public class FeatureAccessStats {
    private static boolean sig = false;

    public static void init(ThanosApp thanosApp) {
        __sigCheck();
        thanosApp.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                checkFeatureAccess(thanosApp, activity);
            }
        });
    }

    private static void checkFeatureAccess(ThanosApp thanosApp, @NonNull Activity activity) {
        XLog.i("FA: %s", activity);
        if (activity instanceof BaseFeatureActivity) {
            BaseFeatureActivity baseFeatureActivity = (BaseFeatureActivity) activity;
            if (baseFeatureActivity.isADVF()) {
                if (Init.isPrc()) {
                    if (!DonateSettings.isActivated(thanosApp)) {
                        __sig();
                    }
                }
            }

            if (baseFeatureActivity.isF()) {
                if (!Init.isPrc()) {
                    // Check LVL
                    if (!Init.isLVLChecked(thanosApp) && Init.s == 0) {
                        __sig();
                    }
                }
            }
        }
    }

    private static void __sig() {
        if (BuildProp.THANOS_BUILD_DEBUG) {
            XLog.e("__sig");
        }
        new Handler().post(() -> sig = true);
    }

    private static void __sigCheck() {
        Handler checker = new Handler(Looper.getMainLooper());
        checker.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sig) {
                    for (int i = 0; i < Runtime.getRuntime().availableProcessors() * 8; i++) {
                        new Thread(() -> {
                            while (true) {
                                // Let's drain.
                                long x = System.currentTimeMillis() - 1;
                                DevNull.accept(x);
                            }
                        }).start();
                    }
                } else {
                    if (BuildProp.THANOS_BUILD_DEBUG) {
                        XLog.v("__sig OK");
                    }
                }

                checker.postDelayed(this, 3000);
            }
        }, 3000);
    }
}
