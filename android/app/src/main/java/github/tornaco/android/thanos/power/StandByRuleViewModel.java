package github.tornaco.android.thanos.power;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import github.tornaco.android.thanos.core.util.ClipboardUtils;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.Consumer;

public class StandByRuleViewModel extends AndroidViewModel {

    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    @Getter
    private final ObservableArrayList<StandbyRule> standbyRules = new ObservableArrayList<>();

    private RulesLoader loader = new RulesLoader() {
        @Override
        public List<StandbyRule> load() {
            List<StandbyRule> res = new ArrayList<>();
            CollectionUtils.consumeRemaining(
                    ThanosManager.from(getApplication())
                            .getActivityManager()
                            .getAllStandbyRules(),
                    new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            res.add(StandbyRule.builder().raw(s).build());
                        }
                    });
            return res;
        }
    };

    public StandByRuleViewModel(@NonNull Application application) {
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
                .create((SingleOnSubscribe<List<StandbyRule>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(loader.load())))
                .flatMapObservable((Function<List<StandbyRule>,
                        ObservableSource<StandbyRule>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> standbyRules.clear())
                .subscribe(standbyRules::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
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

    boolean importRulesFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = Objects.requireNonNull(clipboardManager).getPrimaryClip();
        if (clipData == null) {
            return false;
        }
        ClipData.Item item = clipData.getItemAt(0);
        String content = String.valueOf(item.getText());
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        try {
            String[] lines = content.split("\\r?\\n");
            XLog.v("Rules: %s", Arrays.toString(lines));
            if (!ArrayUtils.isEmpty(lines)) {
                CollectionUtils.consumeRemaining(lines, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        ThanosManager.from(getApplication())
                                .getActivityManager()
                                .addStandbyRule(s);
                    }
                });
                return true;
            }
        } catch (Throwable e) {
            XLog.e(Log.getStackTraceString(e));
        }
        return false;
    }

    void exportAllRulesToClipboard() {
        List<String> rules = Lists.newArrayList(ThanosManager.from(getApplication())
                .getActivityManager().getAllStandbyRules());
        StringBuilder sb = new StringBuilder();
        for (String r : rules) {
            sb.append(r).append(System.lineSeparator());
        }
        ClipboardUtils.copyToClipboard(getApplication(), "rules", sb.toString());
    }

    public interface RulesLoader {
        @WorkerThread
        List<StandbyRule> load();
    }
}
