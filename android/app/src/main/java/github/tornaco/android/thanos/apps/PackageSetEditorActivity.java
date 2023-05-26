package github.tornaco.android.thanos.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;
import util.ObjectsUtils;

public class PackageSetEditorActivity extends CommonAppListFilterActivity {

    private static final String KEY_PACKAGE_SET_ID = "package_set_id";
    private static final int REQ_PICK_APPS = 0x100;

    private PackageSet packageSet;
    private CommonAppListFilterAdapter adapter;

    private boolean changed = false;

    public static void start(Activity activity, String id, int reqCode) {
        Bundle data = new Bundle();
        data.putString(KEY_PACKAGE_SET_ID, id);
        ActivityUtils.startActivityForResult(activity, PackageSetEditorActivity.class, reqCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        resolveIntent();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (closeSearch()) {
            return;
        }
        int resultCode = changed ? RESULT_OK : RESULT_CANCELED;
        XLog.i("onBackPressed, resultCode: %s", resultCode);
        setResult(resultCode, new Intent());
        finish();
    }

    private void resolveIntent() {
        Optional.ofNullable(getIntent()).ifPresent(intent -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            PackageManager pm = thanos.getPkgManager();
            packageSet = pm.getPackageSetById(intent.getStringExtra(KEY_PACKAGE_SET_ID), true);
        });
    }

    @Override
    protected void onSetupSwitchBar(SwitchBar switchBar) {
        super.onSetupSwitchBar(switchBar);
        switchBar.hide();
    }

    @Override
    protected void onSetupFilter(Chip filterAnchor) {
        super.onSetupFilter(filterAnchor);
        filterAnchor.setVisibility(View.GONE);
        setTitle(getTitleString());
    }

    @Override
    protected void onSetupFab(ExtendedFloatingActionButton fab) {
        if (packageSet.isPrebuilt()) {
            fab.hide();
            return;
        }
        fab.setText(null);
        fab.setIconResource(R.drawable.module_common_ic_add_fill);
        fab.show();
        fab.setOnClickListener(
                v -> {
                    ArrayList<Pkg> exclude = packageSet.getPkgNames().stream().map(Pkg::systemUserPkg).collect(Collectors.toCollection(Lists::newArrayList));
                    AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
                });
    }

    @Override
    protected String getTitleString() {
        if (packageSet != null) {
            return packageSet.getLabel();
        }
        return super.getTitleString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_PICK_APPS == requestCode && resultCode == RESULT_OK && data != null && data
                .hasExtra("apps")) {
            List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");

            if (!CollectionUtils.isNullOrEmpty(appInfos)) {
                ThanosManager thanos = ThanosManager.from(getApplicationContext());
                PackageManager pm = thanos.getPkgManager();
                for (AppInfo appInfo : appInfos) {
                    packageSet.addPackage(appInfo.getPkgName());
                    pm.addToPackageSet(appInfo.getPkgName(), packageSet.getId());
                }
                viewModel.start();
                changed = true;
            }
        }
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_package_set_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_rename == item.getItemId()) {
            EditTextDialog.show(thisActivity(), getString(R.string.common_menu_title_rename), packageSet.getLabel(),
                    s -> {
                        ThanosManager.from(thisActivity()).getPkgManager().updatePackageSetLabel(s, packageSet.getId());
                        packageSet.setLabel(s);
                        setTitle(getTitleString());
                    });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            if (packageSet == null || packageSet.getPackageCount() == 0) {
                return Lists.newArrayList();
            }
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) {
                return Lists.newArrayList();
            }
            List<AppListModel> res = new ArrayList<>();
            PackageManager pm = thanos.getPkgManager();
            for (String pkg : packageSet.getPkgNames()) {
                AppInfo appInfo = pm.getAppInfo(pkg);
                if (appInfo == null) {
                    continue;
                }
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            }
            Collections.sort(res);
            return res;
        };
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        adapter = new CommonAppListFilterAdapter((appInfo, itemView) -> {
            AppDetailsActivity.start(thisActivity(), appInfo);
        }, (itemView, model) -> {
            AppInfo appInfo = model.appInfo;
            if (packageSet.isPrebuilt()) {
                return;
            }
            QuickDropdown.show(
                    thisActivity(),
                    itemView,
                    index -> {
                        if (index == 0) {
                            return getString(R.string.title_package_delete_set);
                        } else if (index == 1) {
                            return getString(R.string.feature_title_apps_manager);
                        }
                        return null;
                    },
                    id -> {
                        if (id == 0) {
                            ThanosManager.from(thisActivity())
                                    .getPkgManager()
                                    .removeFromPackageSet(appInfo.getPkgName(), packageSet.getId());
                            packageSet.removePackage(appInfo.getPkgName());
                            adapter.removeItem(
                                    input -> ObjectsUtils.equals(input.appInfo.getPkgName(), appInfo.getPkgName()));
                            changed = true;
                        } else if (id == 1) {
                            AppDetailsActivity.start(thisActivity(), appInfo);
                        }
                    });
        });
        return adapter;
    }
}
