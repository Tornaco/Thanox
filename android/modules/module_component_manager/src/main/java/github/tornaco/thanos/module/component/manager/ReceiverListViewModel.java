package github.tornaco.thanos.module.component.manager;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.CollectionUtils;

public class ReceiverListViewModel extends ComponentListViewModel {

    public ReceiverListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ComponentsLoader onCreateLoader() {
        return appInfo -> {
            List<ComponentModel> res = new ArrayList<>();
            ThanosManager thanox = ThanosManager.from(getApplication());
            CollectionUtils.consumeRemaining(thanox
                    .getPkgManager()
                    .getReceivers(appInfo.getPkgName()), activityInfo -> res.add(ComponentModel.builder()
                    .isDisabledByThanox(activityInfo.isDisabledByThanox())
                    .name(activityInfo.getName())
                    .componentName(activityInfo.getComponentName())
                    .label(activityInfo.getLabel())
                    .componentObject(activityInfo)
                    .enableSetting(activityInfo.getEnableSetting())
                    .build()));
            Collections.sort(res);
            return res;
        };
    }
}
