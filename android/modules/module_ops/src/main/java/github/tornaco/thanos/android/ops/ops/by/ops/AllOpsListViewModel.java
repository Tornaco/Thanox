package github.tornaco.thanos.android.ops.ops.by.ops;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.util.Rxs;
import github.tornaco.thanos.android.ops.model.OpGroup;
import github.tornaco.thanos.android.ops.ops.repo.AllOpsLoader;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import rx2.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import util.CollectionUtils;

public class AllOpsListViewModel extends AndroidViewModel {

    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    @Getter
    protected final ObservableArrayList<OpGroup> opGroups = new ObservableArrayList<>();

    public AllOpsListViewModel(@NonNull Application application) {
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
                .create((SingleOnSubscribe<List<OpGroup>>) emitter ->
                        emitter.onSuccess(new AllOpsLoader().getAllOps(getApplication())))
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
}
