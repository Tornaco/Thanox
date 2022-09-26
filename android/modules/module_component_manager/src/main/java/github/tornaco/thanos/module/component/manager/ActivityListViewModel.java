package github.tornaco.thanos.module.component.manager;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.ComponentInfo;
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
            ThanosManager thanox = ThanosManager.from(getApplication());

            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                List<ComponentInfo> batch = thanox.getPkgManager().getActivitiesInBatch(appInfo.getUserId(), appInfo.getPkgName(), 20, i);
                if (batch == null) {
                    break;
                }
                CollectionUtils.consumeRemaining(batch, activityInfo ->
                        res.add(ComponentModel.builder()
                                .name(activityInfo.getName())
                                .componentName(activityInfo.getComponentName())
                                .isDisabledByThanox(activityInfo.isDisabledByThanox())
                                .label(activityInfo.getLabel())
                                .componentObject(activityInfo)
                                .enableSetting(activityInfo.getEnableSetting())
                                .componentRule(LCRulesAdapterKt.getActivityRule(activityInfo.getComponentName()))
                                .build()));
            }
            Collections.sort(res);
            return res;
        };
    }
}
