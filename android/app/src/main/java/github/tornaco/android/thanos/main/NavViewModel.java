package github.tornaco.android.thanos.main;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.install.InstallCallback;
import github.tornaco.android.nitro.framework.host.install.PluginInstaller;
import github.tornaco.android.nitro.framework.host.install.UnInstallCallback;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.ThanosApp;
import github.tornaco.android.thanos.app.donate.DonateSettings;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.os.SwapInfo;
import github.tornaco.android.thanos.core.pm.AddPluginCallback;
import github.tornaco.android.thanos.dashboard.MemType;
import github.tornaco.android.thanos.dashboard.MemUsage;
import github.tornaco.android.thanos.dashboard.StatusFooterInfo;
import github.tornaco.android.thanos.dashboard.StatusHeaderInfo;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.dashboard.TileGroup;
import github.tornaco.android.thanos.pref.AppPreference;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.ObjectsUtils;

public class NavViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final ObservableField<State> state = new ObservableField<>(State.InActive);
    private final ObservableInt storageUsagePercent = new ObservableInt(24);

    private final ObservableField<String> channel = new ObservableField<>();
    private final ObservableBoolean isPaid = new ObservableBoolean(false);
    private final ObservableBoolean isPowerSaveEnabled = new ObservableBoolean(false);
    private final ObservableBoolean hasFrameworkError = new ObservableBoolean(false);

    private final List<Disposable> disposables = new ArrayList<>();

    private final ObservableList<TileGroup> prebuiltFeatures = new ObservableArrayList<>();

    private final ObservableList<Tile> pluginFeatures = new ObservableArrayList<>();
    private final ObservableBoolean hasAnyPluginFeatures = new ObservableBoolean(false);

    public NavViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    @Verify
    void installPlugin(Uri uri, PluginInstallResUi ui) {
        ui.showInstallBegin();
        Nitro.install(
                getApplication(),
                uri,
                new InstallCallback.MainAdapter() {
                    @Override
                    public void onPostInstallSuccess(@NonNull InstalledPlugin plugin) {
                        XLog.i("installPlugin, plugin: %s", plugin);
                        ui.showInstallSuccess(plugin);
                    }

                    @Override
                    public void onPostInstallFail(int code, @NonNull Throwable err) {
                        XLog.e("onPostInstallFail: %s %s", code, err);
                        if (code == PluginInstaller.ErrorCodes.DUP) {
                            ui.showInstallFail(getApplication().getString(R.string.tile_category_plugin_already_installed));
                        } else {
                            ui.showInstallFail(err);
                        }
                    }
                });
    }

    @Verify
    void uninstallPlugin(InstalledPlugin plugin, PluginUnInstallResUi ui) {
        ui.showUnInstallBegin();
        Nitro.unInstall(
                getApplication(),
                plugin,
                new UnInstallCallback.MainAdapter() {
                    @Override
                    public void onPostUnInstallSuccess(@NonNull InstalledPlugin plugin) {
                        XLog.i("installPlugin, plugin: %s", plugin);
                        ThanosManager.from(getApplication())
                                .ifServiceInstalled(
                                        thanosManager ->
                                                thanosManager.getPkgManager().removePlugin(plugin.getPackageName()));
                        ui.showUnInstallSuccess(plugin);
                    }

                    @Override
                    public void onPostUnInstallFail(int code, @NonNull Throwable err) {
                        XLog.e("onPostInstallFail: %s %s", code, err);
                        ui.showUnInstallFail(err.getMessage());
                    }
                });
    }

    @Verify
    void start() {
        loadState();
        loadPrebuiltFeatures();
    }

    private void loadState() {
        XLog.v("loadState");
        ThanosManager thanos = ThanosManager.from(getApplication());
        if (!thanos.isServiceInstalled()) {
            state.set(State.InActive);
        } else if (!ObjectsUtils.equals(BuildProp.THANOS_BUILD_FINGERPRINT, thanos.fingerPrint())) {
            state.set(State.RebootNeeded);
        } else {
            state.set(State.Active);
        }

        // Init app info.
        isPowerSaveEnabled.set(
                thanos.isServiceInstalled() && thanos.getPowerManager().isPowerSaveModeEnabled());
        hasFrameworkError.set(thanos.isServiceInstalled() && thanos.hasFrameworkInitializeError());
        channel.set(getChannelString());
        isPaid.set(
                !ThanosApp.isPrc() || DonateSettings.isActivated(getApplication().getApplicationContext()));
        XLog.v("isPaid? %s", isPaid.get());
    }

    private StatusHeaderInfo loadStatusHeaderInfo() {
        final String[] memTotalSizeString = {""};
        final String[] memUsageSizeString = {""};
        final String[] memAvailableSizeString = {""};
        final int[] memUsedPercent = {0};

        final String[] swapTotalSizeString = {""};
        final String[] swapUsageSizeString = {""};
        final String[] swapAvailableSizeString = {""};
        final int[] swapUsedPercent = {0};
        final boolean[] swapEnabled = {false};


        final int[] runningAppsCount = {0};

        // Only load for pro.
        if (DonateSettings.isActivated(getApplication()) || !ThanosApp.isPrc()) {
            ThanosManager.from(getApplication()).ifServiceInstalled(
                    thanosManager -> {
                        runningAppsCount[0] = thanosManager.getActivityManager().getRunningAppsCount();
                        ActivityManager.MemoryInfo memoryInfo =
                                thanosManager.getActivityManager().getMemoryInfo();
                        if (memoryInfo != null) {
                            memTotalSizeString[0] = Formatter.formatFileSize(getApplication(), memoryInfo.totalMem);
                            memUsageSizeString[0] = Formatter.formatFileSize(getApplication(), memoryInfo.totalMem - memoryInfo.availMem);
                            memAvailableSizeString[0] = Formatter.formatFileSize(getApplication(), memoryInfo.availMem);
                            memUsedPercent[0] = (int) (100 * (((float) (memoryInfo.totalMem - memoryInfo.availMem) / Math.max((float) memoryInfo.totalMem, 1f))));
                        }

                        SwapInfo swapInfo = thanosManager.getActivityManager().getSwapInfo();
                        if (swapInfo != null) {
                            swapEnabled[0] = swapInfo.totalSwap > 0;
                            if (swapEnabled[0]) {
                                swapTotalSizeString[0] = Formatter.formatFileSize(getApplication(), swapInfo.totalSwap);
                                swapUsageSizeString[0] = Formatter.formatFileSize(getApplication(), swapInfo.totalSwap - swapInfo.freeSwap);
                                swapAvailableSizeString[0] = Formatter.formatFileSize(getApplication(), swapInfo.freeSwap);
                                swapUsedPercent[0] = (int) (100 * (((float) (swapInfo.totalSwap - swapInfo.freeSwap) / Math.max((float) swapInfo.totalSwap, 1f))));
                            }
                        }
                    });
        }

        return new StatusHeaderInfo(
                runningAppsCount[0],
                new MemUsage(
                        MemType.MEMORY,
                        memTotalSizeString[0],
                        memUsedPercent[0],
                        memUsageSizeString[0],
                        memAvailableSizeString[0],
                        true
                ),
                new MemUsage(
                        MemType.SWAP,
                        swapTotalSizeString[0],
                        swapUsedPercent[0],
                        swapUsageSizeString[0],
                        swapAvailableSizeString[0],
                        swapEnabled[0]
                )
        );
    }

    private void loadPrebuiltFeatures() {
        isDataLoading.set(true);
        Resources resources = getApplication().getResources();

        List<TileGroup> tileGroups = new ArrayList<>();

        TileGroup boost =
                new TileGroup(
                        resources.getString(R.string.nav_title_boost),
                        onlyEnabled(
                                Arrays.asList(
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_ONE_KEY_CLEAR)
                                                .iconRes(R.drawable.ic_nav_boost)
                                                .title(resources.getString(R.string.feature_title_one_key_boost))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_BG_TASK_CLEAN)
                                                .themeColor(R.color.nav_icon_boost)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_BACKGROUND_START)
                                                .iconRes(R.drawable.ic_nav_bg_start)
                                                .title(resources.getString(R.string.feature_title_start_restrict))
                                                .category(resources.getString(R.string.feature_category_start_manage))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_START_BLOCKER)
                                                .themeColor(R.color.nav_icon_bg_start)
                                                .build(),
                                        Tile.builder()
                                                .iconRes(R.drawable.ic_nav_bg_restrict)
                                                .id(PrebuiltFeatureIds.ID_BACKGROUND_RESTRICT)
                                                .title(resources.getString(R.string.feature_title_bg_restrict))
                                                .summary(resources.getString(R.string.feature_desc_bg_restrict_brief))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_BG_TASK_CLEAN)
                                                .themeColor(R.color.nav_icon_bg_restrict)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_CLEAN_TASK_REMOVAL)
                                                .iconRes(R.drawable.ic_nav_task_removal)
                                                .title(resources.getString(R.string.feature_title_clean_when_task_removed))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL)
                                                .summary(
                                                        resources.getString(
                                                                R.string.feature_desc_clean_when_task_removed_brief))
                                                .category(resources.getString(R.string.feature_category_app_clean_up))
                                                .themeColor(R.color.nav_icon_task_removal)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_SMART_FREEZE)
                                                .iconRes(R.drawable.ic_nav_smart_freeze)
                                                .title(resources.getString(R.string.feature_title_smart_app_freeze))
                                                .summary(resources.getString(R.string.feature_summary_smart_app_freeze))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE)
                                                .themeColor(R.color.nav_icon_smart_freeze)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_APPS_MANAGER)
                                                .iconRes(R.drawable.ic_nav_app_manager)
                                                .title(resources.getString(R.string.feature_title_apps_manager))
                                                .summary(resources.getString(R.string.feature_summary_apps_manager))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_COMPONENT_MANAGER)
                                                .category(resources.getString(R.string.feature_category_app_manage))
                                                .themeColor(R.color.nav_icon_apps_manager)
                                                .build())));

        TileGroup secure =
                new TileGroup(
                        resources.getString(R.string.nav_title_secure),
                        onlyEnabled(
                                Arrays.asList(
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_PRIVACY_CHEAT)
                                                .iconRes(R.drawable.ic_nav_priv_cheat)
                                                .title(resources.getString(R.string.feature_title_data_cheat))
                                                .category(resources.getString(R.string.feature_category_privacy))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT)
                                                .themeColor(R.color.nav_icon_priv_cheat)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_OPS_BY_OPS)
                                                .iconRes(R.drawable.ic_nav_ops)
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS)
                                                .title(resources.getString(R.string.module_ops_feature_title_ops_app_list))
                                                .summary(
                                                        resources.getString(R.string.module_ops_feature_summary_ops_app_list))
                                                .themeColor(R.color.nav_icon_ops)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_APP_LOCK)
                                                .iconRes(R.drawable.ic_nav_app_lock)
                                                .title(resources.getString(R.string.feature_title_app_lock))
                                                .summary(resources.getString(R.string.feature_summary_app_lock))
                                                .themeColor(R.color.nav_icon_app_lock)
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_TASK_BLUR)
                                                .iconRes(R.drawable.ic_nav_task_blur)
                                                .title(resources.getString(R.string.feature_title_recent_task_blur))
                                                .summary(resources.getString(R.string.feature_summary_recent_task_blur))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR)
                                                .themeColor(R.color.nav_icon_task_blur)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_OP_REMIND)
                                                .iconRes(R.drawable.ic_nav_op_remind)
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER)
                                                .category(resources.getString(R.string.feature_category_remind))
                                                .title(
                                                        resources.getString(R.string.module_ops_feature_title_ops_remind_list))
                                                .summary(
                                                        resources.getString(R.string.module_ops_feature_summary_ops_remind))
                                                .themeColor(R.color.nav_icon_op_remind)
                                                .build())));

        TileGroup ext =
                new TileGroup(
                        resources.getString(R.string.nav_title_exp),
                        onlyEnabled(
                                Arrays.asList(
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_SCREEN_ON_NOTIFICATION)
                                                .iconRes(R.drawable.ic_nav_screen_on_notification)
                                                .title(resources.getString(R.string.feature_title_light_on_notification))
                                                .summary(
                                                        resources.getString(R.string.feature_summary_light_on_notification))
                                                .category(resources.getString(R.string.feature_category_notification))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_EXT_N_UP)
                                                .themeColor(R.color.nav_icon_screen_on_notification)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_NOTIFICATION_RECORDER)
                                                .iconRes(R.drawable.ic_nav_nr)
                                                .title(
                                                        resources.getString(
                                                                R.string
                                                                        .module_notification_recorder_feature_title_notification_recorder))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_EXT_N_RECORDER)
                                                .themeColor(R.color.nav_icon_nr)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_TRAMPOLINE)
                                                .iconRes(R.drawable.ic_nav_activity_replacement)
                                                .category(resources.getString(R.string.feature_category_ext))
                                                .title(resources.getString(R.string.module_activity_trampoline_app_name))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_APP_TRAMPOLINE)
                                                .summary("\uD83C\uDF6D \uD83C\uDF6D \uD83C\uDF6D")
                                                .themeColor(R.color.nav_icon_activity_replacement)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_PROFILE)
                                                .iconRes(R.drawable.ic_nav_profile)
                                                .title(resources.getString(R.string.module_profile_feature_name))
                                                .summary(resources.getString(R.string.module_profile_feature_summary))
                                                .themeColor(R.color.nav_icon_profile)
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PROFILE)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_SMART_STANDBY)
                                                .iconRes(R.drawable.ic_nav_smart_standby)
                                                .category(resources.getString(R.string.feature_category_apps))
                                                .title(resources.getString(R.string.feature_title_smart_app_standby))
                                                .summary(
                                                        resources.getString(R.string.feature_summary_smart_app_standby_brief))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY)
                                                .themeColor(R.color.nav_icon_smart_standby)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_PLUGINS)
                                                .iconRes(R.drawable.ic_nav_plugins)
                                                .title(resources.getString(R.string.nav_title_plugin))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PLUGIN_SUPPORT)
                                                .summary(resources.getString(R.string.card_message_plugin_available))
                                                .themeColor(R.color.nav_icon_plugin)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_WECHAT_PUSH)
                                                .iconRes(R.drawable.ic_nav_wechat_push)
                                                .title(resources.getString(R.string.module_push_message_delegate_title_wechat_proxy))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_PUSH_DELEGATE)
                                                .themeColor(R.color.nav_icon_wechat_push)
                                                .badge1(resources.getString(R.string.common_badge_text_experiment))
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_WAKELOCK_REMOVER)
                                                .iconRes(R.drawable.ic_nav_wakelock_remover)
                                                .title(resources.getString(R.string.feature_title_wakelock_remover))
                                                .disabled(!BuildProp.THANOS_BUILD_DEBUG)
                                                .requiredFeature(BuildProp.THANOX_FEATURE_WAKELOCK_REMOVER)
                                                .themeColor(R.color.nav_icon_wakelock_remover)
                                                .badge1(resources.getString(R.string.common_badge_text_experiment))
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_INFINITE_Z)
                                                .iconRes(R.drawable.ic_nav_app_clone)
                                                .title(resources.getString(R.string.feature_title_infinite_z))
                                                .requiredFeature(BuildProp.THANOX_FEATURE_IZ)
                                                .disabled(!BuildProp.THANOS_BUILD_DEBUG)
                                                .themeColor(R.color.nav_icon_app_clone)
                                                .badge1(resources.getString(R.string.common_badge_text_experiment))
                                                .build())));

        TileGroup guide =
                new TileGroup(
                        resources.getString(R.string.nav_title_guide),
                        onlyEnabled(
                                Arrays.asList(
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_FEEDBACK)
                                                .iconRes(R.drawable.ic_nav_feedback)
                                                .title(resources.getString(R.string.nav_title_feedback))
                                                .themeColor(R.color.nav_icon_feedback)
                                                .build(),
                                        Tile.builder()
                                                .id(PrebuiltFeatureIds.ID_GUIDE)
                                                .iconRes(R.drawable.ic_nav_guide)
                                                .title(resources.getString(R.string.common_menu_title_wiki))
                                                .themeColor(R.color.nav_icon_guide)
                                                .build())));

        tileGroups.add(new TileGroup(loadStatusHeaderInfo()));

        if (boost.hasAtLeastOneTile()) tileGroups.add(boost);
        if (secure.hasAtLeastOneTile()) tileGroups.add(secure);
        if (ext.hasAtLeastOneTile()) tileGroups.add(ext);
        if (guide.hasAtLeastOneTile()) tileGroups.add(guide);
        tileGroups.add(new TileGroup(new StatusFooterInfo(getFooterTips(), AppPreference.getCurrentTipIndex(getApplication()),
                integer -> AppPreference.setCurrentTipIndex(getApplication(), integer))));

        prebuiltFeatures.clear();
        prebuiltFeatures.addAll(tileGroups);
        isDataLoading.set(false);
    }

    private List<String> getFooterTips() {
        return Lists.newArrayList(getApplication().getResources().getStringArray(R.array.nav_footer_tips));
    }

    private List<Tile> onlyEnabled(List<Tile> all) {
        List<Tile> res = new ArrayList<>();
        ThanosManager thanox = ThanosManager.from(getApplication());
        CollectionUtils.consumeRemaining(
                all,
                tile -> {
                    if (thanox.isServiceInstalled()
                            && !TextUtils.isEmpty(tile.getRequiredFeature())
                            && !thanox.hasFeature(tile.getRequiredFeature())) {
                        return;
                    }
                    if (tile.isDisabled()) {
                        return;
                    }
                    res.add(tile);
                });
        return res;
    }

    @Verify
    public void loadPluginFeatures() {
        Resources resources = getApplication().getResources();
        pluginFeatures.clear();
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        final boolean[] first = {true};
        disposables.add(
                Observable.fromIterable(Nitro.getAllInstalledPlugin(getApplication()))
                        .map(plugin -> {
                            try {
                                int pluginStatus = plugin.isWithHooks() ? Nitro.invokePluginStatus(getApplication(), plugin, -1) : -1;
                                boolean isSuPlugin = "github.tornaco.android.plugin.su".equals(plugin.getPackageName());
                                return Tile.builder()
                                        .id(plugin.getPackageName().hashCode())
                                        .category(first[0] ? resources.getString(R.string.tile_category_plugin_installed) : null)
                                        .iconRes(R.drawable.ic_extension_blue)
                                        .themeColor(R.color.md_grey_400)
                                        .title(plugin.getLabel())
                                        .summary(plugin.getDescription())
                                        .warning(isSuPlugin ? resources.getString(R.string.tile_su_plugin_deprecated) : null)
                                        .payload(plugin)
                                        .badge1(pluginStatus == 9 ? resources.getString(R.string.title_plugin_reboot_take_effect) : null)
                                        .checkable(plugin.isWithHooks())
                                        .checked(plugin.isWithHooks() && thanosManager.isServiceInstalled() && thanosManager.getPkgManager().hasPlugin(plugin.getPackageName()))
                                        .build();
                            } finally {
                                first[0] = false;
                            }
                        })
                        .filter(tile -> !tile.isDisabled())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                tile -> {
                                    if (queryTileFromListById(pluginFeatures, tile.getId()) == null) {
                                        pluginFeatures.add(tile);
                                    }
                                },
                                throwable -> XLog.e(Log.getStackTraceString(throwable)),
                                () -> hasAnyPluginFeatures.set(pluginFeatures.size() > 0)));
    }

    void setPluginActive(InstalledPlugin plugin, boolean checked) {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            return;
        }

        if (checked == isPluginActive(plugin)) {
            return;
        }

        // Add path.
        if (checked) {
            try {
                ParcelFileDescriptor pfd =
                        ParcelFileDescriptor.open(
                                new File(plugin.getApkFile()), ParcelFileDescriptor.MODE_READ_ONLY);
                thanosManager
                        .getPkgManager()
                        .addPlugin(
                                pfd,
                                plugin.getPackageName(),
                                new AddPluginCallback() {
                                    @Override
                                    public void onPluginAdd() {
                                        Toast.makeText(
                                                        getApplication(),
                                                        R.string.title_plugin_reboot_take_effect,
                                                        Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    @Override
                                    public void onFail(String message) {
                                        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
                                    }
                                });
            } catch (FileNotFoundException e) {
                XLog.e(Log.getStackTraceString(e));
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                // Reload.
                start();
            }
        } else {
            thanosManager.getPkgManager().removePlugin(plugin.getPackageName());
            Toast.makeText(getApplication(), R.string.title_plugin_reboot_take_effect, Toast.LENGTH_LONG)
                    .show();
        }
    }

    boolean isPluginActive(InstalledPlugin plugin) {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            return false;
        }
        return thanosManager.getPkgManager().hasPlugin(plugin.getPackageName());
    }

    @Nullable
    private Tile queryTileFromListById(ObservableList<Tile> list, int id) {
        for (Tile tile : list) {
            if (tile.getId() == id) return tile;
        }
        return null;
    }

    private void registerEventReceivers() {
        DonateSettings.getRegistry()
                .addObserver((o, arg) -> loadState());
    }

    private void unRegisterEventReceivers() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionUtils.consumeRemaining(disposables, Disposable::dispose);
        unRegisterEventReceivers();
    }

    private String getChannelString() {
        //noinspection ConstantConditions
        return BuildConfig.DEBUG ? "开发版" : (BuildConfig.VERSION_NAME.contains("beta") ? "测试版" : null);
    }

    void cleanUpBackgroundTasks() {
        getApplication().sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableField<State> getState() {
        return this.state;
    }

    public ObservableInt getStorageUsagePercent() {
        return this.storageUsagePercent;
    }

    public ObservableField<String> getChannel() {
        return this.channel;
    }

    public ObservableBoolean getIsPaid() {
        return this.isPaid;
    }

    public ObservableBoolean getIsPowerSaveEnabled() {
        return this.isPowerSaveEnabled;
    }

    public ObservableBoolean getHasFrameworkError() {
        return this.hasFrameworkError;
    }

    public ObservableList<TileGroup> getPrebuiltFeatures() {
        return this.prebuiltFeatures;
    }

    public ObservableList<Tile> getPluginFeatures() {
        return this.pluginFeatures;
    }

    public ObservableBoolean getHasAnyPluginFeatures() {
        return this.hasAnyPluginFeatures;
    }

    public interface PluginInstallResUi {
        void showInstallBegin();

        void showInstallSuccess(@NonNull InstalledPlugin plugin);

        void showInstallFail(String message);

        void showInstallFail(Throwable error);
    }

    public interface PluginUnInstallResUi {
        void showUnInstallBegin();

        void showUnInstallSuccess(@NonNull InstalledPlugin plugin);

        void showUnInstallFail(String message);
    }
}
