package github.tornaco.android.thanos.main;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.google.android.material.chip.Chip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.nitro.framework.Nitro;
import github.tornaco.android.nitro.framework.host.install.InstallCallback;
import github.tornaco.android.nitro.framework.host.install.PluginInstaller;
import github.tornaco.android.nitro.framework.host.manager.data.model.InstalledPlugin;
import github.tornaco.android.thanos.BuildConfig;
import github.tornaco.android.thanos.BuildProp;
import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.common.AppItemClickListener;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.CategoryIndex;
import github.tornaco.android.thanos.common.CommonAppListFilterActivity;
import github.tornaco.android.thanos.common.CommonAppListFilterViewModel;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.plugin.PluginRepo;
import github.tornaco.android.thanos.plugin.PluginResponse;
import github.tornaco.android.thanos.util.ActivityUtils;
import github.tornaco.android.thanos.widget.ModernAlertDialog;
import github.tornaco.android.thanos.widget.SwitchBar;
import util.AssetUtils;
import util.Consumer;

public class PluginMarketActivity extends CommonAppListFilterActivity {

    public static void start(Context context) {
        ActivityUtils.startActivity(context, PluginMarketActivity.class);
    }

    @Override
    protected int getTitleRes() {
        return R.string.nav_title_plugin_repo;
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
    }

    @NonNull
    @Override
    protected AppItemClickListener onCreateAppItemViewClickListener() {
        return new AppItemClickListener() {
            @Override
            public void onAppItemClick(AppInfo appInfo) {
                showPluginInfo(appInfo);
            }
        };
    }

    private void showPluginInfo(AppInfo appInfo) {
        PluginResponse response = (PluginResponse) appInfo.getObj();

        ModernAlertDialog dialog = new ModernAlertDialog(thisActivity());
        dialog.setDialogTitle(response.getName());
        dialog.setDialogMessage(response.getDescription() + "\n" + response.getVersionName() + "\n" + DateUtils.formatLong(response.getUpdateTimeMills()));
        dialog.setCancelable(true);
        dialog.setPositive(getString(R.string.title_install));
        dialog.setNegative(getString(android.R.string.cancel));
        dialog.setOnPositive(() -> installPluginFromAssets(response));
        dialog.show();
    }

    private void installPluginFromAssets(PluginResponse response) {
        String path = response.getDownloadUrl();
        // Copy to temp.
        File tempPluginFile = new File(getApplicationContext().getExternalCacheDir(), "plugin_tmp" + System.currentTimeMillis() + ".tp");
        try {
            AssetUtils.copyTo(thisActivity(), path, tempPluginFile);
            Nitro.install(this, tempPluginFile, new InstallCallback() {
                @Override
                public void onInstallSuccess(@NonNull InstalledPlugin plugin) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(thisActivity(), R.string.tile_category_plugin_installed, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }

                @Override
                public void onInstallFail(int code, @NonNull Throwable err) {
                    XLog.e(err);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (code == PluginInstaller.ErrorCodes.DUP) {
                                Toast.makeText(thisActivity(), R.string.tile_category_plugin_installed, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(thisActivity(), Log.getStackTraceString(err), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            XLog.e(e);
        }
    }

    @NonNull
    @Override
    protected CommonAppListFilterViewModel.ListModelLoader onCreateListModelLoader() {
        return new CommonAppListFilterViewModel.ListModelLoader() {
            @Override
            public List<AppListModel> load(@NonNull CategoryIndex index) {
                List<AppListModel> res = new ArrayList<>();

                String labelInstalled = getString(R.string.tile_category_plugin_installed);
                String labelUpdateAvailable = getString(R.string.tile_category_plugin_update_available);

                for (PluginResponse response : PluginRepo.getPrebuiltPlugins(thisActivity(), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (BuildConfig.DEBUG) {
                                    Toast.makeText(thisActivity(), Log.getStackTraceString(throwable), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppLabel(response.getName());
                    appInfo.setPkgName(BuildProp.THANOS_APP_PKG_NAME);
                    appInfo.setStr(response.getDownloadUrl());
                    appInfo.setObj(response);
                    appInfo.setVersionCode((int) response.getVersionCode());
                    appInfo.setVersionName(response.getVersionName());
                    appInfo.setIconDrawable(R.mipmap.ic_fallback_app_icon);

                    String badge = null;
                    InstalledPlugin installed = Nitro.getInstalledPlugin(thisActivity(), response.getPackageName());
                    if (installed != null) {
                        badge = labelInstalled;
                        if (installed.getVersionCode() < response.getVersionCode()) {
                            badge = labelUpdateAvailable;
                        }
                    }
                    res.add(new AppListModel(
                            appInfo,
                            badge,
                            null,
                            response.getDescription() + "\n" + response.getVersionName()));
                }
                return res;
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
