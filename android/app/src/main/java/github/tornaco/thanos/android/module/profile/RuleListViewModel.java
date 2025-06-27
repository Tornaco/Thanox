package github.tornaco.thanos.android.module.profile;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.documentfile.provider.DocumentFile;
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
import github.tornaco.android.thanos.core.profile.RuleChangeListener;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.PinyinComparatorUtils;

public class RuleListViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ObservableArrayList<github.tornaco.thanos.android.module.profile.RuleUiItem> ruleInfoList = new ObservableArrayList<>();

    private final ThanosManager thanosManager;

    private final RuleLoader loader = () -> {
        List<RuleInfo> res = new ArrayList<>(Arrays.asList(
                ThanosManager.from(getApplication())
                        .getProfileManager()
                        .getAllRules()));
        res.sort((o1, o2) -> {
            if (o1.getEnabled() && !o2.getEnabled()) {
                return -1;
            }
            if (!o1.getEnabled() && o2.getEnabled()) {
                return 1;
            }
            return PinyinComparatorUtils.compare(o1.getName(), o2.getName());
        });
        return res;
    };

    private final RuleChangeListener ruleChangeListener = new RuleChangeListener() {
        @Override
        protected void onRuleEnabledStateChanged(int ruleId, boolean enabled) {
            super.onRuleEnabledStateChanged(ruleId, enabled);
            for (int index = 0; index < ruleInfoList.size(); index++) {
                github.tornaco.thanos.android.module.profile.RuleUiItem item = ruleInfoList.get(index);
                RuleInfo info = item.ruleInfo;
                if (info.getId() == ruleId) {
                    XLog.i("RuleListViewModel onRuleEnabledStateChanged update enable state");
                    info.setEnabled(enabled);
                    ruleInfoList.set(index, item);
                }
            }
        }

        @Override
        protected void onRuleUpdated(int ruleId) {
            super.onRuleUpdated(ruleId);
            for (int index = 0; index < ruleInfoList.size(); index++) {
                github.tornaco.thanos.android.module.profile.RuleUiItem item = ruleInfoList.get(index);
                RuleInfo info = item.ruleInfo;
                if (info.getId() == ruleId) {
                    RuleInfo updatedRule = thanosManager.getProfileManager().getRuleById(ruleId);
                    if (updatedRule != null) {
                        XLog.i("RuleListViewModel onRuleUpdated update rule");
                        ruleInfoList.set(index, toRuleItem(updatedRule));
                    }
                }
            }
        }

        @Override
        protected void onRuleRemoved(int ruleId) {
            super.onRuleRemoved(ruleId);
            int index = -1;
            for (int i = 0; i < ruleInfoList.size(); i++) {
                github.tornaco.thanos.android.module.profile.RuleUiItem item = ruleInfoList.get(i);
                RuleInfo info = item.ruleInfo;
                if (info.getId() == ruleId) {
                    index = i;
                }
            }
            if (index >= 0) {
                XLog.i("RuleListViewModel onRuleRemoved remove rule");
                ruleInfoList.remove(index);
            }
        }

        @Override
        protected void onRuleAdd(int ruleId) {
            super.onRuleAdd(ruleId);
            RuleInfo updatedRule = thanosManager.getProfileManager().getRuleById(ruleId);
            XLog.i("RuleListViewModel onRuleAdd add rule");
            ruleInfoList.add(0, toRuleItem(updatedRule));
        }
    };

    public RuleListViewModel(@NonNull Application application) {
        super(application);
        this.thanosManager = ThanosManager.from(application);
        registerEventReceivers();
    }


    public void start() {
        loadModels();
    }

    private void loadModels() {
        if (!thanosManager.isServiceInstalled()) return;
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single.create((SingleOnSubscribe<List<RuleInfo>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(loader.load())))
                .flatMapObservable((Function<List<RuleInfo>,
                        ObservableSource<RuleInfo>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> ruleInfoList.clear())
                .subscribe(info -> ruleInfoList.add(toRuleItem(info)), Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
    }

    private void registerEventReceivers() {
        if (thanosManager.isServiceInstalled()) {
            thanosManager.getProfileManager().registerRuleChangeListener(ruleChangeListener);
        }
    }

    private void unRegisterEventReceivers() {
        if (thanosManager.isServiceInstalled()) {
            thanosManager.getProfileManager().unRegisterRuleChangeListener(ruleChangeListener);
        }
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

    void importRule(DocumentFile file) {
        RuleListActivityMenuHandlerKt.helperImportFromFile(this, file);
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

    public ObservableArrayList<github.tornaco.thanos.android.module.profile.RuleUiItem> getRuleInfoList() {
        return this.ruleInfoList;
    }

    public void deleteRule(RuleInfo ruleInfo) {
        thanosManager.getProfileManager().deleteRule(ruleInfo.getId());
    }

    public interface RuleLoader {
        @WorkerThread
        List<RuleInfo> load();
    }

    private github.tornaco.thanos.android.module.profile.RuleUiItem toRuleItem(RuleInfo info) {
        // Check for su.
        if (info.getRuleString().contains("su.exe(") && !thanosManager.getProfileManager().isShellSuSupportInstalled()) {
            // Missing su support.
            String warn = getApplication().getString(github.tornaco.android.thanos.res.R.string.module_profile_should_enable_su);
            return new github.tornaco.thanos.android.module.profile.RuleUiItem(info, warn);
        }

        if (info.getRuleString().contains("fcmPushMessageArrived") && !thanosManager.getProfileManager().isProfileEnginePushEnabled()) {
            String warn = getApplication().getString(github.tornaco.android.thanos.res.R.string.module_profile_should_enable_push);
            return new github.tornaco.thanos.android.module.profile.RuleUiItem(info, warn);
        }

        // facts.put("globalVarOf$$it", list)
        List<String> missingGlobalVarNames = github.tornaco.thanos.android.module.profile.RuleAnalyserKt.getMissingGlobalVarNames(getApplication(), info.getRuleString());
        if (!missingGlobalVarNames.isEmpty()) {
            String warn = getApplication().getString(github.tornaco.android.thanos.res.R.string.module_profile_miss_global_var, RuleAnalyserKt.formatGlobalVarNames(missingGlobalVarNames, System.lineSeparator()));
            return new github.tornaco.thanos.android.module.profile.RuleUiItem(info, warn);
        }

        if (info.getRuleString().contains("Thread.sleep") && info.getPriority() >= 0) {
            String warn = getApplication().getString(github.tornaco.android.thanos.res.R.string.module_profile_block_thread);
            return new github.tornaco.thanos.android.module.profile.RuleUiItem(info, warn);
        }


        return new RuleUiItem(info, null);
    }
}
