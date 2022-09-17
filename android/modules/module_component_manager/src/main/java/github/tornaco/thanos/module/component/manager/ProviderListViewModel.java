package github.tornaco.thanos.module.component.manager;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.thanos.module.component.manager.model.ComponentModel;
import util.CollectionUtils;

public class ProviderListViewModel extends ComponentListViewModel {

    public ProviderListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ComponentsLoader onCreateLoader() {
        return appInfo -> {
            List<ComponentModel> res = new ArrayList<>();
            ThanosManager thanox = ThanosManager.from(getApplication());
            CollectionUtils.consumeRemaining(thanox
                    .getPkgManager()
                    .getProviders(appInfo.getUserId(), appInfo.getPkgName()), providerInfo -> res.add(ComponentModel.builder()
                    .isDisabledByThanox(providerInfo.isDisabledByThanox())
                    .name(providerInfo.getName())
                    .componentName(providerInfo.getComponentName())
                    .label(providerInfo.getLabel())
                    .componentObject(providerInfo)
                    .enableSetting(providerInfo.getEnableSetting())
                    .build()));
            Collections.sort(res);
            return res;
        };
    }
}
