package github.tornaco.android.thanos.core.app.event;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public final class ThanosEvent implements Parcelable {
    private final Intent intent;

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

    public ThanosEvent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    @Override
    public String toString() {
        return "ThanosEvent{" +
                "intent=" + intent +
                '}';
    }
}
