package github.tornaco.android.thanos.power;

import android.app.Application;
import android.app.usage.UsageStats;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.elvishew.xlog.XLog;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.common.AppLabelSearchFilter;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.common.sort.AppSort;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.PackageEnableStateChangeListener;
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
import util.IoUtils;
import util.JsonFormatter;

@SuppressWarnings("UnstableApiUsage")
public class SmartFreezeAppsViewModel extends AndroidViewModel {
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private static final String PREF_KEY_SORT_PREFIX = "pref.default.app.sort.id_";

    @Nullable
    private String pkgSetId;

    protected final List<Disposable> disposables = new ArrayList<>();
    protected final ObservableArrayList<AppListModel> listModels = new ObservableArrayList<>();

    private final ObservableField<String> queryText = new ObservableField<>("");
    private final AppLabelSearchFilter appLabelSearchFilter = new AppLabelSearchFilter();

    private final ObservableBoolean sortReverse = new ObservableBoolean(false);
    private final ObservableField<AppSort> currentSort = new ObservableField<>(AppSort.Default);

    public SmartFreezeAppsViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Null means we load all pkgs
     */
    public void setPkgSetId(@Nullable String pkgSetId) {
        XLog.i("setPkgSetId: " + pkgSetId);
        this.pkgSetId = pkgSetId;

        restoreSort();
    }

    private String getAppPrefFeatureId() {
        return PREF_KEY_SORT_PREFIX + "smartFreeze_" + pkgSetId;
    }

    public void restoreSort() {
        String preferredSortName = PreferenceManager.getDefaultSharedPreferences(getApplication())
                .getString(getAppPrefFeatureId(), AppSort.Default.name());
        AppSort preferredSort = AppSort.Default;
        try {
            preferredSort = AppSort.valueOf(preferredSortName);
        } catch (Throwable ignored) {
        }
        currentSort.set(preferredSort);
    }

    @Nullable
    public PackageSet getPackageSet() {
        return pkgSetId == null ? null : ThanosManager.from(getApplication()).getPkgManager().getPackageSetById(pkgSetId, true);
    }

    void start() {
        loadModels();
    }

    @Verify
    private void loadModels() {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single.create((SingleOnSubscribe<List<AppListModel>>) emitter -> emitter.onSuccess(getSmartFreezeApps())).flatMapObservable((Function<List<AppListModel>, ObservableSource<AppListModel>>) Observable::fromIterable).filter(listModel -> {
            String query = queryText.get();
            return TextUtils.isEmpty(query) || appLabelSearchFilter.matches(query, listModel.appInfo.getAppLabel());
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(disposable -> listModels.clear()).subscribe(listModels::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
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
            if (appInfo != null && (packageSet == null || packageSet.getPkgList().contains(pkg))) {
                XLog.v("getSmartFreezeApps app: %s, enabled: %s", appInfo.getPkgName(), !appInfo.disabled());
                AppListModel model = new AppListModel(appInfo);
                res.add(model);
            }
        }

        AppSort sort = currentSort.get();
        if (sort != null) {
            // inflate usage stats if necessary.
            if (sort.relyOnUsageStats()) {
                inflateAppUsageStats(res);
            }

            AppSort.AppSorterProvider appSorterProvider = sort.provider;
            if (appSorterProvider != null) {
                res.sort(appSorterProvider.comparator(getApplication()));
                if (sortReverse.get()) {
                    Collections.reverse(res);
                }
            }
        }

        return res;
    }

    private void inflateAppUsageStats(List<AppListModel> res) {
        XLog.d("inflateAppUsageStats");
        ThanosManager thanox = ThanosManager.from(getApplication());
        if (thanox.isServiceInstalled()) {
            Map<String, UsageStats> statsMap = thanox.getUsageStatsManager().queryAndAggregateUsageStats(0, System.currentTimeMillis());
            for (AppListModel app : res) {
                if (statsMap.containsKey(app.appInfo.getPkgName())) {
                    UsageStats stats = statsMap.get(app.appInfo.getPkgName());
                    if (stats != null) {
                        app.lastUsedTimeMills = stats.getLastTimeUsed();
                        app.totalUsedTimeMills = stats.getTotalTimeInForeground();
                    }
                }
            }
        }
    }

    void createShortcutStubApkForAsync(AppInfo appInfo, String appLabel, String versionName, int versionCode, Consumer<File> onApkFileReady) {
        disposables.add(Single.create((SingleOnSubscribe<File>) emitter ->
                        emitter.onSuccess(ShortcutHelper.createShortcutStubApkFor(getApplication(), appInfo, appLabel, versionName, versionCode)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    onApkFileReady.accept(file);
                }, throwable -> {
                    XLog.e("createShortcutStubApkForAsync error", throwable);
                    Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                }));
    }

    public void requestInstallStubApk(Context context, File apk) {
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
                    thanosManager.getPkgManager().addToPackageSet(Pkg.fromAppInfo(appInfo), pkgSetId);
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
        ThanosManager.from(getApplication()).getPkgManager().setPkgSmartFreezeEnabled(Pkg.fromAppInfo(appInfo), false);
        listModels.remove(model);
        requestUnInstallStubApkIfInstalled(getApplication(), appInfo);
    }

    public void exportPackageListToClipBoard(Runnable onSuccess, Consumer<Throwable> onError) {
        disposables.add(Completable.fromRunnable(() -> {
            String json = getExportPackageListContent();
            ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            XLog.w("content: " + json);
            cmb.setPrimaryClip(ClipData.newPlainText("package-list", json));
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(onSuccess::run, onError::accept));
    }

    private String getExportPackageListContent() {
        Set<String> pkgSet = listModels.stream().map(model -> model.appInfo.getPkgName()).collect(Collectors.toSet());
        return JsonFormatter.toPrettyJson(pkgSet);
    }

    public void exportPackageListToFile(OutputStream os, Runnable onSuccess, Consumer<Throwable> onError) {
        XLog.d("exportPackageListToFile");
        disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            InputStream in = CharSource.wrap(getExportPackageListContent()).asByteSource(Charset.defaultCharset()).openStream();
            ByteStreams.copy(in, os);
            emitter.onSuccess(true);
            IoUtils.closeQuietly(in);
            IoUtils.closeQuietly(os);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> onSuccess.run(), onError::accept));
    }

    public void importPackageListFromClipboard(Consumer<Boolean> onRes) {
        ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null && cmb.hasPrimaryClip()) {
            disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
                try {
                    ClipData.Item item = Objects.requireNonNull(cmb.getPrimaryClip()).getItemAt(0);
                    if (item == null) return;
                    String content = item.getText().toString();
                    XLog.w("importPackageListFromClipboard, content: " + content);
                    Set<String> packageSet = parseJsonToPackages(content);
                    if (!CollectionUtils.isNullOrEmpty(packageSet)) {
                        ThanosManager.from(getApplication()).ifServiceInstalled(thanosManager -> CollectionUtils.consumeRemaining(packageSet, pkg -> thanosManager.getPkgManager().setPkgSmartFreezeEnabled(Pkg.systemUserPkg(pkg), true)));
                        emitter.onSuccess(true);
                        start();
                    } else {
                        emitter.onSuccess(false);
                    }
                } catch (Exception ex) {
                    XLog.e("importPackageListFromClipboard error", Log.getStackTraceString(ex));
                    emitter.onSuccess(false);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onRes::accept));
        }
    }

