package github.tornaco.android.thanos.power;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.common.AppListModel;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.android.thanos.util.InstallerUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;

public class SmartFreezeAppsViewModel extends AndroidViewModel {
    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    protected final List<Disposable> disposables = new ArrayList<>();
    @Getter
    protected final ObservableArrayList<AppListModel> listModels = new ObservableArrayList<>();

    public SmartFreezeAppsViewModel(@NonNull Application application) {
        super(application);
    }

    void start() {
        loadModels();
    }

    @Verify
    private void loadModels() {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<AppListModel>>) emitter ->
                        emitter.onSuccess(getSmartFreezeApps()))
                .map(listModels -> {
                    Collections.sort(listModels);
                    return listModels;
                })
                .flatMapObservable((Function<List<AppListModel>, ObservableSource<AppListModel>>) Observable::fromIterable)
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
        List<AppListModel> res = new ArrayList<>();
        for (String pkg : thanosManager.getPkgManager().getSmartFreezePkgs()) {
            AppInfo appInfo = thanosManager.getPkgManager().getAppInfo(pkg);
            if (appInfo != null) {
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
                        throwable -> Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_LONG).show()));
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
}
