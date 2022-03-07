package github.tornaco.android.thanos.power;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BaseFragment;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.apps.AppDetailsActivity;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.databinding.ActivitySmartFreezeAppsBinding;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.ModernProgressDialog;

public class SmartFreezeAppListFragment extends BaseFragment {
    private static final String ARG_PKG_SET = "arg.pkg.set.id";

    private SmartFreezeAppsViewModel viewModel;
    private ActivitySmartFreezeAppsBinding binding;

    public static SmartFreezeAppListFragment newInstance(@Nullable String pkgSetId) {
        SmartFreezeAppListFragment freezeAppListFragment = new SmartFreezeAppListFragment();
        Bundle data = new Bundle();
        data.putString(ARG_PKG_SET, pkgSetId);
        freezeAppListFragment.setArguments(data);
        return freezeAppListFragment;
    }

    @Nullable
    @Override
    @Verify
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySmartFreezeAppsBinding.inflate(inflater, container, false);
        createOptionsMenu();
        setupView();
        Bundle data = getArguments();
        String pkgSetId = Objects.requireNonNull(data, "Missing arg: " + ARG_PKG_SET).getString(ARG_PKG_SET, null);
        setupViewModel(pkgSetId);
        return binding.getRoot();
    }

    private void setupView() {
        binding.toolbar.setNavigationIcon(R.drawable.module_common_ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        // Search.
        binding.searchView.setOnQueryTextListener(
                new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        viewModel.setSearchText(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        viewModel.setSearchText(newText);
                        return true;
                    }
                });

        binding.searchView.setOnSearchViewListener(
                new MaterialSearchView.SearchViewListener() {
                    @Override
                    public void onSearchViewShown() {
                        binding.toolbarLayout.setTitleEnabled(false);
                        binding.appbar.setExpanded(false, true);
                    }

                    @Override
                    public void onSearchViewClosed() {
                        viewModel.clearSearchText();
                        binding.toolbarLayout.setTitleEnabled(true);
                    }
                });

        // List.
        binding.apps.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        binding.apps.setAdapter(new SmartFreezeAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                PackageManager pm = ThanosManager.from(requireContext())
                        .getPkgManager();
                pm.launchSmartFreezePkg(Pkg.fromAppInfo(appInfo));
                appInfo.setState(AppInfo.STATE_ENABLED);
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }, this::showItemPopMenu));

        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

    }

    @Verify
    private void showItemPopMenu(@NonNull View anchor, @NonNull AppListModel model) {
        AppInfo appInfo = model.appInfo;
        PopupMenu popupMenu = new PopupMenu(requireActivity(), anchor);
        popupMenu.inflate(R.menu.smart_freeze_item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_remove_from_smart_freeze) {
                viewModel.disableSmartFreeze(model);
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut) {
                ShortcutHelper.addShortcut(requireActivity(), appInfo);
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut_apk) {
                if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                    Toast.makeText(requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                    return false;
                }
                onRequestShortcutStubApk(appInfo);
                return true;
            }
            if (item.getItemId() == R.id.action_apps_manager) {
                if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                    Toast.makeText(requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                    return false;
                }
                AppDetailsActivity.start(requireContext(), appInfo);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }


    @Verify
    private void setupViewModel(String pkgSetId) {
        viewModel = obtainViewModel(requireActivity());
        viewModel.setPkgSetId(pkgSetId);

        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        binding.executePendingBindings();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // A little delay make ui looks smoother
        postOnUiDelayed(() -> viewModel.start(), 100);
    }

    private final ActivityResultLauncher<Intent> pickApps
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().hasExtra("apps")) {
                    List<AppInfo> appInfos = result.getData().getParcelableArrayListExtra("apps");
                    PackageSet packageSet = viewModel.getPackageSet();
                    if (packageSet == null) return;
                    if (packageSet.isPrebuilt()) {
                        onRequestAddToSmartFreezeList(appInfos, false);
                    } else {
                        onRequestAddToSmartFreezeListAskIfAddToPkgSet(appInfos);
                    }
                }
            });

    private void onRequestAddToSmartFreezeList(List<AppInfo> appInfos, boolean alsoAddToPkgSet) {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.common_text_wait_a_moment);
        viewModel.addToSmartFreezeList(appInfos,
                alsoAddToPkgSet,
                appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())),
                success -> {
                    progress.dismiss();
                    viewModel.start();
                });
        progress.show();
    }

    private void onRequestAddToSmartFreezeListAskIfAddToPkgSet(List<AppInfo> appInfos) {
        ModernAlertDialog dialog = new ModernAlertDialog(requireActivity());
        dialog.setDialogTitle(getString(R.string.common_fab_title_add));
        dialog.setDialogMessage(getString(R.string.message_do_you_want_to_add_apps_to_pkg_set));
        dialog.setCancelable(false);
        dialog.setNegative(getString(android.R.string.no));
        dialog.setOnNegative(() -> onRequestAddToSmartFreezeList(appInfos, false));
        dialog.setPositive(getString(R.string.common_fab_title_add));
        dialog.setOnPositive(() -> onRequestAddToSmartFreezeList(appInfos, true));
        dialog.show();
    }

    private void createOptionsMenu() {
        binding.toolbar.inflateMenu(R.menu.smart_freeze_menu);
        MenuItem item = binding.toolbar.getMenu().findItem(github.tornaco.android.thanos.module.common.R.id.action_search);
        binding.searchView.setMenuItem(item);
        binding.toolbar.setOnMenuItemClickListener(this::handleOptionsItemSelected);
    }

    @Verify
    private boolean handleOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            SmartFreezeSettingsActivity.start(requireActivity());
            return true;
        }
        if (R.id.action_add == item.getItemId()) {
            onRequestAddNewApps();
            return true;
        }
        if (R.id.action_enable_all_smart_freeze == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                Toast.makeText(requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.menu_title_smart_app_freeze_enable_all_smart_freeze_apps)
                    .setMessage(R.string.menu_desc_smart_app_freeze_enable_all_smart_freeze_apps)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllSmartFreezeApps();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }
        if (R.id.action_enable_all_smart_freeze_temp == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                Toast.makeText(requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps_smart_freeze_temp)
                    .setMessage(R.string.menu_desc_smart_app_freeze_enable_all_apps_smart_freeze_temp)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllSmartFreezeAppsTemp();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }
        if (R.id.action_enable_all_apps == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireContext())) {
                Toast.makeText(requireContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps)
                    .setMessage(R.string.menu_desc_smart_app_freeze_enable_all_apps)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        onRequestEnableAllApps();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void onRequestEnableAllSmartFreezeApps() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_smart_freeze_apps);
        viewModel.enableAllThanoxDisabledPackages(true, appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    private void onRequestEnableAllSmartFreezeAppsTemp() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps_smart_freeze_temp);
        viewModel.enableAllThanoxDisabledPackages(false, appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    private void onRequestEnableAllApps() {
        ModernProgressDialog progress = new ModernProgressDialog(requireActivity());
        progress.setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps);
        viewModel.onRequestEnableAllApps(appInfo -> runOnUiThread(() -> progress.setMessage(appInfo.getAppLabel())), success -> {
            progress.dismiss();
            viewModel.start();
        });
        progress.show();
    }

    void onRequestShortcutStubApk(AppInfo appInfo) {
        View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_create_stub_apk, null, false);
        EditText appNameView = dialogView.findViewById(R.id.app_name);
        EditText appVersionCodeView = dialogView.findViewById(R.id.app_version_code);
        EditText appVersionNameView = dialogView.findViewById(R.id.app_version_name);
        Objects.requireNonNull(appNameView).setText(appInfo.getAppLabel());
        Objects.requireNonNull(appVersionCodeView).setText(String.valueOf(appInfo.getVersionCode()));
        Objects.requireNonNull(appVersionNameView).setText(appInfo.getVersionName());

        new MaterialAlertDialogBuilder(requireActivity())
                .setView(dialogView)
                .setTitle(R.string.menu_title_create_shortcut_apk)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String appName = appNameView.getText().toString();
                    String appVersionName = appVersionNameView.getText().toString();
                    int appVersionCode = appInfo.getVersionCode();
                    try {
                        appVersionCode = Integer.parseInt(appVersionCodeView.getText().toString());
                    } catch (NumberFormatException ignored) {
                    }
                    viewModel.createShortcutStubApkForAsync(appInfo, appName, appVersionName, appVersionCode);
                }).show();
    }

    @Override
    public boolean onBackPressed() {
        return closeSearch();
    }

    protected boolean closeSearch() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return true;
        }
        return false;
    }

    private void onRequestAddNewApps() {
        int size = viewModel.listModels.size();
        // Limit free to add at most 9 apps.
        if (size > 3) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(requireActivity())) {
                Toast.makeText(requireActivity(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ThanosManager.from(requireContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<Pkg> exclude = Lists.newArrayList(thanosManager.getPkgManager().getSmartFreezePkgs());
            pickApps.launch(AppPickerActivity.getIntent(requireContext(), exclude));
        });
    }

    public static SmartFreezeAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SmartFreezeAppsViewModel.class);
    }
}
