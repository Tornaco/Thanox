package github.tornaco.android.thanos.power;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.common.AppLabelSearchFilter;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.Pkg;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.android.thanos.util.InstallerUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.Consumer;
import util.PinyinComparatorUtils;

public class SmartFreezeAppsViewModel extends AndroidViewModel {
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    @Nullable
    private String pkgSetId;

    protected final List<Disposable> disposables = new ArrayList<>();
    protected final ObservableArrayList<AppListModel> listModels = new ObservableArrayList<>();

    private final ObservableField<String> queryText = new ObservableField<>("");
    private final AppLabelSearchFilter appLabelSearchFilter = new AppLabelSearchFilter();

    public SmartFreezeAppsViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Null means we load all pkgs
     */
    public void setPkgSetId(@Nullable String pkgSetId) {
        this.pkgSetId = pkgSetId;
    }

    void start() {
        loadModels();
    }

    void remove() {

    }

    @Verify
    private void loadModels() {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<AppListModel>>) emitter ->
                        emitter.onSuccess(getSmartFreezeApps()))
                .map(listModels -> {
                    listModels.sort((o1, o2) -> PinyinComparatorUtils.compare(o1.appInfo.getAppLabel(), o2.appInfo.getAppLabel()));
                    return listModels;
                })
                .flatMapObservable((Function<List<AppListModel>, ObservableSource<AppListModel>>) Observable::fromIterable)
                .filter(listModel -> {
                    String query = queryText.get();
                    return TextUtils.isEmpty(query)
                            || appLabelSearchFilter.matches(query, listModel.appInfo.getAppLabel());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> listModels.clear())
                .subscribe(listModels::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
    }

    private List<AppListModel> getSmartFreezeApps() {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            return new ArrayList<>(0);
        }
        PackageSet packageSet = pkgSetId == null ? null : thanosManager.getPkgManager().getPackageSetById(pkgSetId, true);
        List<AppListModel> res = new ArrayList<>();
        for (Pkg pkg : thanosManager.getPkgManager().getSmartFreezePkgs()) {
            AppInfo appInfo = thanosManager.getPkgManager().getAppInfoForUser(pkg.getPkgName(), pkg.getUserId());
            if (appInfo != null && (packageSet == null || packageSet.getPkgNames().contains(appInfo.getPkgName()))) {
                XLog.v("getSmartFreezeApps app: %s, enabled: %s", appInfo.getPkgName(), !appInfo.disabled());
                AppListModel model = new AppListModel(appInfo);
                res.add(model);
            }
        }
        return res;
    }

    void createShortcutStubApkForAsync(AppInfo appInfo,
                                       String appLabel,
                                       String versionName,
                                       int versionCode) {
        disposables.add(Single.create((SingleOnSubscribe<File>) emitter ->
                emitter.onSuccess(ShortcutHelper.createShortcutStubApkFor(getApplication(), appInfo, appLabel, versionName, versionCode)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> requestInstallStubApk(getApplication(), file),
                        throwable -> {
                            XLog.e("createShortcutStubApkForAsync error", throwable);
                            Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }));
    }

    private void requestInstallStubApk(Context context, File apk) {
        InstallerUtils.installUserAppWithIntent(context, apk);
    }

    void requestUnInstallStubApkIfInstalled(Context context, AppInfo appInfo) {
        XLog.v("requestUnInstallStubApkIfInstalled: %s", appInfo);
        String stubPkgName = ThanosManager.from(context).getPkgManager().createShortcutStubPkgName(appInfo);
        XLog.v("requestUnInstallStubApkIfInstalled: %s", stubPkgName);
        if (PkgUtils.isPkgInstalled(context, stubPkgName)) {
            InstallerUtils.uninstallUserAppWithIntent(context, stubPkgName);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableArrayList<AppListModel> getListModels() {
        return this.listModels;
    }

    void clearSearchText() {
        if (!TextUtils.isEmpty(queryText.get())) {
            queryText.set(null);
            loadModels();
        }
    }

    void setSearchText(String query) {
        if (TextUtils.isEmpty(query)) return;
        queryText.set(query);
        loadModels();
    }

    public void onRequestEnableAllApps(Consumer<AppInfo> onProgress, Consumer<Boolean> onComplete) {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            onComplete.accept(false);
            return;
        }
        disposables.add(Completable.fromRunnable(() -> CollectionUtils.consumeRemaining(thanosManager.getPkgManager().getInstalledPkgs(AppInfo.FLAGS_ALL), appInfo -> {
            boolean enabled = thanosManager.getPkgManager().getApplicationEnableState(Pkg.fromAppInfo(appInfo));
            if (!enabled) {
                onProgress.accept(appInfo);
                thanosManager.getPkgManager().setApplicationEnableState(Pkg.fromAppInfo(appInfo), true, false);
                // Give system a rest
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        })).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(() -> onComplete.accept(true)));
    }

    public void enableAllThanoxDisabledPackages(boolean alsoDisableSmartFreezeForPkgs, Consumer<AppInfo> onProgress, Consumer<Boolean> onComplete) {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            onComplete.accept(false);
            return;
        }
        disposables.add(Completable.fromRunnable(() -> CollectionUtils.consumeRemaining(listModels, listModel -> {
            AppInfo appInfo = listModel.appInfo;
            boolean enabled = thanosManager.getPkgManager().getApplicationEnableState(Pkg.fromAppInfo(appInfo));
            if (!enabled) {
                onProgress.accept(appInfo);
                thanosManager.getPkgManager().setApplicationEnableState(Pkg.fromAppInfo(appInfo), true, false);
                if (alsoDisableSmartFreezeForPkgs) {
                    thanosManager.getPkgManager().setPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo), false);
                }
                // Give system a rest
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            }
        })).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(() -> onComplete.accept(true)));
    }

    public void addToSmartFreezeList(List<AppInfo> appInfos, boolean alsoAddToPkgSet, Consumer<AppInfo> onProgress, Consumer<Boolean> onComplete) {
        ThanosManager thanosManager = ThanosManager.from(getApplication());
        if (!thanosManager.isServiceInstalled()) {
            onComplete.accept(false);
            return;
        }
        disposables.add(Completable.fromRunnable(() -> {
            PackageSet packageSet = pkgSetId == null ? null : thanosManager.getPkgManager().getPackageSetById(pkgSetId, false);
            CollectionUtils.consumeRemaining(appInfos, appInfo -> {
                onProgress.accept(appInfo);
                thanosManager.getPkgManager().setPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo), true);
                if (alsoAddToPkgSet && packageSet != null && !packageSet.isPrebuilt()) {
                    thanosManager.getPkgManager().addToPackageSet(appInfo.getPkgName(), pkgSetId);
                }
                // Give system a rest
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                }
            });
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(() -> onComplete.accept(true)));
    }

    public void disableSmartFreeze(AppListModel model) {
        AppInfo appInfo = model.appInfo;
        ThanosManager.from(getApplication())
                .getPkgManager()
                .setPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo), false);
        listModels.remove(model);
        requestUnInstallStubApkIfInstalled(getApplication(), appInfo);
    }
}
