package github.tornaco.android.thanos.core.profile.handle;

import android.content.ComponentName;
import android.content.Intent;

@HandlerName("activity")
public interface IActivity {

    boolean launchProcessForPackage(String pkgName);

    boolean launchProcessForPackage(String pkgName, int userId);

    boolean launchActivity(Intent intent);

    boolean launchMainActivityForPackage(String pkgName);

    boolean launchActivityForUser(Intent intent, int userId);

    boolean launchMainActivityForPackageForUser(String pkgName, int userId);

    Intent getLaunchIntentForPackage(String pkgName);

    Intent getLaunchIntentForPackage(String pkgName, int userId);

    String getFrontAppPackage();

    ComponentName getFrontAppPackageComponent();

    void setInactive(String pkgName);

    boolean isInactive(String pkgName);
}
