package github.tornaco.android.thanos.main;

import android.app.Application;
import android.content.res.Resources;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.install.InstallCallback;
import github.tornaco.android.nitro.framework.host.install.PluginInstaller;
import github.tornaco.android.nitro.framework.host.install.UnInstallCallback;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AddPluginCallback;
import github.tornaco.android.thanos.dashboard.Tile;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;

public class NavViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    private final ObservableList<Tile> pluginFeatures = new ObservableArrayList<>();
    private final ObservableBoolean hasAnyPluginFeatures = new ObservableBoolean(false);

    public NavViewModel(@NonNull Application application) {
        super(application);
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
    }

    private List<String> getFooterTips() {
        return Lists.newArrayList(getApplication().getResources().getStringArray(R.array.nav_footer_tips));
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

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionUtils.consumeRemaining(disposables, Disposable::dispose);
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableList<Tile> getPluginFeatures() {
        return this.pluginFeatures;
    }

    public ObservableBoolean getHasAnyPluginFeatures() {
        return this.hasAnyPluginFeatures;
    }
}
