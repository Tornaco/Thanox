package now.fortuitous.thanos.privacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.google.android.material.button.MaterialSplitButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListItemDescriptionComposer;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterAdapter;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.common.StateImageProvider;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.secure.PrivacyManager;
import github.tornaco.android.thanos.support.ContextExtKt;
import github.tornaco.android.thanos.util.ActivityUtils;
import util.CollectionUtils;

public class SensorOffAppListActivity extends CommonAppListFilterActivity {
    private CommonAppListFilterAdapter appListFilterAdapter;

    private String currentModeFilter = null;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, SensorOffAppListActivity.class);
    }

    @Override
    public boolean isADVF() {
        return true;
    }

    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.sensor_off);
    }

    @Nullable
    @Override
    protected String provideFeatureDescText() {
        return getString(github.tornaco.android.thanos.res.R.string.sensor_off_summary);
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return this::showModeSelectionDialog;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showModeSelectionDialog(AppInfo appInfo) {
        String[] items = Lists.newArrayList(
                        getString(github.tornaco.android.thanos.res.R.string.sensor_off_default),
                        getString(github.tornaco.android.thanos.res.R.string.sensor_off_on_start),
                        getString(github.tornaco.android.thanos.res.R.string.sensor_off_always)
                )
                .toArray(new String[0]);
        int currentMode = Integer.parseInt(appInfo.getStr());
        int currentSelection = 0;
        if (currentMode == PrivacyManager.SensorOffSettings.ON_START) {
            currentSelection = 1;
        }
        if (currentMode == PrivacyManager.SensorOffSettings.ALWAYS) {
            currentSelection = 2;
        }
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(appInfo.getAppLabel())
                .setSingleChoiceItems(items, currentSelection, (dialog, which) -> {
                    dialog.dismiss();
                    int newMode = getNewModeFromDialogWhich(which);
                    ContextExtKt.withThanos(thisActivity(), thanosManager -> {
                        thanosManager.getPrivacyManager().setSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo), newMode);
                        return null;
                    });

                    appInfo.setStr(String.valueOf(newMode));
                    appListFilterAdapter.notifyDataSetChanged();
                })
                .setCancelable(true)
                .show();
    }

    private static int getNewModeFromDialogWhich(int which) {
        int newMode = PrivacyManager.SensorOffSettings.DEFAULT;
        if (which == 1) {
            newMode = PrivacyManager.SensorOffSettings.ON_START;
        }
        if (which == 2) {
            newMode = PrivacyManager.SensorOffSettings.ALWAYS;
        }
        return newMode;
    }

    @Override
    protected CommonAppListFilterAdapter onCreateCommonAppListFilterAdapter() {
        appListFilterAdapter = new CommonAppListFilterAdapter(onCreateAppItemViewClickListener(), new StateImageProvider() {
            @Override
            public int provideImageRes(@NonNull AppListModel model) {
                String payload = model.appInfo.getStr();
                int mode = Integer.parseInt(payload);
                if (mode == PrivacyManager.SensorOffSettings.DEFAULT) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_green;
                }
                if (mode == PrivacyManager.SensorOffSettings.ON_START) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_grey;
                }
                if (mode == PrivacyManager.SensorOffSettings.ALWAYS) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_forbid_2_fill_red;
                }
                return 0;
            }

            @Override
            public View.OnClickListener provideOnClickListener(@NonNull AppListModel model, int itemIndex) {
                return v -> {
                    AppInfo appInfo = model.appInfo;
                    quickSwitch(appInfo, itemIndex);
                };
            }
        }, false);
        return appListFilterAdapter;
    }

    @Override
    protected boolean hasCustomFilter() {
        return true;
    }

    @Override
    @SuppressLint("RestrictedApi")
    protected void onSetupCustomFilter(MaterialSplitButton filterAnchor) {
        List<String> menuItemList = new ArrayList<>();
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.sensor_off_default));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.sensor_off_on_start));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.sensor_off_always));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.sensor_off_all));

        currentModeFilter = getString(github.tornaco.android.thanos.res.R.string.sensor_off_all);
        Button leading = filterAnchor.findViewById(github.tornaco.android.thanos.module.common.R.id.leading_button);
        leading.setText(currentModeFilter);

        leading.setOnClickListener(view -> {
            MenuBuilder menuBuilder = new MenuBuilder(this);
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this, menuBuilder, view);
            menuPopupHelper.setForceShowIcon(true);
            for (int i = 0; i < menuItemList.size(); i++) {
                String modeFilter = menuItemList.get(i);
                MenuItem menuItem = menuBuilder.add(
                        1000,
                        i,
                        Menu.NONE,
                        modeFilter);
            }
            menuBuilder.setCallback(new MenuBuilder.Callback() {
                @Override
                public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                    int index = item.getItemId();
                    currentModeFilter = menuItemList.get(index);
                    leading.setText(currentModeFilter);

                    viewModel.start();
                    return true;
                }

                @Override
                public void onMenuModeChange(@NonNull MenuBuilder menu) {
                    // Noop
                }
            });
            menuPopupHelper.show();
        });
    }

    private void quickSwitch(AppInfo appInfo, int itemIndex) {
        String payload = appInfo.getStr();
        int mode = Integer.parseInt(payload);
        int finalNewMode = getQuickSwitchMode(mode);
        ThanosManager.from(thisActivity())
                .ifServiceInstalled(thanosManager -> thanosManager.getPrivacyManager()
                        .setSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo), finalNewMode));
        appInfo.setStr(String.valueOf(finalNewMode));
        appListFilterAdapter.notifyItemChanged(itemIndex);
    }

    private static int getQuickSwitchMode(int mode) {
        int newMode = PrivacyManager.SensorOffSettings.DEFAULT;
        if (mode == PrivacyManager.SensorOffSettings.DEFAULT) {
            newMode = PrivacyManager.SensorOffSettings.ON_START;
        } else if (mode == PrivacyManager.SensorOffSettings.ON_START) {
            newMode = PrivacyManager.SensorOffSettings.ALWAYS;
        }
        return newMode;
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        Context context = getApplicationContext();
        ThanosManager thanos = ThanosManager.from(context);
        PrivacyManager privacyManager = thanos.getPrivacyManager();
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            if (!thanos.isServiceInstalled())
                return Lists.newArrayListWithCapacity(0);

            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setStr(String.valueOf(privacyManager.getSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo))));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            });
            Collections.sort(res);

            if (currentModeFilter == null || currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.sensor_off_default))) {
                return res;
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.sensor_off_default))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == PrivacyManager.SensorOffSettings.DEFAULT;
                }).collect(Collectors.toList());
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.sensor_off_on_start))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == PrivacyManager.SensorOffSettings.ON_START;
                }).collect(Collectors.toList());
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.sensor_off_always))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == PrivacyManager.SensorOffSettings.ALWAYS;
                }).collect(Collectors.toList());
            } else {
                return res;
            }
        };
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sensor_off_app_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_select_all_default == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.sensor_off_default)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(PrivacyManager.SensorOffSettings.DEFAULT)).show();
            return true;
        }
        if (R.id.action_select_all_on_start == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.sensor_off_on_start)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(PrivacyManager.SensorOffSettings.ON_START)).show();
            return true;
        }
        if (R.id.action_select_all_always == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.sensor_off_always)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(PrivacyManager.SensorOffSettings.ALWAYS)).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectAll(int mode) {
        CollectionUtils.consumeRemaining(appListFilterAdapter.getListModels(), model -> {
            AppInfo appInfo = model.appInfo;
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> thanosManager.getPrivacyManager()
                            .setSensorOffSettingsForPackage(Pkg.fromAppInfo(appInfo), mode));
        });
        viewModel.start();
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled()
                && ThanosManager.from(this).getPrivacyManager().isSensorOffEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getPrivacyManager().setSensorOffEnabled(isChecked);
    }
}
