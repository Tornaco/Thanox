package github.tornaco.android.thanos.core.n;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = {"pkgName", "when", "title", "content"})
public final class NotificationRecord implements Parcelable {

    public static class Types {
        public static final int TYPE_TOAST = 1;
        public static final int TYPE_GENERAL_NOTIFICATION = 0;
    }

    private int id;
    private String pkgName;
    private long when;
    private long creationTime;
    private String title;
    private String content;
    private String tickerText;
    private String channelId;
    private String notificationId;
    private int visibility;
    private int type;

    private NotificationRecord(Parcel in) {
        id = in.readInt();
        pkgName = in.readString();
        when = in.readLong();
        creationTime = in.readLong();
        title = in.readString();
        content = in.readString();
        tickerText = in.readString();
        channelId = in.readString();
        notificationId = in.readString();
        visibility = in.readInt();
        type = in.readInt();
    }

    public static final Creator<NotificationRecord> CREATOR = new Creator<NotificationRecord>() {
        @Override
        public NotificationRecord createFromParcel(Parcel in) {
            return new NotificationRecord(in);
        }

        @Override
        public NotificationRecord[] newArray(int size) {
            return new NotificationRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(pkgName);
        parcel.writeLong(when);
        parcel.writeLong(creationTime);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(tickerText);
        parcel.writeString(channelId);
        parcel.writeString(notificationId);
        parcel.writeInt(visibility);
        parcel.writeInt(type);
    }

    public boolean isToast() {
        return type == Types.TYPE_TOAST;
    }
}
