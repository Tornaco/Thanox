/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.PackageSetChangeListener;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.EditTextDialog;
import github.tornaco.android.thanos.widget.QuickDropdown;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;
import util.Consumer;

public class PackageSetListActivity extends CommonAppListFilterActivity {
    private static final int REQ_CODE_EDIT = 100;

    private final PackageSetChangeListener packageSetChangeListener = new PackageSetChangeListener() {
        @Override
        public void onPackageSetChanged(String pkgSetId) {
            super.onPackageSetChanged(pkgSetId);
            XLog.d("onPackageSetChanged...");
            viewModel.start();
        }
    };

    public static void start(Activity activity) {
        ActivityUtils.startActivity(activity, PackageSetListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThanosManager.from(this).getPkgManager().registerPackageSetChangeListener(packageSetChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThanosManager.from(this).getPkgManager().unRegisterPackageSetChangeListener(packageSetChangeListener);
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
        setTitle(getTitleRes());
        filterAnchor.setChipIconVisible(false);
    }

    @Override
    protected void onSetupSorter(Chip sorterAnchor) {
        super.onSetupSorter(sorterAnchor);
        sorterAnchor.setVisibility(View.GONE);
    }

    @Override
    protected int getTitleRes() {
        return github.tornaco.android.thanos.res.R.string.title_package_sets;
    }

    @Override
    protected void onSetupFab(ExtendedFloatingActionButton fab) {
        fab.hide();
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pkg_set_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_add == item.getItemId()) {
            onRequestAddPackageSet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRequestAddPackageSet() {
        EditTextDialog.show(
                thisActivity(),
                getString(github.tornaco.android.thanos.res.R.string.title_package_add_set),
                new Consumer<String>() {
                    @Override
                    public void accept(String content) {
                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        PackageSet set = ThanosManager.from(thisActivity()).getPkgManager()
                                .createPackageSet(content);
                        if (set != null) {
                            // Reload.
                            PackageSetEditorActivity.start(PackageSetListActivity.this, set.getId(), REQ_CODE_EDIT);
                            ToastUtils.ok(thisActivity());
                        } else {
                            ToastUtils.nook(thisActivity());
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.i("onActivityResult, resultCode=%s", resultCode);
        if (resultCode == RESULT_OK) {
            viewModel.start();
        }
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        return new CommonAppListFilterAdapter(
                // itemViewClickListener
                (appInfo, itemView) -> PackageSetEditorActivity.start(PackageSetListActivity.this, (String) appInfo.getObj(), REQ_CODE_EDIT),
                (view, model) -> {
                    AppInfo appInfo = model.appInfo;
                    boolean isEditable = appInfo.isSelected();
                    if (!isEditable) {
                        QuickDropdown.show(thisActivity(),
                                view,
                                // titleProvider
                                index -> {
                                    if (index == 0) {
                                        return getString(github.tornaco.android.thanos.res.R.string.title_package_delete_set);
                                    }
                                    return null;
                                },
                                id -> {
                                    if (id == 0) {
                                        ThanosManager.from(thisActivity())
                                                .getPkgManager()
                                                .removePackageSet((String) appInfo.getObj());
                                        viewModel.start();
                                    }
                                });
                    }
                });
    }


    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return index -> {
            ThanosManager thanos = ThanosManager.from(getApplicationContext());
            if (!thanos.isServiceInstalled()) {
                return Lists.newArrayListWithCapacity(0);
            }
            PackageManager pm = thanos.getPkgManager();
            List<PackageSet> packageSets = pm.getAllPackageSets(true);
            List<AppListModel> res = new ArrayList<>();
            String prebuiltBadgeStr = getString(github.tornaco.android.thanos.res.R.string.title_package_prebuilt_set);
            CollectionUtils.consumeRemaining(
                    packageSets,
                    packageSet -> {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setPkgName(packageSet.getId());
                        appInfo.setObj(packageSet.getId());
                        appInfo.setIconDrawable(github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_nothing);
                        appInfo.setAppLabel(packageSet.getLabel());
                        appInfo.setArg3(packageSet.getCreateAt());
                        appInfo.setSelected(packageSet.isPrebuilt());
                        int count = packageSet.getPackageCount();
                        appInfo.setArg1(count);

                        String description = getString(github.tornaco.android.thanos.res.R.string.title_package_count_set, String.valueOf(count));
                        if (packageSet.getDescription() != null) {
                            description = description + "\n" + packageSet.getDescription();
                        }

                        res.add(new AppListModel(
                                appInfo,
                                packageSet.isPrebuilt() ? prebuiltBadgeStr : null,
                                null,
                                description));
                    });
            // Sort by time.
            res.sort((o1, o2) -> -Long.compare(o1.appInfo.getArg3(), o2.appInfo.getArg3()));
            return res;
        };
    }
}
