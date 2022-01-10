package github.tornaco.android.thanos.core.n;

import github.tornaco.android.thanos.core.n.INotificationObserver;
import github.tornaco.android.thanos.core.n.NotificationRecord;

interface INotificationManager {

    List<NotificationRecord> getShowingNotificationRecordsForPackage(in String packageName);
    boolean hasShowingNotificationRecordsForPackage(in String packageName);

    void registerObserver(in INotificationObserver obs);
    void unRegisterObserver(in INotificationObserver obs);

    void setScreenOnNotificationEnabled(boolean enable);
    boolean isScreenOnNotificationEnabled();

    void setScreenOnNotificationEnabledForPkg(String pkg, boolean enable);
    boolean isScreenOnNotificationEnabledForPkg(String pkg);

    int nextNotificationId();

    void setPersistOnNewNotificationEnabled(boolean enable);
    boolean isPersistOnNewNotificationEnabled();

    void cleanUpPersistNotificationRecords();
    List<NotificationRecord> getAllNotificationRecordsByPage(int start, int limit);
    List<NotificationRecord> getNotificationRecordsForPackage(String packageName);

    void onAddNotificationRecord(in NotificationRecord record);
    void setShowToastAppInfoEnabled(boolean enabled);
    boolean isShowToastAppInfoEnabled();

    boolean isNREnabled(int type);
    void setNREnabled(int type, boolean enabled);

    // For searching.
    List<NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, String keyword);
    List<NotificationRecord> getAllNotificationRecordsByPageAndKeywordInDateRange(int start, int limit, long startTimeMills, long endTimeMills, String keyword);
}