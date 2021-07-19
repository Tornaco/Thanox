package github.tornaco.thanos.module.component.manager;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.CollectionUtils;

public class ActivityListViewModel extends ComponentListViewModel {

    public ActivityListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ComponentsLoader onCreateLoader() {
        return appInfo -> {
            List<ComponentModel> res = new ArrayList<>();
            PackageManager pm = getApplication().getPackageManager();
            ThanosManager thanox = ThanosManager.from(getApplication());

            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                ActivityInfo[] batch = thanox.getPkgManager().getActivitiesInBatch(appInfo.getPkgName(), 20, i);
                if (batch == null) {
                    break;
                }
                CollectionUtils.consumeRemaining(batch, activityInfo ->
                        res.add(ComponentModel.builder()
                                .name(activityInfo.name)
                                .componentName(PkgUtils.getComponentName(activityInfo))
                                .isDisabledByThanox(thanox.getPkgManager().isComponentDisabledByThanox(PkgUtils.getComponentName(activityInfo)))
                                .label(String.valueOf(activityInfo.loadLabel(pm)))
                                .componentObject(activityInfo)
                                .enableSetting(thanox.getPkgManager().getComponentEnabledSetting(
                                        PkgUtils.getComponentName(activityInfo)))
                                .build()));
            }
            Collections.sort(res);
            return res;
        };
    }
}
