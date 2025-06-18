package github.tornaco.thanos.android.module.profile;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.io.CharStreams;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.GlobalVar;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;

public class GlobalVarViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ObservableArrayList<GlobalVar> globalVars = new ObservableArrayList<>();

    private GlobalVarLoader loader = () -> new ArrayList<>(Arrays.asList(
            ThanosManager.from(getApplication())
                    .getProfileManager()
                    .getAllGlobalRuleVar()));

    public GlobalVarViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }


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
                .create((SingleOnSubscribe<List<GlobalVar>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(loader.load())))
                .flatMapObservable((Function<List<GlobalVar>,
                        ObservableSource<GlobalVar>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> globalVars.clear())
                .subscribe(globalVars::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
    }

    private void registerEventReceivers() {
    }

    private void unRegisterEventReceivers() {
    }

    @SuppressWarnings("UnstableApiUsage")
    private String readTextFromUri(Uri uri) {
        try {
            return CharStreams.toString(new InputStreamReader(
                    Objects.requireNonNull(getApplication().getContentResolver().openInputStream(uri))
                    , StandardCharsets.UTF_8));
        } catch (Exception e) {
            XLog.e(e);
            return null;
        }
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

    public ObservableArrayList<GlobalVar> getGlobalVars() {
        return this.globalVars;
    }

    public interface GlobalVarLoader {
        @WorkerThread
        List<GlobalVar> load();
    }
}
