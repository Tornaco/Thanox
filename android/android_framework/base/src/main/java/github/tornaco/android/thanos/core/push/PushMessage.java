package github.tornaco.android.thanos.core.push;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.UUID;

import github.tornaco.android.thanos.core.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by guohao4 on 2018/1/29.
 * Email: Tornaco@163.com
 */

// Sample:

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushMessage implements Parcelable {
    public static final int IMPORTANCE_MAX = 0;
    public static final int TYPE_APP_UPDATE = 0x1;
    public static final String DATA_SCHEMA_FIREBASE_BODY = "body";

    private String title;
    private String message;
    private int type;
    private String[] payload;
    private long timeMills;
    private int from;
    private String messageId;
    private int importance;
    private boolean isTest;

    // Notification settings.
    private String channelId;
    private String channelName;
    private String largeIconResName;
    private String smallIconResName;

    // Host.
    private String targetPackageName;

    protected PushMessage(Parcel in) {
        title = in.readString();
        message = in.readString();
        type = in.readInt();
        payload = in.readStringArray();
        timeMills = in.readLong();
        from = in.readInt();
        messageId = in.readString();
        importance = in.readInt();
        isTest = in.readByte() != 0;
        channelId = in.readString();
        channelName = in.readString();
        largeIconResName = in.readString();
        smallIconResName = in.readString();
        targetPackageName = in.readString();
    }

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };

    public static PushMessage makeDummy() {
        PushMessage p = new PushMessage();
        p.setImportance(0);
        p.setMessage("Hello world!");
        p.setMessageId(UUID.randomUUID().toString());
        p.setPayload(new String[]{"www.google.com"});
        p.setTest(false);
        p.setTimeMills(System.currentTimeMillis());
        p.setTitle("New message");
        p.setType(0);

        return p;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Nullable
    public static PushMessage fromJson(String js) {
        try {
            return new Gson().fromJson(js, PushMessage.class);
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
        dest.writeInt(type);
        dest.writeStringArray(payload);
        dest.writeLong(timeMills);
        dest.writeInt(from);
        dest.writeString(messageId);
        dest.writeInt(importance);
        dest.writeByte((byte) (isTest ? 1 : 0));
        dest.writeString(channelId);
        dest.writeString(channelName);
        dest.writeString(largeIconResName);
        dest.writeString(smallIconResName);
        dest.writeString(targetPackageName);
    }
}
