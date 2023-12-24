package github.tornaco.thanos.android.module.profile;

import android.app.Application;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.profile.ProfileManager;
import github.tornaco.android.thanos.core.profile.RuleAddCallback;
import github.tornaco.android.thanos.core.profile.RuleChangeListener;
import github.tornaco.android.thanos.core.profile.RuleInfo;
import github.tornaco.android.thanos.core.profile.RuleInfoKt;
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
    private final ObservableArrayList<RuleUiItem> ruleInfoList = new ObservableArrayList<>();

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
                RuleUiItem item = ruleInfoList.get(index);
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
                RuleUiItem item = ruleInfoList.get(index);
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
                RuleUiItem item = ruleInfoList.get(i);
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

    void importRuleFromUri(Uri uri) {
        try {
            String ruleString = readTextFromUri(uri);
            XLog.d(ruleString);
            RuleAddCallback callback = new RuleAddCallback() {
                @Override
                protected void onRuleAddSuccess() {
                    super.onRuleAddSuccess();
                    Toast.makeText(getApplication(),
                                    R.string.module_profile_editor_save_success,
                                    Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                protected void onRuleAddFail(int errorCode, String errorMessage) {
                    super.onRuleAddFail(errorCode, errorMessage);
                    ThanosManager.from(getApplication())
                            .getProfileManager()
                            .addRule("Thanox", RuleInfoKt.DEFAULT_RULE_VERSION, ruleString, new RuleAddCallback() {
                                @Override
                                protected void onRuleAddSuccess() {
                                    super.onRuleAddSuccess();
                                    Toast.makeText(getApplication(),
                                                    R.string.module_profile_editor_save_success,
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }

                                @Override
                                protected void onRuleAddFail(int errorCode, String errorMessage) {
                                    super.onRuleAddFail(errorCode, errorMessage);
                                    Toast.makeText(getApplication(),
                                                    errorMessage,
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }, ProfileManager.RULE_FORMAT_YAML);
                }
            };
            // Try json first.
            ThanosManager.from(getApplication())
                    .getProfileManager()
                    .addRule("Thanox", RuleInfoKt.DEFAULT_RULE_VERSION, ruleString, callback, ProfileManager.RULE_FORMAT_JSON);
        } catch (Exception e) {
            XLog.e(e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    void importRuleExamples() {
        try {
            String[] ruleFiles = getApplication().getAssets().list("prebuilt_profile");
            if (ruleFiles == null) {
                return;
            }
            for (String file : ruleFiles) {
                int type = ProfileManager.RULE_FORMAT_JSON;
                if (file.endsWith("yml")) {
                    type = ProfileManager.RULE_FORMAT_YAML;
                }
                InputStream inputStream = getApplication().getAssets().open("prebuilt_profile/" + file);
                String ruleString = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                ThanosManager.from(getApplication())
                        .getProfileManager()
                        .addRuleIfNotExists(
                                "Thanox",
                                1,
                                ruleString, new RuleAddCallback() {
                                    @Override
                                    protected void onRuleAddFail(int errorCode, String errorMessage) {
                                        super.onRuleAddFail(errorCode, errorMessage);
                                        Toast.makeText(getApplication(),
                                                        R.string.module_profile_editor_save_check_error,
                                                        Toast.LENGTH_LONG)
                                                .show();
                                    }

                                    @Override
                                    protected void onRuleAddSuccess() {
                                        super.onRuleAddSuccess();
                                    }
                                }, type);
            }
            Toast.makeText(getApplication(),
                            R.string.module_profile_editor_save_success,
                            Toast.LENGTH_LONG)
                    .show();
        } catch (IOException e) {
            XLog.e(e);
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

    public ObservableArrayList<RuleUiItem> getRuleInfoList() {
        return this.ruleInfoList;
    }

    public void deleteRule(RuleInfo ruleInfo) {
        thanosManager.getProfileManager().deleteRule(ruleInfo.getId());
    }

    public interface RuleLoader {
        @WorkerThread
        List<RuleInfo> load();
    }

    private RuleUiItem toRuleItem(RuleInfo info) {
        // Check for su.
        if (info.getRuleString().contains("su.exe(") && !thanosManager.getProfileManager().isShellSuSupportInstalled()) {
            // Missing su support.
            String warn = getApplication().getString(R.string.module_profile_should_enable_su);
            return new RuleUiItem(info, warn);
        }

        if (info.getRuleString().contains("fcmPushMessageArrived") && !thanosManager.getProfileManager().isProfileEnginePushEnabled()) {
            String warn = getApplication().getString(R.string.module_profile_should_enable_push);
            return new RuleUiItem(info, warn);
        }

        // facts.put("globalVarOf$$it", list)
        List<String> missingGlobalVarNames = RuleAnalyserKt.getMissingGlobalVarNames(getApplication(), info.getRuleString());
        if (!missingGlobalVarNames.isEmpty()) {
            String warn = getApplication().getString(R.string.module_profile_miss_global_var, RuleAnalyserKt.formatGlobalVarNames(missingGlobalVarNames, System.lineSeparator()));
            return new RuleUiItem(info, warn);
        }

        if (info.getRuleString().contains("Thread.sleep") && info.getPriority() >= 0) {
            String warn = getApplication().getString(R.string.module_profile_block_thread);
            return new RuleUiItem(info, warn);
        }


        return new RuleUiItem(info, null);
    }
}
