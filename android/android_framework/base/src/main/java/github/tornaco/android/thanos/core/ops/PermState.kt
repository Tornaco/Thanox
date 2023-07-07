package github.tornaco.android.thanos.core.ops

import android.os.Parcel
import android.os.Parcelable

enum class PermState {
    // Align with System. no UID mode.
    DEFAULT,

    // Has not been set
    NOT_SET,

    // Requested/Set but denied
    DENY,
    IGNORE,

    // Ask everytime, for Runtime perm only?
    ASK,

    ALLOW_ALWAYS,
    ALLOW_FOREGROUND_ONLY,

    // May got error
    UNKNOWN
}

data class PermInfo(
    val permState: PermState,
    val hasBackgroundPermission: Boolean,
    val isRuntimePermission: Boolean,
    val isSupportOneTimeGrant: Boolean,
    val opAccessSummary: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        permState = PermState.valueOf(parcel.readString()),
        hasBackgroundPermission = parcel.readByte() != 0.toByte(),
        isRuntimePermission = parcel.readByte() != 0.toByte(),
        isSupportOneTimeGrant = parcel.readByte() != 0.toByte(),
        opAccessSummary = parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(permState.name)
        dest?.writeByte(if (hasBackgroundPermission) 1 else 0)
        dest?.writeByte(if (isRuntimePermission) 1 else 0)
        dest?.writeByte(if (isSupportOneTimeGrant) 1 else 0)
        dest?.writeString(opAccessSummary)
    }

    companion object CREATOR : Parcelable.Creator<PermInfo> {
        override fun createFromParcel(parcel: Parcel): PermInfo {
            return PermInfo(parcel)
        }

        override fun newArray(size: Int): Array<PermInfo?> {
            return arrayOfNulls(size)
        }
    }
}