package github.tornaco.android.thanos.core.secure.field;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class Fields implements Parcelable {
  private String label;
  private String id;
  private long createAt;

  private String deviceId;
  private String androidId;
  private String line1Number;
  private String simSerial;

  private String simCountryIso;
  private String simOperatorName;
  private String simOperator;


  private String netCountryIso;
  private String netOperatorName;
  private String netOperator;

  // Support at most 3 slots
  private String imeiForSlots0;
  private String imeiForSlots1;
  private String imeiForSlots2;

  // Support at most 3 slots
  private String meidForSlots0;
  private String meidForSlots1;
  private String meidForSlots2;

  private boolean showN;

  private Fields(Parcel in) {
    label = in.readString();
    id = in.readString();
    createAt = in.readLong();
    deviceId = in.readString();
    androidId = in.readString();
    line1Number = in.readString();
    simSerial = in.readString();

    simCountryIso = in.readString();
    simOperatorName = in.readString();
    simOperator = in.readString();

    netCountryIso = in.readString();
    netOperatorName = in.readString();
    netOperator = in.readString();

    imeiForSlots0 = in.readString();
    imeiForSlots1 = in.readString();
    imeiForSlots2 = in.readString();
    meidForSlots0 = in.readString();
    meidForSlots1 = in.readString();
    meidForSlots2 = in.readString();
    showN = in.readInt() == 1;
  }

  public static final Creator<Fields> CREATOR =
      new Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel in) {
          return new Fields(in);
        }

        @Override
        public Fields[] newArray(int size) {
          return new Fields[size];
        }
      };


  public String getLabel() {
    return label;
  }

  public String getId() {
    return id;
  }

  public long getCreateAt() {
    return createAt;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getAndroidId() {
    return androidId;
  }

  public String getLine1Number() {
    return line1Number;
  }

  public String getSimSerial() {
    return simSerial;
  }

  public String getSimCountryIso() {
    return simCountryIso;
  }

  public String getSimOperatorName() {
    return simOperatorName;
  }

  public String getSimOperator() {
    return simOperator;
  }

  public String getNetCountryIso() {
    return netCountryIso;
  }

  public String getNetOperatorName() {
    return netOperatorName;
  }

  public String getNetOperator() {
    return netOperator;
  }

  public String getImeiForSlots0() {
    return imeiForSlots0;
  }

  public String getImeiForSlots1() {
    return imeiForSlots1;
  }

  public String getImeiForSlots2() {
    return imeiForSlots2;
  }

  public String getMeidForSlots0() {
    return meidForSlots0;
  }

  public String getMeidForSlots1() {
    return meidForSlots1;
  }

  public String getMeidForSlots2() {
    return meidForSlots2;
  }

  public boolean isShowN() {
    return showN;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(label);
    parcel.writeString(id);
    parcel.writeLong(createAt);
    parcel.writeString(deviceId);
    parcel.writeString(androidId);
    parcel.writeString(line1Number);
    parcel.writeString(simSerial);

    parcel.writeString(simCountryIso);
    parcel.writeString(simOperatorName);
    parcel.writeString(simOperator);


    parcel.writeString(netCountryIso);
    parcel.writeString(netOperatorName);
    parcel.writeString(netOperator);

    parcel.writeString(imeiForSlots0);
    parcel.writeString(imeiForSlots1);
    parcel.writeString(imeiForSlots2);
    parcel.writeString(meidForSlots0);
    parcel.writeString(meidForSlots1);
    parcel.writeString(meidForSlots2);
    parcel.writeInt(showN ? 1 : 0);
  }

  public boolean validate() {
    return !TextUtils.isEmpty(id);
  }

  public String getImei(int slotIndex) {
    switch (slotIndex) {
      case 1:
        return imeiForSlots1;
      case 2:
        return imeiForSlots2;
      default:
        return imeiForSlots0;
    }
  }

  public void setImei(int slotIndex, String value) {
    switch (slotIndex) {
      case 1:
        imeiForSlots1 = value;
        break;
      case 2:
        imeiForSlots2 = value;
        break;
      default:
        imeiForSlots0 = value;
        break;
    }
  }

  public void setMeid(int slotIndex, String value) {
    switch (slotIndex) {
      case 1:
        meidForSlots1 = value;
        break;
      case 2:
        meidForSlots2 = value;
        break;
      default:
        meidForSlots0 = value;
        break;
    }
  }

  public String getMeid(int slotIndex) {
    switch (slotIndex) {
      case 1:
        return meidForSlots1;
      case 2:
        return meidForSlots2;
      default:
        return meidForSlots0;
    }
  }
}
