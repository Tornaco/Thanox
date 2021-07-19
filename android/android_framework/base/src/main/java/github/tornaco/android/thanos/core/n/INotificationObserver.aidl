package github.tornaco.android.thanos.core.n;

oneway interface INotificationObserver {
    void onNewNotification(in NotificationRecord record);
    void onNotificationRemoved(in NotificationRecord record);
}