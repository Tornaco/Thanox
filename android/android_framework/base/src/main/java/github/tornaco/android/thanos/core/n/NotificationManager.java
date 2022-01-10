package github.tornaco.android.thanos.core.n;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class NotificationManager {

    private final INotificationManager service;

    @SneakyThrows
    public List<NotificationRecord> getShowingNotificationRecordsForPackage(String packageName) {
        return service.getShowingNotificationRecordsForPackage(packageName);
    }

    @SneakyThrows
    public List<NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, String keyword) {
        return service.getAllNotificationRecordsByPageAndKeyword(start, limit, keyword);
    }

    @SneakyThrows
    public boolean hasShowingNotificationRecordsForPackage(String packageName) {
        return service.hasShowingNotificationRecordsForPackage(packageName);
    }

    @SneakyThrows
    public void registerObserver(INotificationObserver obs) {
        service.registerObserver(obs);
    }

    @SneakyThrows
    public void unRegisterObserver(INotificationObserver obs) {
        service.unRegisterObserver(obs);
    }

    @SneakyThrows
    public void setScreenOnNotificationEnabledForPkg(String pkg, boolean enable) {
        service.setScreenOnNotificationEnabledForPkg(pkg, enable);
    }

    @SneakyThrows
    public boolean isScreenOnNotificationEnabledForPkg(String pkg) {
        return service.isScreenOnNotificationEnabledForPkg(pkg);
    }

    @SneakyThrows
    public void setScreenOnNotificationEnabled(boolean enable) {
        service.setScreenOnNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isScreenOnNotificationEnabled() {
        return service.isScreenOnNotificationEnabled();
    }

    @SneakyThrows
    public void setPersistOnNewNotificationEnabled(boolean enable) {
        service.setPersistOnNewNotificationEnabled(enable);
    }

    @SneakyThrows
    public boolean isPersistOnNewNotificationEnabled() {
        return service.isPersistOnNewNotificationEnabled();
    }

    @SneakyThrows
    public void cleanUpPersistNotificationRecords() {
        service.cleanUpPersistNotificationRecords();
    }

    @SneakyThrows
    public List<NotificationRecord> getAllNotificationRecordsByPage(int start, int limit) {
        return service.getAllNotificationRecordsByPage(start, limit);
    }

    @SneakyThrows
    public List<NotificationRecord> getAllNotificationRecordsByPageAndKeywordInDateRange(int start, int limit, long startTimeMills, long endTimeMills, String keyword) {
        return service.getAllNotificationRecordsByPageAndKeywordInDateRange(start, limit, startTimeMills, endTimeMills, keyword);
    }

    @SneakyThrows
    public List<NotificationRecord> getNotificationRecordsForPackage(String packageName) {
        return service.getNotificationRecordsForPackage(packageName);
    }

    @SneakyThrows
    public void setShowToastAppInfoEnabled(boolean enabled) {
        service.setShowToastAppInfoEnabled(enabled);
    }

    @SneakyThrows
    public boolean isShowToastAppInfoEnabled() {
        return service.isShowToastAppInfoEnabled();
    }

    @SneakyThrows
    public boolean isNREnabled(int type) {
        return service.isNREnabled(type);
    }

    @SneakyThrows
    public void setNREnabled(int type, boolean enabled) {
        service.setNREnabled(type, enabled);
    }
}
