package github.tornaco.android.thanos.core.app.event;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public final class ThanosEvent implements Parcelable {
    public static final String ACTION_BASE = "github.tornaco.android.thanos.event.action_";
    public static final String ACTION_TASK_REMOVED = ACTION_BASE + "task_removed";
    public static final String ACTION_FRONT_UI_APP_CHANGED = ACTION_BASE + "front_ui_app_changed";

    private Intent intent;

    private ThanosEvent(Parcel in) {
        intent = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<ThanosEvent> CREATOR = new Creator<ThanosEvent>() {
        @Override
        public ThanosEvent createFromParcel(Parcel in) {
            return new ThanosEvent(in);
        }

        @Override
        public ThanosEvent[] newArray(int size) {
            return new ThanosEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(intent, flags);
    }
}
