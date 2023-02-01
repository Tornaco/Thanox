package github.tornaco.android.thanos.core.n;

import android.content.ClipData;

import java.util.List;

import github.tornaco.android.thanos.core.pm.Pkg;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class NotificationManager {

    private final INotificationManager service;

    /**
     * @deprecated Use {{@link #getShowingNotificationRecordsForPackage(Pkg)}} instead
     */
    @SneakyThrows
    @Deprecated
    public List<NotificationRecord> getShowingNotificationRecordsForPackage(String packageName) {
        return service.getShowingNotificationRecordsForPackage(Pkg.systemUserPkg(packageName));
    }

    @SneakyThrows
    public List<NotificationRecord> getShowingNotificationRecordsForPackage(Pkg pkg) {
        return service.getShowingNotificationRecordsForPackage(pkg);
    }

    @SneakyThrows
    public List<NotificationRecord> getAllNotificationRecordsByPageAndKeyword(int start, int limit, String keyword) {
        return service.getAllNotificationRecordsByPageAndKeyword(start, limit, keyword);
    }

    /**
     * @deprecated Use {{@link #hasShowingNotificationRecordsForPackage(Pkg)}} instead
     */
    @SneakyThrows
    @Deprecated
    public boolean hasShowingNotificationRecordsForPackage(String pkgName) {
        return service.hasShowingNotificationRecordsForPackage(Pkg.systemUserPkg(pkgName));
    }

    @SneakyThrows
    public boolean hasShowingNotificationRecordsForPackage(Pkg pkg) {
        return service.hasShowingNotificationRecordsForPackage(pkg);
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

    @SneakyThrows
    public void onSetPrimaryClip(ClipData clip, Pkg caller) {
        service.onSetPrimaryClip(clip, caller);
    }

    @SneakyThrows
    public void setPackageRedactionNotificationEnabled(Pkg pkg, boolean enable) {
        service.setPackageRedactionNotificationEnabled(pkg, enable);
    }

    @SneakyThrows
    public boolean isPackageRedactionNotificationEnabled(Pkg pkg) {
        return service.isPackageRedactionNotificationEnabled(pkg);
    }

    @SneakyThrows
    public void setPackageRedactionNotificationTitle(Pkg pkg, String title) {
        service.setPackageRedactionNotificationTitle(pkg, title);
    }

    @SneakyThrows
    public String getPackageRedactionNotificationTitle(Pkg pkg) {
        return service.getPackageRedactionNotificationTitle(pkg);
    }

    @SneakyThrows
    public void setPackageRedactionNotificationText(Pkg pkg, String text) {
        service.setPackageRedactionNotificationText(pkg, text);
    }

    @SneakyThrows
    public String getPackageRedactionNotificationText(Pkg pkg) {
        return service.getPackageRedactionNotificationText(pkg);
    }

    @SneakyThrows
    public void setPkgNREnabled(Pkg pkg, boolean enable) {
        service.setPkgNREnabled(pkg, enable);
    }

    @SneakyThrows
    public boolean isPkgNREnabled(Pkg pkg) {
        return service.isPkgNREnabled(pkg);
    }
}
