package github.tornaco.android.thanos.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.module.common.R;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class AppPickerActivity extends CommonAppListFilterActivity {
    private static final String EXTRA_EXCLUDE_PKGS = "github.tornaco.android.thanos.picker.extra.EXTRA_EXCLUDE_PKGS";

    private final Map<String, AppInfo> selectedAppInfoMap = Maps.newHashMap();
    private final ArrayList<Pkg> excludePkgs = Lists.newArrayList();
    private CommonAppListFilterAdapter appListFilterAdapter = null;

    private final AppItemClickListener appItemClickListener = appInfo -> {
        if (appInfo.isSelected()) {
            selectedAppInfoMap.put(appInfo.getPkgName(), appInfo);
        } else {
            selectedAppInfoMap.remove(appInfo.getPkgName());
        }
    };

    public static void start(Activity context, int requestCode) {
        start(context, requestCode, new ArrayList<>(0));
    }

    public static void start(Activity context, int requestCode, List<Pkg> excludePkgs) {
        Bundle data = new Bundle();
        data.putParcelableArrayList(EXTRA_EXCLUDE_PKGS, new ArrayList<>(excludePkgs));
        ActivityUtils.startActivityForResult(context, AppPickerActivity.class, requestCode, data);
    }

    public static Intent getIntent(Context context, ArrayList<Pkg> excludePkgs) {
        Intent intent = new Intent(context, AppPickerActivity.class);
        Bundle data = new Bundle();
        data.putParcelableArrayList(EXTRA_EXCLUDE_PKGS, excludePkgs);
        intent.putExtras(data);
        return intent;
    }

    @Override
    protected int getTitleRes() {
        return github.tornaco.android.thanos.res.R.string.app_picker_title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedAppInfoMap.clear();
        excludePkgs.clear();

        resolveIntent();
    }

    private void resolveIntent() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_EXCLUDE_PKGS)) {
            ArrayList<Pkg> exclude = getIntent().getParcelableArrayListExtra(EXTRA_EXCLUDE_PKGS);
            if (exclude != null) {
                excludePkgs.addAll(exclude);
            }
        }
    }

    @Override
    protected void onSetupFab(ExtendedFloatingActionButton fab) {
        fab.show();
        fab.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK, new Intent().putParcelableArrayListExtra(
                    "apps",
                    Lists.newArrayList(selectedAppInfoMap.values())));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectedAppInfoMap.clear();
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return appItemClickListener;
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) {
                return Lists.newArrayList(new AppListModel(AppInfo.dummy()));
            }
            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                if (excludePkgs.contains(Pkg.fromAppInfo(appInfo))) {
                    return;
                }
                appInfo.setSelected(selectedAppInfoMap.containsKey(appInfo.getPkgName()));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            });
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        appListFilterAdapter = new CommonAppListFilterAdapter(onCreateAppItemViewClickListener(), true);
        return appListFilterAdapter;
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_picker, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_select_all == item.getItemId()) {
            toogleListSelection(true);
            return true;
        }
        if (R.id.action_un_select_all == item.getItemId()) {
            toogleListSelection(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toogleListSelection(boolean selected) {
        CollectionUtils.consumeRemaining(appListFilterAdapter.getListModels()
                , appListModel -> {
                    appListModel.appInfo.setSelected(selected);
                    appItemClickListener.onAppItemClick(appListModel.appInfo);
                });
        appListFilterAdapter.notifyDataSetChanged();
    }
}
