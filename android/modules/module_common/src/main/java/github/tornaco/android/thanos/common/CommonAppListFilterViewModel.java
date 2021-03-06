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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import lombok.Getter;
import lombok.Setter;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;

public class CommonAppListFilterViewModel extends AndroidViewModel {

    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    protected final List<Disposable> disposables = new ArrayList<>();
    @Getter
    protected final ObservableArrayList<AppListModel> listModels = new ObservableArrayList<>();
    @Getter
    private final ObservableField<CategoryIndex> categoryIndex = new ObservableField<>(CategoryIndex._3rd);

    private final ObservableField<String> queryText = new ObservableField<>("");

    @Setter
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

    public void setAppCategoryFilter(int index) {
        categoryIndex.set(CategoryIndex.values()[index]);
        start();
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

    public interface ListModelLoader {
        @WorkerThread
        List<AppListModel> load(@NonNull CategoryIndex index);
    }
}
