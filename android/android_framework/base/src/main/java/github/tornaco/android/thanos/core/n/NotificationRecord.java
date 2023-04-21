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
@EqualsAndHashCode(of = {"pkgName", "when", "title", "content", "userId"})
public final class NotificationRecord implements Parcelable {

    public static class Types {
        public static final int TYPE_TOAST = 1;
        public static final int TYPE_GENERAL_NOTIFICATION = 0;
        public static final int TYPE_CLIPBOARD = 2;
    }

    private final int id;
    private final String pkgName;
    private final int userId;
    private final long when;
    private final long creationTime;
    private final String title;
    private final String content;
    private final String tickerText;
    private final String channelId;
    private final String notificationId;
    private final int visibility;
    private final int type;
    private final boolean isForegroundService;
    private final boolean isAutoCancel;

    private NotificationRecord(Parcel in) {
        id = in.readInt();
        pkgName = in.readString();
        userId = in.readInt();
        when = in.readLong();
        creationTime = in.readLong();
        title = in.readString();
        content = in.readString();
        tickerText = in.readString();
        channelId = in.readString();
        notificationId = in.readString();
        visibility = in.readInt();
        type = in.readInt();
        isForegroundService = in.readByte() != 0;
        isAutoCancel = in.readByte() != 0;
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
        parcel.writeInt(userId);
        parcel.writeLong(when);
        parcel.writeLong(creationTime);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(tickerText);
        parcel.writeString(channelId);
        parcel.writeString(notificationId);
        parcel.writeInt(visibility);
        parcel.writeInt(type);
        parcel.writeByte((byte) (isForegroundService ? 1 : 0));
        parcel.writeByte((byte) (isAutoCancel ? 1 : 0));
    }

    public boolean isToast() {
        return type == Types.TYPE_TOAST;
    }

    public boolean isNotification() {
        return type == Types.TYPE_GENERAL_NOTIFICATION;
    }
}
