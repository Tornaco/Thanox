package github.tornaco.android.thanos.core.pm;

import static github.tornaco.android.thanos.core.pm.PrebuiltPkgSetsKt.USER_PACKAGE_SET_ID_USER_WHITELISTED;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import util.CollectionUtils;

public class PackageSet implements Parcelable {
    private String label;
    private String id;
    private long createAt;
    private List<String> pkgNames;
    private boolean isPrebuilt;
    private String description;

    protected PackageSet(Parcel in) {
        label = in.readString();
        id = in.readString();
        createAt = in.readLong();
        pkgNames = in.createStringArrayList();
        if (pkgNames == null) {
            pkgNames = new ArrayList<>();
        }
        isPrebuilt = in.readInt() == 1;
        description = in.readString();
    }

    public PackageSet(String label, String id, long createAt, List<String> pkgNames, boolean isPrebuilt, String description) {
        this.label = label;
        this.id = id;
        this.createAt = createAt;
        this.pkgNames = pkgNames;
        this.isPrebuilt = isPrebuilt;
        this.description = description;
    }

    public PackageSet() {
    }

    public static PackageSetBuilder builder() {
        return new PackageSetBuilder();
    }

    public List<String> getPkgNames() {
        if (pkgNames == null) {
            pkgNames = new ArrayList<>();
        }
        return pkgNames;
    }

    public void addPackage(String pkg) {
        if (pkgNames == null) {
            pkgNames = new ArrayList<>();
        }
        pkgNames.add(pkg);
    }

    public void removePackage(String pkg) {
        if (pkgNames == null) {
            pkgNames = new ArrayList<>();
        }
        pkgNames.remove(pkg);
    }

    public int getPackageCount() {
        return CollectionUtils.sizeOf(pkgNames);
    }

    public boolean isPrebuilt() {
        return isPrebuilt;
    }

    public boolean isUserWhiteListed() {
        return USER_PACKAGE_SET_ID_USER_WHITELISTED.equals(id);
    }

    public static final Creator<PackageSet> CREATOR = new Creator<PackageSet>() {
        @Override
        public PackageSet createFromParcel(Parcel in) {
            return new PackageSet(in);
        }

        @Override
        public PackageSet[] newArray(int size) {
            return new PackageSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(id);
        parcel.writeLong(createAt);
        parcel.writeStringList(pkgNames);
        parcel.writeInt(isPrebuilt ? 1 : 0);
        parcel.writeString(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackageSet that = (PackageSet) o;
        return createAt == that.createAt &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createAt);
    }

    public String toString() {
        return "PackageSet(label=" + this.label + ", id=" + this.id + ", createAt=" + this.createAt + ", pkgNames=" + this.getPkgNames() + ")";
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public long getCreateAt() {
        return this.createAt;
    }

    public static class PackageSetBuilder {
        private String label;
        private String id;
        private long createAt;
        private List<String> pkgNames;
        private boolean isPrebuilt;
        private String description;

        PackageSetBuilder() {
        }

        public PackageSet.PackageSetBuilder label(String label) {
            this.label = label;
            return this;
        }

        public PackageSet.PackageSetBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PackageSet.PackageSetBuilder id(String id) {
            this.id = id;
            return this;
        }

        public PackageSet.PackageSetBuilder createAt(long createAt) {
            this.createAt = createAt;
            return this;
        }

        public PackageSet.PackageSetBuilder pkgNames(List<String> pkgNames) {
            this.pkgNames = pkgNames;
            return this;
        }

        public PackageSet.PackageSetBuilder prebuilt(boolean isPrebuilt) {
            this.isPrebuilt = isPrebuilt;
            return this;
        }

        public PackageSet build() {
            return new PackageSet(label, id, createAt, pkgNames, isPrebuilt, description);
        }
    }
}
