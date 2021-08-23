package github.tornaco.android.thanos.start;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.rhino.plugin.Verify;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;

public class StartRuleViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ObservableArrayList<StartRule> startRules = new ObservableArrayList<>();

    private RulesLoader loader = new RulesLoader() {
        @Override
        public List<StartRule> load() {
            List<StartRule> res = new ArrayList<>();
            String[] startRules = ThanosManager.from(getApplication()).getActivityManager().getAllStartRules();
            for (String r : startRules) {
                res.add(StartRule.builder().raw(r).build());
            }
            return res;
        }
    };

    public StartRuleViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    @Verify
    public void start() {
        loadModels();
    }

    public void resume() {
        loadModels();
    }

    private void loadModels() {
        if (!ThanosManager.from(getApplication()).isServiceInstalled()) return;
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<StartRule>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(loader.load())))
                .flatMapObservable((Function<List<StartRule>,
                        ObservableSource<StartRule>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> startRules.clear())
                .subscribe(startRules::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
    }

    private void registerEventReceivers() {
    }

    private void unRegisterEventReceivers() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        unRegisterEventReceivers();
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableArrayList<StartRule> getStartRules() {
        return this.startRules;
    }

    public interface RulesLoader {
        @WorkerThread
        List<StartRule> load();
    }
}
