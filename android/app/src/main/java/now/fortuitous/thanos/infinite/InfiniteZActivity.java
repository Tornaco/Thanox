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

package now.fortuitous.thanos.infinite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.infinite.AddPackageCallback;
import github.tornaco.android.thanos.core.app.infinite.EnableCallback;
import github.tornaco.android.thanos.core.app.infinite.LaunchPackageCallback;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.databinding.ActivityIniniteZAppsBinding;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.support.ThanoxAppContext;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.util.DialogUtils;
import github.tornaco.android.thanos.util.ToastUtils;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class InfiniteZActivity extends ThemeActivity {
    private static final int REQ_PICK_APPS = 0x100;

    private InfiniteZAppsViewModel viewModel;
    private ActivityIniniteZAppsBinding binding;

    @Override
    public Context getApplicationContext() {
        return new ThanoxAppContext(super.getApplicationContext());
    }


    public static void start(Context context) {
        ActivityUtils.startActivity(context, InfiniteZActivity.class);
    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniniteZAppsBinding.inflate(
                LayoutInflater.from(this), null, false);
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // List.
        binding.apps.setLayoutManager(new GridLayoutManager(this, 5));
        binding.apps.setAdapter(new InfiniteZAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                ThanosManager.from(getApplicationContext())
                        .getInfiniteZ()
                        .launchPackage(appInfo.getPkgName(), new LaunchPackageCallback() {
                            @Override
                            public void onSuccessMain() {

                            }

                            @Override
                            public void onErrorMain(String errorMessage, int errorCode) {

                            }
                        });
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }, this::showItemPopMenu));

        binding.swipe.setOnRefreshListener(this::refreshState);
        binding.swipe.setColorSchemeColors(getResources()
                .getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        onSetupSwitchBar(binding.switchBarContainer.switchBar);
    }

    private void refreshState() {
        viewModel.start();
    }


    private void showItemPopMenu(@NonNull View anchor, @NonNull AppListModel model) {
        AppInfo appInfo = model.appInfo;
        PopupMenu popupMenu = new PopupMenu(thisActivity(), anchor);
        popupMenu.inflate(R.menu.infinite_z_item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_uninstall) {
                onRequestUninstall(appInfo);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void onRequestUninstall(AppInfo appInfo) {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(getString(github.tornaco.android.thanos.res.R.string.common_menu_title_uninstall) + "\t" + appInfo.getAppLabel())
                .setMessage(getString(github.tornaco.android.thanos.res.R.string.feature_message_infinite_z_uninstall, appInfo.getAppLabel()))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> viewModel.uninstall(appInfo, this::onUninstallSuccess, this::onUninstallFail))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                })
                .setCancelable(false)
                .show();
    }

    private void onUninstallSuccess() {
        ToastUtils.ok(thisActivity());
    }

    private void onUninstallFail(String errorMessage) {
        DialogUtils.showError(thisActivity(), errorMessage);
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.setChecked(ThanosManager.from(thisActivity()).getInfiniteZ().isEnabled());
        switchBar.addOnSwitchChangeListener((switchView, isChecked) -> {
            boolean isEnable = ThanosManager.from(thisActivity()).getInfiniteZ().isEnabled();
            if (isChecked && !isEnable) {
                requestEnableIZDialog(this::enableIZ, () -> switchBar.setChecked(false));
            } else if (!isChecked && isEnable) {
                requestDisableIZDialog(this::disableIZ, () -> switchBar.setChecked(true));
            }
        });
    }

    private void requestEnableIZDialog(Runnable onYes, Runnable onNo) {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.feature_title_infinite_z)
                .setMessage(github.tornaco.android.thanos.res.R.string.feature_message_infinite_z_enable)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> onYes.run())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> onNo.run())
                .setCancelable(false)
                .show();
    }

    private void requestDisableIZDialog(Runnable onYes, Runnable onNo) {
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(github.tornaco.android.thanos.res.R.string.feature_title_infinite_z)
                .setMessage(github.tornaco.android.thanos.res.R.string.feature_message_infinite_z_disable)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> onYes.run())
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> onNo.run())
                .setCancelable(false)
                .show();
    }

    private void enableIZ() {
        ModernProgressDialog p = new ModernProgressDialog(thisActivity());
        p.setMessage(getString(github.tornaco.android.thanos.res.R.string.common_text_wait_a_moment));
        p.show();
        ThanosManager.from(getApplicationContext()).getInfiniteZ()
                .setEnabled(true, new EnableCallback() {
                    @Override
                    public void onSuccessMain(int userId) {
                        p.dismiss();
                        refreshState();
                    }

                    @Override
                    public void onErrorMain(String errorMessage, int errorCode) {
                        XLog.e("Enable infiniteZ fail: %s %s", errorCode, errorMessage);
                        DialogUtils.showMessage(thisActivity(), null, getString(github.tornaco.android.thanos.res.R.string.common_generic_error));
                        p.dismiss();
                        refreshState();
                    }
                });
    }

    private void disableIZ() {
        ModernProgressDialog p = new ModernProgressDialog(thisActivity());
        p.setMessage(getString(github.tornaco.android.thanos.res.R.string.common_text_wait_a_moment));
        p.show();
        ThanosManager.from(getApplicationContext()).getInfiniteZ()
                .setEnabled(false, new EnableCallback() {
                    @Override
                    public void onSuccessMain(int userId) {
                        p.dismiss();
                        refreshState();
                    }

                    @Override
                    public void onErrorMain(String errorMessage, int errorCode) {
                        XLog.e("Disable infiniteZ fail: %s %s", errorCode, errorMessage);
                        DialogUtils.showMessage(thisActivity(), null, getString(github.tornaco.android.thanos.res.R.string.common_generic_error));
                        p.dismiss();
                        refreshState();
                    }
                });
    }


    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.start();

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_PICK_APPS == requestCode && resultCode == RESULT_OK && data != null && data.hasExtra("apps")) {
            List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");
            ModernProgressDialog p = new ModernProgressDialog(thisActivity());
            p.setMessage(getString(github.tornaco.android.thanos.res.R.string.common_text_wait_a_moment));
            p.show();
            CollectionUtils.consumeRemaining(appInfos, appInfo -> ThanosManager.from(getApplicationContext())
                    .getInfiniteZ()
                    .addPackage(appInfo.getPkgName(), new AddPackageCallback() {
                        @Override
                        public void onSuccessMain(int userId) {
                            new Handler().postDelayed(p::dismiss, 2400);
                            viewModel.start();
                        }

                        @Override
                        public void onErrorMain(String errorMessage, int errorCode) {
                            DialogUtils.showMessage(thisActivity(), String.valueOf(errorCode), errorMessage);
                            p.dismiss();
                        }
                    }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.infinite_z, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_add == item.getItemId()) {
            onRequestAddApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRequestAddApp() {
        ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<Pkg> exclude = new ArrayList<>(0);
            AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
        });
    }

    public static InfiniteZAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(InfiniteZAppsViewModel.class);
    }
}
