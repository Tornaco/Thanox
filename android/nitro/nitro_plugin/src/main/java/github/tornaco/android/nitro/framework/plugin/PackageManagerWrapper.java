package github.tornaco.android.nitro.framework.plugin;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class PackageManagerWrapper extends PackageManager {

    @Delegate(excludes = PMExcludes.class)
    private final PackageManager origin;

    @Override
    public ActivityInfo getActivityInfo(ComponentName component, int flags) throws NameNotFoundException {
        Log.d("PackageManagerWrapper", "getActivityInfo called: " + component);
        return origin.getActivityInfo(
                new ComponentName(component.getPackageName(), "github.tornaco.android.nitro.framework.host.ContainerActivity"),
                flags);
    }

    interface PMExcludes {
        ActivityInfo getActivityInfo(ComponentName component, int flags);
    }
}
