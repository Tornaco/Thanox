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

val PermState.isGrant get() = this == PermState.ASK || this == PermState.ALLOW_FOREGROUND_ONLY || this == PermState.ALLOW_ALWAYS
val PermState.isOneTime get() = this == PermState.ASK
val PermState.isUserFixed get() = this == PermState.DENY
val PermState.isUserSet get() = this == PermState.DENY

data class PermInfo(
    val permState: PermState,
    val hasBackgroundPermission: Boolean,
    val isRuntimePermission: Boolean,
    val isSupportOneTimeGrant: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        permState = PermState.valueOf(parcel.readString()),
        hasBackgroundPermission = parcel.readByte() != 0.toByte(),
        isRuntimePermission = parcel.readByte() != 0.toByte(),
        isSupportOneTimeGrant = parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(permState.name)
        dest?.writeByte(if (hasBackgroundPermission) 1 else 0)
        dest?.writeByte(if (isRuntimePermission) 1 else 0)
        dest?.writeByte(if (isSupportOneTimeGrant) 1 else 0)
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