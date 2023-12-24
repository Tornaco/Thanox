package github.tornaco.android.thanox.module.activity.trampoline;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import github.tornaco.android.thanos.common.AppLabelSearchFilter;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.component.ComponentReplacement;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.pm.ComponentNameBrief;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.IoUtils;
import util.JsonFormatter;
import util.PinyinComparatorUtils;

public class TrampolineViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    private final ObservableArrayList<ActivityTrampolineModel> replacements = new ObservableArrayList<>();

    private final ObservableInt exportSuccessSignal = new ObservableInt();
    private final ObservableInt exportFailSignal = new ObservableInt();
    private final ObservableInt importSuccessSignal = new ObservableInt();
    private final ObservableInt importFailSignal = new ObservableInt();

    private final ObservableField<String> queryText = new ObservableField<>("");
    private final AppLabelSearchFilter appLabelSearchFilter = new AppLabelSearchFilter();

    private final TrampolineLoader trampolineLoader = () -> {
        List<ActivityTrampolineModel> res = new ArrayList<>();
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> CollectionUtils.consumeRemaining(thanosManager.getActivityStackSupervisor()
                        .getComponentReplacements(), componentReplacement -> {
                    AppInfo appInfo = thanosManager.getPkgManager().getAppInfo(componentReplacement.from.getPackageName());
                    ActivityTrampolineModel model = new ActivityTrampolineModel(componentReplacement, appInfo);
                    res.add(model);
                }));
        Collections.sort(res, (o1, o2) -> {
            if (o1.getApp() != null && o2.getApp() == null) return -1;
            if (o1.getApp() == null && o2.getApp() != null) return 1;
            if (o1.getApp() != null && o2.getApp() != null) {
                return PinyinComparatorUtils.compare(o1.getApp().getAppLabel(), o2.getApp().getAppLabel());
            }
            return 0;
        });
        return res;
    };

    public TrampolineViewModel(@NonNull Application application) {
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
                .create((SingleOnSubscribe<List<ActivityTrampolineModel>>) emitter ->
                        emitter.onSuccess(Objects.requireNonNull(trampolineLoader.load())))
                .flatMapObservable((Function<List<ActivityTrampolineModel>,
                        ObservableSource<ActivityTrampolineModel>>) Observable::fromIterable)
                .filter(listModel -> {
                    String query = queryText.get();
                    // Match package or cpmponent.
                    return TextUtils.isEmpty(query)
                            || query.length() > 1 && listModel.getReplacement() != null && listModel.getReplacement().from.flattenToString().contains(query)
                            || query.length() > 1 && listModel.getReplacement() != null && listModel.getReplacement().to.flattenToString().contains(query)
                            || query.length() > 1 && listModel.getApp() != null && listModel.getApp().getPkgName().contains(query)
                            || listModel.getApp() != null && appLabelSearchFilter.matches(query, listModel.getApp().getAppLabel());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> replacements.clear())
                .subscribe(replacements::add, Rxs.ON_ERROR_LOGGING, () -> isDataLoading.set(false)));
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

    void onRequestAddNewReplacement(ComponentNameBrief f, ComponentNameBrief t) {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> {
                    thanosManager.getActivityStackSupervisor()
                            .addComponentReplacement(new ComponentReplacement(f, t));
                    // Reload.
                    loadModels();
                });
    }

    void onRequestRemoveNewReplacement(ComponentNameBrief f, ComponentNameBrief t) {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(thanosManager -> {
                    thanosManager.getActivityStackSupervisor()
                            .removeComponentReplacement(new ComponentReplacement(f, t));
                    // Reload.
                    loadModels();
                });
    }

    void exportToClipboard(@Nullable String componentReplacementKey) {
        XLog.d("exportToClipboard: %s", JsonFormatter.toPrettyJson(ComponentNameBrief.unflattenFromString("com.a.c/.xxx")));
        XLog.d("exportToClipboard: %s", componentReplacementKey);
        disposables.add(Single.create((SingleOnSubscribe<String>) emitter -> {
                    List<ComponentReplacement> componentReplacements = new ArrayList<>();
                    for (ActivityTrampolineModel model : replacements) {
                        if (componentReplacementKey == null
                                || componentReplacementKey.equals(model.getReplacement().from.flattenToString())) {
                            componentReplacements.add(model.getReplacement());
                        }
                    }
                    XLog.d("exportToClipboard: %s", Arrays.toString(componentReplacements.toArray()));
                    emitter.onSuccess(JsonFormatter.toPrettyJson(componentReplacements));
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(content -> {
                    ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (cmb != null) {
                        XLog.w("content: " + content);
                        cmb.setPrimaryClip(ClipData.newPlainText("trampoline", content));
                        exportSuccessSignal.set(exportSuccessSignal.get() + 1);
                    }
                }));
    }

    void exportToFile(OutputStream os, @Nullable String componentReplacementKey) {
        XLog.d("exportToFile: %s", componentReplacementKey);
        disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
                    List<ComponentReplacement> componentReplacements = new ArrayList<>();
                    for (ActivityTrampolineModel model : replacements) {
                        if (componentReplacementKey == null
                                || componentReplacementKey.equals(model.getReplacement().from.flattenToString())) {
                            componentReplacements.add(model.getReplacement());
                        }
                    }
                    String contentToWrite = JsonFormatter.toPrettyJson(componentReplacements);
                    //noinspection UnstableApiUsage
                    InputStream in = CharSource.wrap(contentToWrite).asByteSource(Charset.defaultCharset()).openStream();
                    //noinspection UnstableApiUsage
                    emitter.onSuccess(ByteStreams.copy(in, os) > 0);

                    IoUtils.closeQuietly(in);
                    IoUtils.closeQuietly(os);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (res) exportSuccessSignal.set(exportSuccessSignal.get() + 1);
                    else exportFailSignal.set(exportFailSignal.get() + 1);
                }));
    }

    void importFromClipboard() {
        ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null && cmb.hasPrimaryClip()) {
            disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
                try {
                    ClipData.Item item = Objects.requireNonNull(cmb.getPrimaryClip()).getItemAt(0);
                    if (item == null) return;
                    String content = item.getText().toString();
                    XLog.w("importFromClipboard, content: " + content);
                    List<ComponentReplacement> componentReplacements = parseJson(content);
                    if (!CollectionUtils.isNullOrEmpty(componentReplacements)) {
                        ThanosManager.from(getApplication())
                                .ifServiceInstalled(thanosManager ->
                                        CollectionUtils.consumeRemaining(componentReplacements, replacement ->
                                                thanosManager
                                                        .getActivityStackSupervisor()
                                                        .addComponentReplacement(replacement)));
                        emitter.onSuccess(true);
                        start();
                    } else {
                        emitter.onSuccess(false);
                    }
                } catch (Exception ex) {
                    XLog.e(Log.getStackTraceString(ex));
                    emitter.onSuccess(false);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
                if (res) importSuccessSignal.set(importSuccessSignal.get() + 1);
                else importFailSignal.set(importFailSignal.get() + 1);
            }));
        }
    }

    void importFromFile(Uri uri) {
        disposables.add(Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            try {
                InputStream inputStream = getApplication()
                        .getContentResolver()
                        .openInputStream(uri);
                if (inputStream == null) {
                    return;
                }
                @SuppressWarnings("UnstableApiUsage")
                String content = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                XLog.w("content: " + content);
                List<ComponentReplacement> componentReplacements = parseJson(content);
                if (!CollectionUtils.isNullOrEmpty(componentReplacements)) {
                    ThanosManager.from(getApplication())
                            .ifServiceInstalled(thanosManager ->
                                    CollectionUtils.consumeRemaining(componentReplacements, replacement ->
                                            thanosManager
                                                    .getActivityStackSupervisor()
                                                    .addComponentReplacement(replacement)));
                    emitter.onSuccess(true);
                    start();
                } else {
                    emitter.onSuccess(false);
                }
            } catch (IOException ioe) {
                XLog.e(Log.getStackTraceString(ioe));
                emitter.onSuccess(false);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(res -> {
            if (res) importSuccessSignal.set(importSuccessSignal.get() + 1);
            else importFailSignal.set(importFailSignal.get() + 1);
        }));
    }

    private List<ComponentReplacement> parseJson(String content) {
        try {
            List<ComponentReplacement> componentReplacements = new Gson()
                    .fromJson(content, new TypeToken<List<ComponentReplacement>>() {
                    }.getType());
            if (!CollectionUtils.isNullOrEmpty(componentReplacements)) {
                return componentReplacements;
            }
        } catch (Throwable throwable) {
            XLog.e(throwable);
        }
        return null;
    }

    public ObservableBoolean getIsDataLoading() {
        return this.isDataLoading;
    }

    public ObservableArrayList<ActivityTrampolineModel> getReplacements() {
        return this.replacements;
    }

    public ObservableInt getExportSuccessSignal() {
        return this.exportSuccessSignal;
    }

    public ObservableInt getExportFailSignal() {
        return this.exportFailSignal;
    }

    public ObservableInt getImportSuccessSignal() {
        return this.importSuccessSignal;
    }

    public ObservableInt getImportFailSignal() {
        return this.importFailSignal;
    }

    void clearSearchText() {
        setSearchText(null);
    }

    void setSearchText(String query) {
        queryText.set(query);
        disposables.clear();
        loadModels();
    }

    public interface TrampolineLoader {
        @WorkerThread
        List<ActivityTrampolineModel> load();
    }
}
