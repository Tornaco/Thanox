package now.fortuitous.thanos.launchother;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
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
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.support.ContextExtKt;
import github.tornaco.android.thanos.util.ActivityUtils;
import util.CollectionUtils;

public class LaunchOtherAppListActivity extends CommonAppListFilterActivity {
    private CommonAppListFilterAdapter appListFilterAdapter;

    private String currentModeFilter = null;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, LaunchOtherAppListActivity.class);
    }

    @Override
    public boolean isADVF() {
        return true;
    }

    @Override
    protected String getTitleString() {
        return getString(github.tornaco.android.thanos.res.R.string.launch_other_app);
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return this::showModeSelectionDialog;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showModeSelectionDialog(AppInfo appInfo) {
        String[] items = Lists.newArrayList(
                        getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_allow),
                        getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ask),
                        getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ignore),
                        getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_allow_listed)
                )
                .toArray(new String[0]);
        int currentMode = Integer.parseInt(appInfo.getStr());
        int currentSelection = 0;
        if (currentMode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK) {
            currentSelection = 1;
        }
        if (currentMode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE) {
            currentSelection = 2;
        }
        if (currentMode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED) {
            currentSelection = 3;
        }
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(appInfo.getAppLabel())
                .setSingleChoiceItems(items, currentSelection, (dialog, which) -> {
                    dialog.dismiss();
                    int newMode = getNewModeFromDialogWhich(which);
                    ContextExtKt.withThanos(thisActivity(), thanosManager -> {
                        thanosManager.getActivityStackSupervisor().setLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo), newMode);
                        return null;
                    });

                    appInfo.setStr(String.valueOf(newMode));
                    appListFilterAdapter.notifyDataSetChanged();
                })
                .setCancelable(true)
                .show();
    }

    private static int getNewModeFromDialogWhich(int which) {
        int newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW;
        if (which == 1) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK;
        }
        if (which == 2) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE;
        }
        if (which == 3) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED;
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
                if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_green;
                }
                if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_remix_question_fill_amber;
                }
                if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_forbid_2_fill_red;
                }
                if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED) {
                    return github.tornaco.thanos.android.ops.R.drawable.module_ops_ic_checkbox_circle_fill_light_green;
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
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_allow));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ask));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ignore));
        menuItemList.add(getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_all));

        currentModeFilter = getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_all);
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
        if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW_LISTED) {
            AllowListActivity.start(this, appInfo);
        } else {
            int finalNewMode = getQuickSwitchMode(mode);
            ThanosManager.from(thisActivity())
                    .ifServiceInstalled(thanosManager -> thanosManager.getActivityStackSupervisor()
                            .setLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo), finalNewMode));
            appInfo.setStr(String.valueOf(finalNewMode));
            appListFilterAdapter.notifyItemChanged(itemIndex);
        }
    }

    private static int getQuickSwitchMode(int mode) {
        int newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW;
        if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK;
        } else if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE;
        } else if (mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE) {
            newMode = ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW;
        }
        return newMode;
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        Context context = getApplicationContext();
        ThanosManager thanos = ThanosManager.from(context);
        ActivityStackSupervisor stackSupervisor = thanos.getActivityStackSupervisor();
        AppListItemDescriptionComposer composer = new AppListItemDescriptionComposer(thisActivity());
        return index -> {
            if (!thanos.isServiceInstalled())
                return Lists.newArrayListWithCapacity(0);

            List<AppInfo> installed = thanos.getPkgManager().getInstalledPkgsByPackageSetId(index.pkgSetId);
            List<AppListModel> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(installed, appInfo -> {
                appInfo.setStr(String.valueOf(stackSupervisor.getLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo))));
                res.add(new AppListModel(appInfo, null, null, composer.getAppItemDescription(appInfo)));
            });
            Collections.sort(res);

            if (currentModeFilter == null || currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.module_ops_mode_all))) {
                return res;
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_allow))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW;
                }).collect(Collectors.toList());
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ignore))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE;
                }).collect(Collectors.toList());
            } else if (currentModeFilter.equals(getString(github.tornaco.android.thanos.res.R.string.launch_other_app_options_ask))) {
                return res.stream().filter(model -> {
                    String payload = model.appInfo.getStr();
                    int mode = Integer.parseInt(payload);
                    return mode == ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK;
                }).collect(Collectors.toList());
            } else {
                return res;
            }
        };
    }

    @Override
    protected void onInflateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_launch_other_app_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_select_all_allow == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.module_ops_mode_allow_all)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(ActivityStackSupervisor.LaunchOtherAppPkgSetting.ALLOW)).show();
            return true;
        }
        if (R.id.action_select_all_ask == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.module_ops_mode_ask_all)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(ActivityStackSupervisor.LaunchOtherAppPkgSetting.ASK)).show();
            return true;
        }
        if (R.id.action_un_select_all_ignore == item.getItemId()) {
            new MaterialAlertDialogBuilder(thisActivity())
                    .setTitle(github.tornaco.android.thanos.res.R.string.module_ops_mode_ignore)
                    .setMessage(github.tornaco.android.thanos.res.R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> selectAll(ActivityStackSupervisor.LaunchOtherAppPkgSetting.IGNORE)).show();
            return true;
        }
        if (R.id.action_rule == item.getItemId()) {
            LaunchOtherAppRuleActivity.start(thisActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectAll(int mode) {
        CollectionUtils.consumeRemaining(appListFilterAdapter.getListModels(), model -> {
            AppInfo appInfo = model.appInfo;
            ThanosManager.from(getApplicationContext())
                    .ifServiceInstalled(thanosManager -> thanosManager.getActivityStackSupervisor()
                            .setLaunchOtherAppSetting(Pkg.fromAppInfo(appInfo), mode));
        });
        viewModel.start();
    }

    @Override
    protected boolean getSwitchBarCheckState() {
        return ThanosManager.from(this).isServiceInstalled()
                && ThanosManager.from(this).getActivityStackSupervisor().isLaunchOtherAppBlockerEnabled();
    }

    @Override
    protected void onSwitchBarCheckChanged(MaterialSwitch switchBar, boolean isChecked) {
        super.onSwitchBarCheckChanged(switchBar, isChecked);
        ThanosManager.from(this).getActivityStackSupervisor().setLaunchOtherAppBlockerEnabled(isChecked);
    }
}
