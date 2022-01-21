package github.tornaco.android.thanos;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;

import github.tornaco.android.plugin.push.message.delegate.WechatPushDeleteMainActivity;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.util.Preconditions;
import github.tornaco.android.thanos.util.ActivityLifecycleCallbacksAdapter;

public class FeatureAccessStats {

    public static void init(ThanosApp thanosApp) {
        thanosApp.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                checkFeatureAccess(thanosApp, activity.getClass());
            }
        });
    }

    private static void checkFeatureAccess(ThanosApp thanosApp, Class<?> featureClass) {
        if (featureClass == WechatPushDeleteMainActivity.class) {
            XLog.v("FeatureAccessStats checkFeatureAccess for WechatPush");
            Preconditions.checkArgument(!ThanosApp.isPrc() || DonateSettings.isActivated(thanosApp));
        }
    }
}
