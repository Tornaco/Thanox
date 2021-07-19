package github.tornaco.android.thanos.core.pm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import util.CollectionUtils;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageSet implements Parcelable {
  @Getter
  private String label;
  @Getter
  private String id;
  @Getter
  private long createAt;
  private List<String> pkgNames;

  protected PackageSet(Parcel in) {
    label = in.readString();
    id = in.readString();
    createAt = in.readLong();
    pkgNames = in.createStringArrayList();
    if (pkgNames == null) {
      pkgNames = new ArrayList<>();
    }
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
}
