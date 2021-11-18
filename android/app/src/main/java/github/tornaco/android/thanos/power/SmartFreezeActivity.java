package github.tornaco.android.thanos.power;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.Lists;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.common.AppItemActionListener;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.databinding.ActivitySmartFreezeAppsBinding;
import github.tornaco.android.thanos.picker.AppPickerActivity;
import github.tornaco.android.thanos.pref.AppPreference;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.CollectionUtils;

public class SmartFreezeActivity extends ThemeActivity {
    private static final int REQ_PICK_APPS = 0x100;

    private SmartFreezeAppsViewModel viewModel;
    private ActivitySmartFreezeAppsBinding binding;

    private Handler uiHandler;
    private Runnable hideFabRunnable = new Runnable() {
        @Override
        public void run() {
            binding.fab.hide();
        }
    };
    private Runnable showFabRunnable = new Runnable() {
        @Override
        public void run() {
            binding.fab.show();
        }
    };

    @Verify
    public static void start(Context context) {
        ActivityUtils.startActivity(context, SmartFreezeActivity.class);
    }

    @Override
    @Verify
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmartFreezeAppsBinding.inflate(
                LayoutInflater.from(this), null, false);
        uiHandler = new Handler(Looper.getMainLooper());
        setContentView(binding.getRoot());
        setupView();
        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showFeatureDialogIfNeed();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                        // Noop.
                    }

                    @Override
                    public void onSearchViewClosed() {
                        viewModel.clearSearchText();
                    }
                });

        // List.
        binding.apps.setLayoutManager(new GridLayoutManager(this, 5));
        binding.apps.setAdapter(new SmartFreezeAppsAdapter(new AppItemActionListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                ThanosManager.from(getApplicationContext())
                        .getPkgManager()
                        .launchSmartFreezePkg(appInfo.getPkgName());
                postOnUiDelayed(() -> viewModel.start(), 2000);
            }

            @Override
            public void onAppItemSwitchStateChange(AppInfo appInfo, boolean checked) {

            }
        }, this::showItemPopMenu));
        binding.apps.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    delayHideFab();
                } else {
                    nowShowFab();
                }
            }
        });


        binding.swipe.setOnRefreshListener(() -> viewModel.start());
        binding.swipe.setColorSchemeColors(getResources().getIntArray(github.tornaco.android.thanos.module.common.R.array.common_swipe_refresh_colors));

        onSetupSwitchBar(binding.switchBarContainer.switchBar);

        binding.fab.setOnClickListener(v -> ThanosManager.from(getApplicationContext()).ifServiceInstalled(thanosManager -> {
            ArrayList<String> exclude = Lists.newArrayList(thanosManager.getPkgManager().getSmartFreezePkgs());
            AppPickerActivity.start(thisActivity(), REQ_PICK_APPS, exclude);
        }));

        delayHideFab();
    }

    private void nowHideFab() {
        uiHandler.removeCallbacks(hideFabRunnable);
        uiHandler.post(hideFabRunnable);
    }

    private void delayHideFab() {
        uiHandler.removeCallbacks(hideFabRunnable);
        uiHandler.postDelayed(hideFabRunnable, 1800);
    }

    private void nowShowFab() {
        uiHandler.removeCallbacks(showFabRunnable);
        uiHandler.post(showFabRunnable);
    }

    @Verify
    private void showItemPopMenu(@NonNull View anchor, @NonNull AppInfo appInfo) {
        PopupMenu popupMenu = new PopupMenu(thisActivity(), anchor);
        popupMenu.inflate(R.menu.smart_freeze_item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_remove_from_smart_freeze) {
                ThanosManager.from(getApplicationContext())
                        .getPkgManager()
                        .setPkgSmartFreezeEnabled(appInfo.getPkgName(), false);
                viewModel.requestUnInstallStubApkIfInstalled(getApplicationContext(), appInfo);
                viewModel.start();
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut) {
                ShortcutHelper.addShortcut(thisActivity(), appInfo);
                return true;
            }
            if (item.getItemId() == R.id.action_create_shortcut_apk) {
                if (ThanosApp.isPrc() && !DonateSettings.isActivated(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                    return false;
                }
                onRequestShortcutStubApk(appInfo);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    protected void onSetupSwitchBar(SwitchBar switchBar) {
        switchBar.hide();
    }

    @Verify
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
            binding.swipe.setRefreshing(true);
            List<AppInfo> appInfos = data.getParcelableArrayListExtra("apps");
            CollectionUtils.consumeRemaining(appInfos, appInfo -> ThanosManager.from(getApplicationContext())
                    .getPkgManager()
                    .setPkgSmartFreezeEnabled(appInfo.getPkgName(), true));
            postOnUiDelayed(() -> {
                binding.swipe.setRefreshing(false);
                viewModel.start();
            }, 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.smart_freeze_menu, menu);
        MenuItem item = menu.findItem(github.tornaco.android.thanos.module.common.R.id.action_search);
        binding.searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    @Verify
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_settings == item.getItemId()) {
            SmartFreezeSettingsActivity.start(this);
            return true;
        }
        if (R.id.action_enable_all == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps)
                    .setMessage(R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        ThanosManager.from(getApplicationContext())
                                .getPkgManager()
                                .enableAllThanoxDisabledPackages(true);
                        viewModel.start();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }
        if (R.id.action_enable_all_temp == item.getItemId()) {
            if (ThanosApp.isPrc() && !DonateSettings.isActivated(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), R.string.module_donate_donated_available, Toast.LENGTH_SHORT).show();
                return false;
            }
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.menu_title_smart_app_freeze_enable_all_apps_temp)
                    .setMessage(R.string.common_dialog_message_are_you_sure)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        ThanosManager.from(getApplicationContext())
                                .getPkgManager()
                                .enableAllThanoxDisabledPackages(false);
                        viewModel.start();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void onRequestShortcutStubApk(AppInfo appInfo) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_stub_apk, null, false);
        EditText appNameView = dialogView.findViewById(R.id.app_name);
        EditText appVersionCodeView = dialogView.findViewById(R.id.app_version_code);
        EditText appVersionNameView = dialogView.findViewById(R.id.app_version_name);
        Objects.requireNonNull(appNameView).setText(appInfo.getAppLabel());
        Objects.requireNonNull(appVersionCodeView).setText(String.valueOf(appInfo.getVersionCode()));
        Objects.requireNonNull(appVersionNameView).setText(appInfo.getVersionName());

        new MaterialAlertDialogBuilder(thisActivity())
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

    private void showFeatureDialogIfNeed() {
        if (AppPreference.isFeatureNoticeAccepted(getApplicationContext(), "SmartFreeze")) {
            return;
        }
        new MaterialAlertDialogBuilder(thisActivity())
                .setTitle(R.string.feature_title_smart_app_freeze)
                .setMessage(R.string.feature_desc_smart_app_freeze)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> finish())
                .setNeutralButton(R.string.title_remember, (dialog, which) ->
                        AppPreference.setFeatureNoticeAccepted(getApplicationContext(), "SmartFreeze", true))
                .show();

    }

    @Override
    public void onBackPressed() {
        if (closeSearch()) {
            return;
        }
        super.onBackPressed();
    }

    protected boolean closeSearch() {
        if (binding.searchView.isSearchOpen()) {
            binding.searchView.closeSearch();
            return true;
        }
        return false;
    }

    public static SmartFreezeAppsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(SmartFreezeAppsViewModel.class);
    }
}
