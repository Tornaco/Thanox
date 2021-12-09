package github.tornaco.thanos.module.component.manager;

import android.app.Application;
import android.content.ComponentName;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.CollectionUtils;

public class ServiceListViewModel extends ComponentListViewModel {

    public ServiceListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ComponentsLoader onCreateLoader() {
        return appInfo -> {
            List<ComponentModel> res = new ArrayList<>();
            ThanosManager thanox = ThanosManager.from(getApplication());
            List<android.app.ActivityManager.RunningServiceInfo> runningServiceInfos
                    = thanox.getActivityManager().getRunningServiceLegacy(1000);
            List<ComponentName> runningComponents = new ArrayList<>();
            CollectionUtils.consumeRemaining(runningServiceInfos, runningServiceInfo -> runningComponents.add(runningServiceInfo.service));
            CollectionUtils.consumeRemaining(thanox.getPkgManager().getServices(appInfo.getPkgName()),
                    serviceInfo -> res.add(ComponentModel.builder()
                            .name(serviceInfo.getName())
                            .componentName(serviceInfo.getComponentName())
                            .isDisabledByThanox(serviceInfo.isDisabledByThanox())
                            .label(serviceInfo.getLabel())
                            .componentObject(serviceInfo)
                            .isRunning(runningComponents.contains(serviceInfo.getComponentName()))
                            .enableSetting(serviceInfo.getEnableSetting())
                            .build()));
            Collections.sort(res);
            return res;
        };
    }
}
