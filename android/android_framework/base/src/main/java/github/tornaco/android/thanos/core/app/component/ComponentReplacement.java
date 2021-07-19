package github.tornaco.android.thanos.core.app.component;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class ComponentReplacement implements Parcelable {

    public ComponentName from, to;

    private ComponentReplacement(Parcel in) {
        from = in.readParcelable(ComponentName.class.getClassLoader());
        to = in.readParcelable(ComponentName.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(from, flags);
        dest.writeParcelable(to, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ComponentReplacement> CREATOR = new Creator<ComponentReplacement>() {
        @Override
        public ComponentReplacement createFromParcel(Parcel in) {
            return new ComponentReplacement(in);
        }

        @Override
        public ComponentReplacement[] newArray(int size) {
            return new ComponentReplacement[size];
        }
    };
}
