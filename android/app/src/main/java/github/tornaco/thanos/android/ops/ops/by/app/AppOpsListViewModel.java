package github.tornaco.thanos.android.ops.ops.by.app;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.thanos.android.ops.model.Op;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.ops.repo.PkgOpsLoader;
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

public class AppOpsListViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    protected final ObservableArrayList<OpGroup> opGroups = new ObservableArrayList<>();

    private final ObservableInt categoryIndex = new ObservableInt(PkgOpsLoader.FILTER_ALL /* All */);

    public AppOpsListViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    public void start(AppInfo appInfo) {
        loadModels(appInfo);
    }

    private void loadModels(AppInfo appInfo) {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<OpGroup>>) emitter ->
                        emitter.onSuccess(new PkgOpsLoader().getPkgOps(getApplication(), appInfo, categoryIndex.get())))
                .flatMapObservable((Function<List<OpGroup>, ObservableSource<OpGroup>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> opGroups.clear())
                .subscribe(opGroups::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
    }

    private void registerEventReceivers() {
    }

    private void unRegisterEventReceivers() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionUtils.consumeRemaining(disposables, Disposable::dispose);
        unRegisterEventReceivers();
    }

    void switchOp(@NonNull Op op, AppInfo appInfo, int selectedMode) {
        ThanosManager thanos = ThanosManager.from(getApplication());
        if (thanos.isServiceInstalled()) {
            thanos.getAppOpsManager().setMode(
                    op.getCode(),
                    appInfo.getUid(),
                    appInfo.getPkgName(),
                    selectedMode);
            op.setMode(selectedMode);
        }
    }

    void setModeFilter(int index) {
        categoryIndex.set(index);
    }

    public void selectAll(AppInfo appInfo, int mode) {
        CollectionUtils.consumeRemaining(opGroups, new Consumer<OpGroup>() {
            @Override
            public void accept(OpGroup opGroup) {
                CollectionUtils.consumeRemaining(opGroup.getOpList(), new Consumer<Op>() {
                    @Override
                    public void accept(Op op) {
                        switchOp(op, appInfo, mode);
                    }
                });
            }
        });
        start(appInfo);
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableArrayList<OpGroup> getOpGroups() {
        return this.opGroups;
    }
}
