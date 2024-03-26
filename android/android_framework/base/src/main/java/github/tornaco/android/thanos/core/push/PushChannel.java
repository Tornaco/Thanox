package github.tornaco.android.thanos.core.push;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Objects;

import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.annotation.NonNull;
import github.tornaco.android.thanos.core.util.ArrayUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Keep
public final class PushChannel implements Parcelable {

    public static final PushChannel FCM_GCM = new PushChannel(new String[]{
            "com.google.android.c2dm.intent.RECEIVE",
            "com.google.firebase.MESSAGING_EVENT"
    }, "google:fcm/gcm", "B75F00CB-D413-4E35-BBA1-80BB6DD0ADBB");

    public static final PushChannel GCM = new PushChannel(new String[]{
            "com.google.android.c2dm.intent.RECEIVE",
    }, "google:gcm", "82D094AC-95B1-40C9-B037-8A88F8309AE6");

    public static final PushChannel FCM = new PushChannel(new String[]{
            "com.google.firebase.MESSAGING_EVENT"
    }, "google:fcm", "74EC7A26-5597-4C37-BD3C-A827A074EC02");

    @NonNull
    private final String[] actions;
    @NonNull
    private final String channelName;

    private final String channelId;

    private PushChannel(Parcel in) {
        actions = in.readStringArray();
        channelName = in.readString();
        channelId = in.readString();
    }

    public PushChannel(String[] actions, String channelName, String channelId) {
        this.actions = actions;
        this.channelName = channelName;
        this.channelId = channelId;
    }

    public String[] getActions() {
        return actions;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public static final Creator<PushChannel> CREATOR = new Creator<PushChannel>() {
        @Override
        public PushChannel createFromParcel(Parcel in) {
            return new PushChannel(in);
        }

        @Override
        public PushChannel[] newArray(int size) {
            return new PushChannel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(actions);
        parcel.writeString(channelName);
        parcel.writeString(channelId);
    }

    public boolean match(Intent intent) {
        return intent != null && ArrayUtils.contains(actions, intent.getAction());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushChannel that = (PushChannel) o;
        return Arrays.equals(actions, that.actions) &&
                channelName.equals(that.channelName) &&
                channelId.equals(that.channelId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(channelName, channelId);
        result = 31 * result + Arrays.hashCode(actions);
        return result;
    }
}
