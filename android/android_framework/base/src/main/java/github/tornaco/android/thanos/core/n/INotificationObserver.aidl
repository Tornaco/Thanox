package github.tornaco.android.thanos.core.n;

oneway interface INotificationObserver {
    void onNewNotification(in NotificationRecord record);
    void onNotificationRemoved(in NotificationRecord record);
    void onNotificationUpdated(in NotificationRecord record);
    void onNotificationClicked(in NotificationRecord record);
}