package github.tornaco.android.thanos.common;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.PackageSet;
import github.tornaco.android.thanos.core.pm.PrebuiltPkgSetsKt;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.ObjectsUtils;

public class CommonAppListFilterViewModel extends AndroidViewModel {
    public static final CategoryIndex DEFAULT_CATEGORY_INDEX = CategoryIndex.from(PrebuiltPkgSetsKt.PREBUILT_PACKAGE_SET_ID_3RD);

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    protected final List<Disposable> disposables = new ArrayList<>();
    protected final ObservableArrayList<AppListModel> listModels = new ObservableArrayList<>();
    private final ObservableField<CategoryIndex> categoryIndex = new ObservableField<>(DEFAULT_CATEGORY_INDEX);

    private final ObservableField<String> queryText = new ObservableField<>("");

    private ListModelLoader listModelLoader;

    public CommonAppListFilterViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    public void start() {
        loadModels();
    }

    private void loadModels() {
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create(new SingleOnSubscribe<List<AppListModel>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<AppListModel>> emitter) throws Exception {
                        emitter.onSuccess(Objects.requireNonNull(listModelLoader.load(Objects.requireNonNull(categoryIndex.get()))));
                    }
                })
                .flatMapObservable((Function<List<AppListModel>, ObservableSource<AppListModel>>) Observable::fromIterable)
                .filter(new Predicate<AppListModel>() {
                    @Override
                    public boolean test(AppListModel listModel) throws Exception {
                        String query = queryText.get();
                        return TextUtils.isEmpty(query)
                                || listModel.appInfo.getAppLabel().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listModels.clear();
                    }
                })
                .subscribe(new Consumer<AppListModel>() {
                    @Override
                    public void accept(AppListModel object) throws Exception {
                        listModels.add(object);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        XLog.e(Log.getStackTraceString(throwable));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        isDataLoading.set(false);
                    }
                }));
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

    public void setAppCategoryFilter(String id) {
        categoryIndex.set(CategoryIndex.from(id));
        start();
    }

    List<PackageSet> getAllPackageSetFilterItems() {
        ThanosManager thanox = ThanosManager.from(getApplication());
        if (!thanox.isServiceInstalled()) {
            return Lists.newArrayListWithCapacity(0);
        }
        return thanox.getPkgManager().getAllPackageSets();
    }

    PackageSet getCurrentPackageSet() {
        CategoryIndex currentIndex = categoryIndex.get();
        if (currentIndex == null) {
            return null;
        }
        List<PackageSet> all = getAllPackageSetFilterItems();
        if (all.isEmpty()) {
            return null;
        }
        for (PackageSet set : all) {
            if (ObjectsUtils.equals(set.getId(), currentIndex.pkgSetId)) {
                return set;
            }
        }
        return null;
    }

    void clearSearchText() {
        queryText.set(null);
        loadModels();
    }

    void setSearchText(String query) {
        if (TextUtils.isEmpty(query)) return;
        queryText.set(query);
        loadModels();
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableArrayList<AppListModel> getListModels() {
        return this.listModels;
    }

    public ObservableField<CategoryIndex> getCategoryIndex() {
        return this.categoryIndex;
    }

    public void setListModelLoader(ListModelLoader listModelLoader) {
        this.listModelLoader = listModelLoader;
    }

    public interface ListModelLoader {
        @WorkerThread
        List<AppListModel> load(@NonNull CategoryIndex index);
    }
}
