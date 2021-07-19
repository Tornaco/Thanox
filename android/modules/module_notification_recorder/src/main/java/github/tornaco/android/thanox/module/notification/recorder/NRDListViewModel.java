package github.tornaco.android.thanox.module.notification.recorder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.util.DateUtils;
import github.tornaco.android.thanos.core.util.Rxs;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import rx2.android.schedulers.AndroidSchedulers;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;
import util.CollectionUtils;
import util.Consumer;

public class NRDListViewModel extends AndroidViewModel {

    @Getter
    private final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    private final List<Disposable> disposables = new ArrayList<>();
    @Getter
    protected final ObservableArrayList<NotificationRecordModelGroup> recordModelGroups = new ObservableArrayList<>();
    @Getter
    private final ObservableInt nCount = new ObservableInt(0);

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
        if (isDataLoading.get()) return;
        isDataLoading.set(true);
        disposables.add(Single
                .create((SingleOnSubscribe<List<NotificationRecordModelGroup>>) emitter ->
                        emitter.onSuccess(loadModelsSync()))
                .flatMapObservable((Function<List<NotificationRecordModelGroup>, ObservableSource<NotificationRecordModelGroup>>) Observable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> recordModelGroups.clear())
                .subscribe(recordModelGroups::add,
                        Rxs.ON_ERROR_LOGGING,
                        () -> {
                            isDataLoading.set(false);
                            nCount.set(countModulesOfCurrentLoadedGroups());
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

        ThanosManager.from(getApplication()).ifServiceInstalled(
                thanosManager -> {
                    long todayTimeInMills = DateUtils.getToadyStartTimeInMills();
                    CollectionUtils.consumeRemaining(
                            thanosManager.getNotificationManager()
                                    .getAllNotificationRecordsByPage(0, 1024),
                            notificationRecord -> {
                                AppInfo appInfo = thanosManager
                                        .getPkgManager()
                                        .getAppInfo(notificationRecord.getPkgName());
                                if (appInfo != null && appInfo.isSystemUid()) {
                                    return;
                                }
                                String timeF = FuzzyDateTimeFormatter
                                        .getTimeAgo(getApplication(), new Date(notificationRecord.getWhen()));
                                NotificationRecordModel model = new NotificationRecordModel(notificationRecord, appInfo, timeF);
                                if (notificationRecord.getWhen() >= todayTimeInMills) {
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
        return res;
    }

    private int countModulesOfCurrentLoadedGroups() {
        if (CollectionUtils.isNullOrEmpty(recordModelGroups)) return 0;
        AtomicInteger c = new AtomicInteger(0);
        CollectionUtils.consumeRemaining(recordModelGroups, new Consumer<NotificationRecordModelGroup>() {
            @Override
            public void accept(NotificationRecordModelGroup notificationRecordModelGroup) {
                c.addAndGet(CollectionUtils.sizeOf(notificationRecordModelGroup.getModels()));
            }
        });
        return c.get();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        CollectionUtils.consumeRemaining(disposables, Disposable::dispose);
        unRegisterEventReceivers();
    }
}
