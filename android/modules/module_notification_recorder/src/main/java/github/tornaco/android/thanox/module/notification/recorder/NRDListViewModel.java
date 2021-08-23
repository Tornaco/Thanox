package github.tornaco.android.thanox.module.notification.recorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.elvishew.xlog.XLog;

import java.util.ArrayList;
import java.util.List;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.DateUtils;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import rx2.android.schedulers.AndroidSchedulers;
import util.CollectionUtils;
import util.Consumer;

public class NRDListViewModel extends AndroidViewModel {

    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final CompositeDisposable disposables = new CompositeDisposable();
    protected final MutableLiveData<List<NotificationRecordModelGroup>> recordModelGroups = new MutableLiveData<>();

    private final ObservableField<String> queryText = new ObservableField<>("");

    public NRDListViewModel(@NonNull Application application) {
        super(application);
        registerEventReceivers();
    }

    public void start() {
        loadModels();
    }

    public void clearAllRecords() {
        ThanosManager.from(getApplication())
                .ifServiceInstalled(new Consumer<ThanosManager>() {
                    @Override
                    public void accept(ThanosManager thanosManager) {
                        thanosManager.getNotificationManager().cleanUpPersistNotificationRecords();
                    }
                });
    }

    private void loadModels() {
        disposables.clear();
        recordModelGroups.postValue(new ArrayList<>(0));
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<NotificationRecordModelGroup>>) emitter ->
                        emitter.onSuccess(loadModelsSync()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<NotificationRecordModelGroup>, Throwable>() {
                    @Override
                    public void accept(List<NotificationRecordModelGroup> notificationRecordModelGroups,
                                       Throwable throwable) throws Exception {
                        isDataLoading.set(false);
                        recordModelGroups.postValue(notificationRecordModelGroups);
                        XLog.i("loadModels error? %s", throwable);
                    }
                }));
    }

    private void registerEventReceivers() {
    }

    private void unRegisterEventReceivers() {
    }

    private List<NotificationRecordModelGroup> loadModelsSync() {
        List<NotificationRecordModelGroup> res = new ArrayList<>();

        NotificationRecordModelGroup today = new NotificationRecordModelGroup(
                getApplication().getString(R.string.module_notification_recorder_toady),
                new ArrayList<>());
        NotificationRecordModelGroup otherDay = new NotificationRecordModelGroup(
                getApplication().getString(R.string.module_notification_recorder_before_today),
                new ArrayList<>());

        String keyword = queryText.get();
        ThanosManager.from(getApplication()).ifServiceInstalled(
                thanosManager -> {
                    long todayTimeInMills = DateUtils.getToadyStartTimeInMills();
                    CollectionUtils.consumeRemaining(
                            thanosManager.getNotificationManager().getAllNotificationRecordsByPageAndKeyword(0, 2048, keyword),
                            notificationRecord -> {
                                XLog.v("loadModelsSync notificationRecord: %s", notificationRecord);

                                AppInfo appInfo = thanosManager
                                        .getPkgManager()
                                        .getAppInfo(notificationRecord.getPkgName());
                                if (appInfo != null && appInfo.isSystemUid()) {
                                    return;
                                }
                                boolean isToday = notificationRecord.getWhen() >= todayTimeInMills;
                                String timeF = isToday ?
                                        DateUtils.formatShortForMessageTime(notificationRecord.getWhen())
                                        : DateUtils.formatLongForMessageTime(notificationRecord.getWhen());
                                NotificationRecordModel model = new NotificationRecordModel(notificationRecord, appInfo, timeF);
                                if (isToday) {
                                    today.getModels().add(model);
                                } else {
                                    otherDay.getModels().add(model);
                                }
                            });
                });

        if (!CollectionUtils.isNullOrEmpty(today.getModels())) {
            res.add(today);
        }
        if (!CollectionUtils.isNullOrEmpty(otherDay.getModels())) {
            res.add(otherDay);
        }

        if (res.isEmpty()) {
            NotificationRecordModelGroup emptyTips = new NotificationRecordModelGroup(
                    "\uD83D\uDC40",
                    new ArrayList<>());
            res.add(emptyTips);
        }

        return res;
    }

    void clearSearchText() {
        queryText.set(null);
        loadModels();
    }

    void setSearchText(String query) {
        queryText.set(query);
        loadModels();
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

    public MutableLiveData<List<NotificationRecordModelGroup>> getRecordModelGroups() {
        return this.recordModelGroups;
    }
}
