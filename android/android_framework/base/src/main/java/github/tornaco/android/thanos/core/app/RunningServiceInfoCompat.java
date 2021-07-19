package github.tornaco.android.thanos.core.app;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class RunningServiceInfoCompat implements Parcelable {
    private ComponentName componentName;

    private RunningServiceInfoCompat(Parcel in) {
        componentName = in.readParcelable(ComponentName.class.getClassLoader());
    }

    public static final Creator<RunningServiceInfoCompat> CREATOR = new Creator<RunningServiceInfoCompat>() {
        @Override
        public RunningServiceInfoCompat createFromParcel(Parcel in) {
            return new RunningServiceInfoCompat(in);
        }

        @Override
        public RunningServiceInfoCompat[] newArray(int size) {
            return new RunningServiceInfoCompat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(componentName, i);
    }
}
