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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.install.InstallCallback;
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
import github.tornaco.android.thanos.core.pm.AddPluginCallback;
import github.tornaco.android.thanos.dashboard.StatusFooterInfo;
import github.tornaco.android.thanos.dashboard.StatusHeaderInfo;
import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.dashboard.TileGroup;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.ObjectsUtils;

public class NavViewModel extends AndroidViewModel {

  @Getter private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
  @Getter private final ObservableField<State> state = new ObservableField<>(State.InActive);
  @Getter private final ObservableInt storageUsagePercent = new ObservableInt(24);

  @Getter private final ObservableField<String> channel = new ObservableField<>();
  @Getter private final ObservableBoolean isPaid = new ObservableBoolean(false);
  @Getter private final ObservableBoolean isPowerSaveEnabled = new ObservableBoolean(false);
  @Getter private final ObservableBoolean hasFrameworkError = new ObservableBoolean(false);

  private final List<Disposable> disposables = new ArrayList<>();

  @Getter private final ObservableList<TileGroup> prebuiltFeatures = new ObservableArrayList<>();

  @Getter private final ObservableList<Tile> pluginFeatures = new ObservableArrayList<>();
  @Getter private final ObservableBoolean hasAnyPluginFeatures = new ObservableBoolean(false);

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
            ui.showInstallFail(err.getMessage());
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
    final int[] usedPercent = {0};
    final String[] memAvailablePercentString = {"N/A"};
    final int[] runningAppsCount = {0};

    // Only load for pro.
    if (DonateSettings.isActivated(getApplication()) || !ThanosApp.isPrc()) {
      ThanosManager.from(getApplication())
          .ifServiceInstalled(
              new util.Consumer<ThanosManager>() {
                @Override
                public void accept(ThanosManager thanosManager) {
                  runningAppsCount[0] = thanosManager.getActivityManager().getRunningAppsCount();

                  ActivityManager.MemoryInfo memoryInfo =
                      thanosManager.getActivityManager().getMemoryInfo();
                  if (memoryInfo != null) {
                    usedPercent[0] =
                        (int)
                            (100
                                * (((float) (memoryInfo.totalMem - memoryInfo.availMem)
                                    / (float) memoryInfo.totalMem)));
                    long availableMB = memoryInfo.availMem;
                    memAvailablePercentString[0] =
                        Formatter.formatFileSize(getApplication(), availableMB);
                  }
                }
              });
    }

