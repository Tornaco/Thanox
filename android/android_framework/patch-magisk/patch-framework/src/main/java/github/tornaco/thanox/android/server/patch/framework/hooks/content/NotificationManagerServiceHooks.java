package github.tornaco.thanox.android.server.patch.framework.hooks.content;

import com.android.server.notification.NotificationManagerService;
import com.elvishew.xlog.XLog;

import java.util.List;

import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.util.obs.ListProxy;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.util.NotificationRecordUtils;
import github.tornaco.thanox.android.server.patch.framework.LocalServices;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import util.Consumer;
import util.XposedHelpers;

public class NotificationManagerServiceHooks {
    public static void install() {
        installNotificationListHooks();
    }

    private static void installNotificationListHooks() {
        XLog.i("NotificationManagerServiceHooks.installNotificationListHooks");
        try {
            LocalServices.getService(NotificationManagerService.class)
                    .ifPresent(new Consumer<NotificationManagerService>() {
                        @SuppressWarnings("rawtypes")
                        @Override
                        public void accept(NotificationManagerService service) {
                            XLog.i("NotificationManagerServiceHooks.installNotificationListHooks service: %s", service);
                            // Install list proxy.
                            List mNotificationList =
                                    (List) XposedHelpers.getObjectField(service, "mNotificationList");
                            XLog.d("mNotificationList: " + mNotificationList);
                            @SuppressWarnings("unchecked")
                            List proxyList = NotificationRecordListProxy.newProxy(mNotificationList);
                            XposedHelpers.setObjectField(service, "mNotificationList", proxyList);
                        }
                    });
        } catch (Throwable e) {
            XLog.e("NotificationManagerServiceHooks.installNotificationListHooks error", e);
        }
    }


    private static class NotificationRecordListProxy<T> extends ListProxy<T> {

        NotificationRecordListProxy(List<T> orig) {
            super(orig);
        }

        static <T> NotificationRecordListProxy<T> newProxy(List<T> orig) {
            return new NotificationRecordListProxy<>(orig);
        }

        @Override
        public void add(int i, T e) {
            super.add(i, e);
            Completable.fromRunnable(
                    () -> {
                        NotificationRecord record = NotificationRecordUtils.fromLegacy(e);
                        if (record != null) {
                            BootStrap.THANOS_X
                                    .getNotificationManagerService()
                                    .onAddNotificationRecord(record);
                        }
                    })
                    .subscribeOn(Schedulers.trampoline())
                    .subscribe();
        }

        @Override
        public boolean add(T e) {
            boolean added = super.add(e);
            if (added) {
                Completable.fromRunnable(
                        () -> {
                            NotificationRecord record = NotificationRecordUtils.fromLegacy(e);
                            if (record != null) {
                                BootStrap.THANOS_X
                                        .getNotificationManagerService()
                                        .onAddNotificationRecord(record);
                            }
                        })
                        .subscribeOn(Schedulers.trampoline())
                        .subscribe();
            }
            return added;
        }

        @Override
        public T remove(int i) {
            T removed = super.remove(i);
            if (removed != null) {
                Completable.fromRunnable(
                        () -> {
                            NotificationRecord record = NotificationRecordUtils.fromLegacy(removed);
                            if (record != null) {
                                BootStrap.THANOS_X
                                        .getNotificationManagerService()
                                        .onRemoveNotificationRecord(record);
                            }
                        })
                        .subscribeOn(Schedulers.trampoline())
                        .subscribe();
            }
            return removed;
        }

        @Override
        public boolean remove(Object object) {
            boolean removed = super.remove(object);
            if (removed) {
                Completable.fromRunnable(
                        () -> {
                            NotificationRecord record = NotificationRecordUtils.fromLegacy(object);
                            if (record != null) {
                                BootStrap.THANOS_X
                                        .getNotificationManagerService()
                                        .onRemoveNotificationRecord(record);
                            }
                        })
                        .subscribeOn(Schedulers.trampoline())
                        .subscribe();
            }
            return removed;
        }
    }
}
