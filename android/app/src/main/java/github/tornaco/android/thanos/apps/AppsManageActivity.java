package github.tornaco.android.thanos.apps;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class AppsManageActivity extends CommonAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, AppsManageActivity.class);
    }

    @Override
    protected int getTitleRes() {
        return R.string.activity_title_apps_manager;
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return appInfo -> AppDetailsActivity.start(AppsManageActivity.this, appInfo);
    }

    @NonNull
    @Override
    @Verify
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        String runningBadge = getString(R.string.badge_app_running);
        String idleBadge = getApplicationContext().getString(R.string.badge_app_idle);
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) {
                return Lists.newArrayList(new AppListModel(AppInfo.dummy()));
            }
            List<AppListModel> res = new ArrayList<>();
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Observable.fromIterable(thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId))
                    .distinct()
                    .doOnComplete(disposable::dispose)
                    .subscribe(appInfo -> res.add(new AppListModel(
                            appInfo,
                            thanos.getActivityManager().isPackageRunning(Pkg.fromAppInfo(appInfo)) ? runningBadge : null,
                            thanos.getActivityManager().isPackageIdle(Pkg.fromAppInfo(appInfo)) ? idleBadge : null,
                            composer.getAppItemDescription(appInfo)))));
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }
}