    private Set<String> parseJsonToPackages(String content) {
        try {
            Set<String> res = new Gson().fromJson(content, new TypeToken<Set<String>>() {
            }.getType());
            if (!CollectionUtils.isNullOrEmpty(res)) {
                return res;
            }
        } catch (Throwable throwable) {
            XLog.e("parseJsonToPackages", throwable);
        }
        return null;
    }

    public void importPackageListFromFile(Uri uri, Consumer<Boolean> onRes) {
        disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                if (inputStream == null) {
                    return;
                }
                @SuppressWarnings("UnstableApiUsage") String content = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                XLog.w("importPackageListFromFile, content: " + content);
                Set<String> packageSet = parseJsonToPackages(content);
                if (!CollectionUtils.isNullOrEmpty(packageSet)) {
                    ThanosManager.from(getApplication()).ifServiceInstalled(thanosManager -> CollectionUtils.consumeRemaining(packageSet, pkg -> thanosManager.getPkgManager().setPkgSmartFreezeEnabled(Pkg.systemUserPkg(pkg), true)));
                    emitter.onSuccess(true);
                    start();
                } else {
                    emitter.onSuccess(false);
                }
            } catch (Throwable ex) {
                XLog.e("importPackageListFromClipboard error", Log.getStackTraceString(ex));
                emitter.onSuccess(false);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onRes::accept));
    }

    public boolean isSortReverse() {
        return sortReverse.get();
    }

    public void setSortReverse(boolean reverse) {
        sortReverse.set(reverse);
        start();
    }

    public AppSort getCurrentAppSort() {
        return currentSort.get();
    }

    public void setAppSort(AppSort sort) {
        currentSort.set(sort);

        if (sort != null) {
            PreferenceManager.getDefaultSharedPreferences(getApplication())
                    .edit()
                    .putString(getAppPrefFeatureId(), sort.name())
                    .apply();
        }

        start();
    }

    public void freezeAllOnCurrentPage() {
        ThanosManager.from(getApplication()).getPkgManager().freezeSmartFreezePackages(
                listModels.stream().map(appListModel -> Pkg.fromAppInfo(appListModel.appInfo)).collect(Collectors.toList()),
                new PackageEnableStateChangeListener() {
                    @Override
                    public void onPackageEnableStateChanged(List<Pkg> pkgs) {
                        super.onPackageEnableStateChanged(pkgs);
                        loadModels();
                    }
                });
    }

    public void freezeAll() {
        ThanosManager.from(getApplication()).getPkgManager().freezeAllSmartFreezePackages(
                new PackageEnableStateChangeListener() {
                    @Override
                    public void onPackageEnableStateChanged(List<Pkg> pkgs) {
                        super.onPackageEnableStateChanged(pkgs);
                        loadModels();
                    }
                });
    }
}
