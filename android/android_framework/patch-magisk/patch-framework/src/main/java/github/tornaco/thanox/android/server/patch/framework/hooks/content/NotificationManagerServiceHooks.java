package github.tornaco.thanox.android.server.patch.framework.hooks.content;

import com.elvishew.xlog.XLog;

import java.util.List;

import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.util.obs.ListProxy;
import now.fortuitous.BootStrap;
import github.tornaco.android.thanos.services.patch.common.LocalServices;
import github.tornaco.android.thanos.services.patch.common.notification.NMSHelper;
import now.fortuitous.util.NotificationRecordUtils;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import util.Consumer;
import util.XposedHelpers;

public class NotificationManagerServiceHooks {

    public static void install(ClassLoader classLoader) {
        installNotificationListHooks(classLoader);
    }

    private static void installNotificationListHooks(ClassLoader classLoader) {
        XLog.i("NotificationManagerServiceHooks.installNotificationListHooks");
        try {
            new LocalServices(classLoader).getService(NMSHelper.INSTANCE.nmsClass(classLoader))
                    .ifPresent(new Consumer<Object>() {
                        @SuppressWarnings("rawtypes")
                        @Override
                        public void accept(Object service) {
                            XLog.i("NotificationManagerServiceHooks.installNotificationListHooks service: %s", service);
                            // Install list proxy.
                            List mNotificationList =
                                    (List) XposedHelpers.getObjectField(service, "mNotificationList");
                            XLog.d("mNotificationList: " + mNotificationList);
                            @SuppressWarnings("unchecked")
                            List proxyList = NotificationRecordListProxy.newProxy(mNotificationList);
                            XposedHelpers.setObjectField(service, "mNotificationList", proxyList);

                            // Attach
                            BootStrap.THANOS_X.getAndroidSystemServices().attachService(service);
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
                        NotificationRecord record = NotificationRecordUtils.fromNotificationRecord(e);
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
                            NotificationRecord record = NotificationRecordUtils.fromNotificationRecord(e);
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
                            NotificationRecord record = NotificationRecordUtils.fromNotificationRecord(removed);
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
                            NotificationRecord record = NotificationRecordUtils.fromNotificationRecord(object);
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