    return StatusHeaderInfo.builder()
        .runningAppsCount(runningAppsCount[0])
        .memUsagePercent(usedPercent[0])
        .memAvailablePercentString(memAvailablePercentString[0])
        .build();
  }

  private void loadPrebuiltFeatures() {
    Resources resources = getApplication().getResources();
    ThanosManager thanosManager = ThanosManager.from(getApplication());

    List<TileGroup> tileGroups = new ArrayList<>();

    TileGroup boost =
        new TileGroup(
            resources.getString(R.string.nav_title_boost),
            onlyEnabled(
                Arrays.asList(
                    Tile.builder()
                        .id(R.id.id_one_key_clear)
                        .iconRes(R.drawable.ic_rocket_line)
                        .title(resources.getString(R.string.feature_title_one_key_boost))
                        .requiredFeature(BuildProp.THANOX_FEATURE_BG_TASK_CLEAN)
                        .themeColor(R.color.md_green_500)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_background_start)
                        .iconRes(R.drawable.ic_forbid_line)
                        .title(resources.getString(R.string.feature_title_start_restrict))
                        .category(resources.getString(R.string.feature_category_start_manage))
                        .requiredFeature(BuildProp.THANOX_FEATURE_START_BLOCKER)
                        .themeColor(R.color.md_blue_500)
                        .build(),
                    Tile.builder()
                        .iconRes(R.drawable.ic_restart_line)
                        .id(R.id.id_background_restrict)
                        .title(resources.getString(R.string.feature_title_bg_restrict))
                        .summary(resources.getString(R.string.feature_desc_bg_restrict_brief))
                        .requiredFeature(BuildProp.THANOX_FEATURE_BG_TASK_CLEAN)
                        .themeColor(R.color.md_amber_500)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_clean_task_removal)
                        .iconRes(R.drawable.ic_clear_all_black_24dp)
                        .title(resources.getString(R.string.feature_title_clean_when_task_removed))
                        .requiredFeature(BuildProp.THANOX_FEATURE_RECENT_TASK_REMOVAL)
                        .summary(
                            resources.getString(
                                R.string.feature_desc_clean_when_task_removed_brief))
                        .category(resources.getString(R.string.feature_category_app_clean_up))
                        .themeColor(R.color.md_grey_500)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_smart_freeze)
                        .iconRes(R.drawable.ic_ac_unit_black_24dp)
                        .title(resources.getString(R.string.feature_title_smart_app_freeze))
                        .summary(resources.getString(R.string.feature_summary_smart_app_freeze))
                        .requiredFeature(BuildProp.THANOX_FEATURE_EXT_APP_SMART_FREEZE)
                        .themeColor(R.color.md_blue_grey_300)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_apps_manager)
                        .iconRes(R.drawable.ic_apps_2_line)
                        .title(resources.getString(R.string.feature_title_apps_manager))
                        .summary(resources.getString(R.string.feature_summary_apps_manager))
                        .requiredFeature(BuildProp.THANOX_FEATURE_COMPONENT_MANAGER)
                        .category(resources.getString(R.string.feature_category_app_manage))
                        .themeColor(R.color.md_pink_300)
                        .build())));

    TileGroup secure =
        new TileGroup(
            resources.getString(R.string.nav_title_secure),
            onlyEnabled(
                Arrays.asList(
                    Tile.builder()
                        .id(R.id.id_privacy_cheat)
                        .iconRes(R.drawable.ic_spy_line)
                        .title(resources.getString(R.string.feature_title_data_cheat))
                        .category(resources.getString(R.string.feature_category_privacy))
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_DATA_CHEAT)
                        .themeColor(R.color.md_grey_700)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_ops_by_app)
                        .iconRes(R.drawable.ic_shield_star_line)
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS)
                        .title(resources.getString(R.string.module_ops_feature_title_app_ops_list))
                        .summary(
                            resources.getString(R.string.module_ops_feature_summary_app_ops_list))
                        .themeColor(R.color.md_lime_600)
                        // Dup with apps manager.
                        .disabled(true)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_ops_by_ops)
                        .iconRes(R.drawable.ic_shield_star_line)
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS)
                        .title(resources.getString(R.string.module_ops_feature_title_ops_app_list))
                        .summary(
                            resources.getString(R.string.module_ops_feature_summary_ops_app_list))
                        .themeColor(R.color.md_teal_500)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_app_lock)
                        .iconRes(R.drawable.ic_phone_lock_line)
                        .title(resources.getString(R.string.feature_title_app_lock))
                        .summary(resources.getString(R.string.feature_summary_app_lock))
                        .themeColor(R.color.md_deep_orange_500)
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_APPLOCK)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_task_blur)
                        .iconRes(R.drawable.ic_brush_2_line)
                        .title(resources.getString(R.string.feature_title_recent_task_blur))
                        .summary(resources.getString(R.string.feature_summary_recent_task_blur))
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_TASK_BLUR)
                        .themeColor(R.color.md_cyan_300)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_op_remind)
                        .iconRes(R.drawable.ic_alarm_warning_line)
                        .requiredFeature(BuildProp.THANOX_FEATURE_PRIVACY_OPS_REMINDER)
                        .category(resources.getString(R.string.feature_category_remind))
                        .title(
                            resources.getString(R.string.module_ops_feature_title_ops_remind_list))
                        .summary(
                            resources.getString(R.string.module_ops_feature_summary_ops_remind))
                        .themeColor(R.color.md_amber_500)
                        .build())));

    TileGroup ext =
        new TileGroup(
            resources.getString(R.string.nav_title_exp),
            onlyEnabled(
                Arrays.asList(
                    Tile.builder()
                        .id(R.id.id_screen_on_notification)
                        .iconRes(R.drawable.ic_notification_badge_line)
                        .title(resources.getString(R.string.feature_title_light_on_notification))
                        .summary(
                            resources.getString(R.string.feature_summary_light_on_notification))
                        .category(resources.getString(R.string.feature_category_notification))
                        .requiredFeature(BuildProp.THANOX_FEATURE_EXT_N_UP)
                        .themeColor(R.color.md_red_500)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_notification_recorder)
                        .iconRes(R.drawable.ic_chat_1_line)
                        .title(
                            resources.getString(
                                R.string
                                    .module_notification_recorder_feature_title_notification_recorder))
                        .requiredFeature(BuildProp.THANOX_FEATURE_EXT_N_RECORDER)
                        .themeColor(R.color.md_cyan_600)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_trampoline)
                        .iconRes(R.drawable.ic_guide_fill)
                        .category(resources.getString(R.string.feature_category_ext))
                        .title(resources.getString(R.string.module_activity_trampoline_app_name))
                        .requiredFeature(BuildProp.THANOX_FEATURE_APP_TRAMPOLINE)
                        .summary("\uD83C\uDF6D \uD83C\uDF6D \uD83C\uDF6D")
                        .themeColor(R.color.md_green_a700)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_profile)
                        .iconRes(R.drawable.ic_thunderstorms_line)
                        .title(resources.getString(R.string.module_profile_feature_name))
                        .summary(resources.getString(R.string.module_profile_feature_summary))
                        .themeColor(R.color.md_indigo_300)
                        .requiredFeature(BuildProp.THANOX_FEATURE_PROFILE)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_smart_standby)
                        .iconRes(R.drawable.ic_mickey_line)
                        .category(resources.getString(R.string.feature_category_apps))
                        .title(resources.getString(R.string.feature_title_smart_app_standby))
                        .summary(
                            resources.getString(R.string.feature_summary_smart_app_standby_brief))
                        .requiredFeature(BuildProp.THANOX_FEATURE_APP_SMART_STAND_BY)
                        .themeColor(R.color.md_deep_orange_300)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_plugins)
                        .iconRes(R.drawable.ic_store_line)
                        .title(resources.getString(R.string.nav_title_plugin))
                        .requiredFeature(BuildProp.THANOX_FEATURE_PLUGIN_SUPPORT)
                        .summary(resources.getString(R.string.card_message_plugin_available))
                        .themeColor(R.color.md_grey_400)
                        .build(),
                    Tile.builder()
                        .id(R.id.id_infinite_z)
                        .iconRes(R.drawable.ic_bring_forward)
                        .title(resources.getString(R.string.feature_title_infinite_z))
                        .disabled(true)
                        .themeColor(R.color.md_blue_600)
                        .build())));

    tileGroups.add(new TileGroup(loadStatusHeaderInfo()));
    if (boost.hasAtLeastOneTile()) tileGroups.add(boost);
    if (secure.hasAtLeastOneTile()) tileGroups.add(secure);
    if (ext.hasAtLeastOneTile()) tileGroups.add(ext);
    tileGroups.add(
        new TileGroup(
            StatusFooterInfo.builder()
                .footerString1("Powered by thanox@" + BuildProp.THANOS_VERSION_NAME)
                .build()));

    prebuiltFeatures.clear();
    prebuiltFeatures.addAll(tileGroups);
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
            .map(
                plugin -> {
                  try {
                    int pluginStatus =
                        plugin.isWithHooks()
                            ? Nitro.invokePluginStatus(getApplication(), plugin, -1)
                            : -1;
                    return Tile.builder()
                        .id(plugin.getPackageName().hashCode())
                        .category(
                            first[0]
                                ? resources.getString(R.string.tile_category_plugin_installed)
                                : null)
                        .iconRes(R.drawable.ic_extension_blue)
                        .themeColor(R.color.md_grey_400)
                        .title(plugin.getLabel())
                        .summary(plugin.getDescription())
                        .payload(plugin)
                        .badge1(
                            pluginStatus == 9
                                ? resources.getString(R.string.title_plugin_reboot_take_effect)
                                : null)
                        .checkable(plugin.isWithHooks())
                        .checked(
                            plugin.isWithHooks()
                                && thanosManager.isServiceInstalled()
                                && thanosManager.getPkgManager().hasPlugin(plugin.getPackageName()))
                        .build();
                  } finally {
                    first[0] = false;
                  }
                })
            .filter(tile -> !tile.isDisabled())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                new Consumer<Tile>() {
                  @Override
                  public void accept(Tile tile) {
                    if (queryTileFromListById(pluginFeatures, tile.getId()) == null) {
                      pluginFeatures.add(tile);
                    }
                  }
                },
                new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) {
                    XLog.e(Log.getStackTraceString(throwable));
                  }
                },
                new Action() {
                  @Override
                  public void run() {
                    hasAnyPluginFeatures.set(pluginFeatures.size() > 0);
                  }
                }));
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

  private void unRegisterEventReceivers() {}

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

  public interface PluginInstallResUi {
    void showInstallBegin();

    void showInstallSuccess(@NonNull InstalledPlugin plugin);

    void showInstallFail(String message);
  }

  public interface PluginUnInstallResUi {
    void showUnInstallBegin();

    void showUnInstallSuccess(@NonNull InstalledPlugin plugin);

    void showUnInstallFail(String message);
  }
}
