package github.tornaco.android.thanox.module.notification.recorder;

import java.util.List;

public class NotificationRecordModelGroup {
    private String dateDesc;
    private List<NotificationRecordModel> models;

    public NotificationRecordModelGroup(String dateDesc, List<NotificationRecordModel> models) {
        this.dateDesc = dateDesc;
        this.models = models;
    }

    public String getDateDesc() {
        return this.dateDesc;
    }

    public List<NotificationRecordModel> getModels() {
        return this.models;
    }

    public void setDateDesc(String dateDesc) {
        this.dateDesc = dateDesc;
    }

    public void setModels(List<NotificationRecordModel> models) {
        this.models = models;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NotificationRecordModelGroup))
            return false;
        final NotificationRecordModelGroup other = (NotificationRecordModelGroup) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$dateDesc = this.getDateDesc();
        final Object other$dateDesc = other.getDateDesc();
        if (this$dateDesc == null ? other$dateDesc != null : !this$dateDesc.equals(other$dateDesc))
            return false;
        final Object this$models = this.getModels();
        final Object other$models = other.getModels();
        if (this$models == null ? other$models != null : !this$models.equals(other$models))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof NotificationRecordModelGroup;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dateDesc = this.getDateDesc();
        result = result * PRIME + ($dateDesc == null ? 43 : $dateDesc.hashCode());
        final Object $models = this.getModels();
        result = result * PRIME + ($models == null ? 43 : $models.hashCode());
        return result;
    }

    public String toString() {
        return "NotificationRecordModelGroup(dateDesc=" + this.getDateDesc() + ", models=" + this.getModels() + ")";
    }
}
