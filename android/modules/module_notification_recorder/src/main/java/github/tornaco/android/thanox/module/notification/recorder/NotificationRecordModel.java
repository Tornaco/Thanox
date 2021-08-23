package github.tornaco.android.thanox.module.notification.recorder;

import androidx.annotation.Nullable;

import github.tornaco.android.thanos.core.n.NotificationRecord;
import github.tornaco.android.thanos.core.pm.AppInfo;

public class NotificationRecordModel {
    private NotificationRecord record;
    @Nullable
    private AppInfo appInfo;
    private String formattedTime;

    public NotificationRecordModel(NotificationRecord record, AppInfo appInfo, String formattedTime) {
        this.record = record;
        this.appInfo = appInfo;
        this.formattedTime = formattedTime;
    }

    public NotificationRecord getRecord() {
        return this.record;
    }

    @Nullable
    public AppInfo getAppInfo() {
        return this.appInfo;
    }

    public String getFormattedTime() {
        return this.formattedTime;
    }

    public void setRecord(NotificationRecord record) {
        this.record = record;
    }

    public void setAppInfo(@Nullable AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NotificationRecordModel))
            return false;
        final NotificationRecordModel other = (NotificationRecordModel) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$record = this.getRecord();
        final Object other$record = other.getRecord();
        if (this$record == null ? other$record != null : !this$record.equals(other$record))
            return false;
        final Object this$appInfo = this.getAppInfo();
        final Object other$appInfo = other.getAppInfo();
        if (this$appInfo == null ? other$appInfo != null : !this$appInfo.equals(other$appInfo))
            return false;
        final Object this$formattedTime = this.getFormattedTime();
        final Object other$formattedTime = other.getFormattedTime();
        if (this$formattedTime == null ? other$formattedTime != null : !this$formattedTime.equals(other$formattedTime))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NotificationRecordModel;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $record = this.getRecord();
        result = result * PRIME + ($record == null ? 43 : $record.hashCode());
        final Object $appInfo = this.getAppInfo();
        result = result * PRIME + ($appInfo == null ? 43 : $appInfo.hashCode());
        final Object $formattedTime = this.getFormattedTime();
        result = result * PRIME + ($formattedTime == null ? 43 : $formattedTime.hashCode());
        return result;
    }

    public String toString() {
        return "NotificationRecordModel(record=" + this.getRecord() + ", appInfo=" + this.getAppInfo() + ", formattedTime=" + this.getFormattedTime() + ")";
    }
}
