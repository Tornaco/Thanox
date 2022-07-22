package github.tornaco.android.thanos.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.elvishew.xlog.XLog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.util.ObjectToStringUtils;
import github.tornaco.android.thanos.core.util.Optional;
import github.tornaco.android.thanos.dashboard.DashboardAdapter;
import github.tornaco.android.thanos.dashboard.OnTileClickListener;
import github.tornaco.android.thanos.dashboard.OnTileLongClickListener;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.databinding.FragmentPluginBinding;
import github.tornaco.android.thanos.feature.access.AppFeatureManager;
import github.tornaco.android.thanos.util.DialogUtils;
import github.tornaco.android.thanos.util.IntentUtils;
import github.tornaco.android.thanos.widget.ModernProgressDialog;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

@RuntimePermissions
public class PluginFragment extends NavFragment implements NavViewModel.PluginInstallResUi,
        OnTileClickListener,
        OnTileLongClickListener,
        NavViewModel.PluginUnInstallResUi, DashboardAdapter.OnTileSwitchChangeListener {

    private final static int REQUEST_CODE_PLUGIN_FILE_PICK = 0x200;

    private FragmentPluginBinding pluginBinding;
    private NavViewModel navViewModel;

    private ModernProgressDialog installDialog;
    private ModernProgressDialog uninstallDialog;

    private boolean isFABOpen = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        pluginBinding = FragmentPluginBinding.inflate(inflater, container, false);
        setupView();
        setupViewModel();
        return pluginBinding.getRoot();
    }

    private void setupView() {
        pluginBinding.features.setLayoutManager(new GridLayoutManager(getContext(), 1));
        pluginBinding.features.setAdapter(new DashboardAdapter(this, this, this));

        pluginBinding.fab.setOnClickListener(v -> {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    toggleFabMenu();
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
        });

        pluginBinding.fabFile.setOnClickListener(v -> {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    PluginFragmentPermissionRequester.installPluginRequestedChecked(PluginFragment.this);
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
        });

        pluginBinding.fabMarket.setOnClickListener(v -> {
            AppFeatureManager.INSTANCE.withSubscriptionStatus(requireContext(), isSubscribed -> {
                if (isSubscribed) {
                    PluginMarketActivity.start(getActivity());
                } else {
                    AppFeatureManager.INSTANCE.showDonateIntroDialog(requireActivity());
                }
                return null;
            });
        });

        closeFABMenu();
    }

    private void toggleFabMenu() {
        if (isFABOpen) closeFABMenu();
        else showFABMenu();
    }

    private void showFABMenu() {
        if (isFABOpen) return;
        isFABOpen = true;
        pluginBinding.fabMarket.show(new ExtendedFloatingActionButton.OnChangedCallback() {
            @Override
            public void onShown(ExtendedFloatingActionButton extendedFab) {
                super.onShown(extendedFab);
                pluginBinding.fabMarket.animate().translationY(-getResources().getDimension(R.dimen.fab_standard_60));
            }
        });
        pluginBinding.fabFile.show(new ExtendedFloatingActionButton.OnChangedCallback() {
            @Override
            public void onShown(ExtendedFloatingActionButton extendedFab) {
                super.onShown(extendedFab);
                pluginBinding.fabFile.animate().translationY(-getResources().getDimension(R.dimen.fab_standard_120));
            }
        });
    }

    private void closeFABMenu() {
        if (!isFABOpen) return;
        isFABOpen = false;
        pluginBinding.fabMarket.animate().translationY(0);
        pluginBinding.fabFile.animate().translationY(0);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            pluginBinding.fabFile.hide();
            pluginBinding.fabMarket.hide();
        }, 300);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewModel() {
        navViewModel = obtainViewModel(getActivity());
        pluginBinding.setViewmodel(navViewModel);
        pluginBinding.executePendingBindings();
    }

    @Override
    public void onResume() {
        super.onResume();
        navViewModel.loadPluginFeatures();
    }

    @RequiresPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void installPluginRequested() {
        IntentUtils.startFilePickerActivityForRes(this, REQUEST_CODE_PLUGIN_FILE_PICK);
    }

    private void onPluginFilePickRequestResult(@Nullable Intent data) {
        if (data == null) {
            XLog.e("No data.");
            return;
        }

        if (getActivity() == null) return;

        Uri uri = data.getData();
        if (uri == null) {
            Toast.makeText(getActivity(), "uri == null", Toast.LENGTH_LONG).show();
            XLog.e("No uri.");
            return;
        }

        Optional.ofNullable(getActivity())
                .ifPresent(fragmentActivity -> obtainViewModel(fragmentActivity)
                        .installPlugin(uri, PluginFragment.this));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.d("onActivityResult: %s %s %s", requestCode, resultCode, ObjectToStringUtils.intentToString(data));
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PLUGIN_FILE_PICK) {
            onPluginFilePickRequestResult(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PluginFragmentPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static NavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(NavViewModel.class);
    }

    @Override
    public void showInstallSuccess(@NonNull InstalledPlugin plugin) {
        if (getActivity() == null) return;
        dismissProgressDialog();
        navViewModel.loadPluginFeatures();
    }

    @Override
    public void showInstallFail(String message) {
        if (getActivity() == null) return;
        dismissProgressDialog();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInstallFail(Throwable error) {
        if (getActivity() == null) return;
        dismissProgressDialog();
        DialogUtils.showError(requireActivity(), error);
    }

    @Override
    public void showInstallBegin() {
        showInstallProgressDialog();
    }

    private void showInstallProgressDialog() {
        if (getActivity() == null) return;
        if (installDialog == null) {
            installDialog = new ModernProgressDialog(getActivity());
            installDialog.setTitle(R.string.dialog_title_plugin_installing);
        }
        if (!installDialog.isShowing()) {
            installDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (getActivity() == null) return;
        if (installDialog != null) {
            installDialog.dismiss();
        }
        if (uninstallDialog != null) {
            uninstallDialog.dismiss();
        }
    }

    private void showUnInstallProgressDialog() {
        if (getActivity() == null) return;
        if (uninstallDialog == null) {
            uninstallDialog = new ModernProgressDialog(getActivity());
            uninstallDialog.setTitle(R.string.dialog_title_plugin_uninstalling);
        }
        if (!uninstallDialog.isShowing()) {
            uninstallDialog.show();
        }
    }

    @Override
    public void onClick(@NonNull Tile tile) {
        InstalledPlugin plugin = (InstalledPlugin) tile.getPayload();
        Nitro.launchMainActivity(requireActivity(), plugin);
    }

    @Override
    public void onLongClick(@NonNull Tile tile, @NonNull View view) {
        InstalledPlugin plugin = (InstalledPlugin) tile.getPayload();
        showPluginPopMenu(plugin, view);
    }

    private void showPluginPopMenu(InstalledPlugin plugin, @NonNull View view) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), view);
        popupMenu.inflate(R.menu.plugin_item_pop_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_uninstall_plugin) {
                showUnInstallPluginConfirmDialog(plugin);
                return true;
            }
            if (item.getItemId() == R.id.action_view_plugin_details) {
                showPluginDetailsDialog(plugin);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showPluginDetailsDialog(InstalledPlugin plugin) {
        if (getActivity() == null) return;
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(plugin.getLabel())
                .setMessage(String.format("%s\n%s\n%s\nwithHooks? %s\nstable? %s\ncallback:%s",
                        plugin.getVersionName(),
                        plugin.getDescription(),
                        plugin.getPackageName(),
                        plugin.isWithHooks(),
                        plugin.isStable(),
                        plugin.getStatusCallableClass()))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showUnInstallPluginConfirmDialog(InstalledPlugin plugin) {
        if (getActivity() == null) return;
        new MaterialAlertDialogBuilder(getActivity())
                .setMessage(R.string.dialog_title_plugin_uninstall_confirm)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        navViewModel.uninstallPlugin(plugin, PluginFragment.this))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void showUnInstallBegin() {
        showUnInstallProgressDialog();
    }

    @Override
    public void showUnInstallSuccess(@NonNull InstalledPlugin plugin) {
        if (getActivity() == null) return;
        dismissProgressDialog();
        navViewModel.loadPluginFeatures();
    }

    @Override
    public void showUnInstallFail(String message) {
        if (getActivity() == null) return;
        dismissProgressDialog();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSwitchChange(@NonNull Tile tile, boolean checked) {
        InstalledPlugin plugin = (InstalledPlugin) tile.getPayload();
        navViewModel.setPluginActive(plugin, checked);
    }
}
