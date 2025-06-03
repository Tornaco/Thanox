package github.tornaco.android.thanos.core.app.component;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.ComponentNameBrief;

@Keep
public final class ComponentReplacement implements Parcelable {

    public ComponentNameBrief from, to;
    public String note;

    private ComponentReplacement(Parcel in) {
        from = in.readParcelable(ComponentNameBrief.class.getClassLoader());
        to = in.readParcelable(ComponentNameBrief.class.getClassLoader());
        note = in.readString();
    }

    public ComponentReplacement(ComponentNameBrief from, ComponentNameBrief to, String note) {
        this.from = from;
        this.to = to;
        this.note = note;
    }

    public ComponentReplacement() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(from, flags);
        dest.writeParcelable(to, flags);
        dest.writeString(note);
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

    @Override
    public String toString() {
        return "ComponentReplacement{" +
                "from=" + from +
                ", to=" + to +
                ", note='" + note + '\'' +
                '}';
    }
}
