package github.tornaco.android.thanos.core.profile

import android.os.Parcel
import android.os.Parcelable

const val DEFAULT_RULE_VERSION = 1

data class RuleInfo(
    val id: Int,
    var name: String,
    var description: String,
    var ruleString: String,
    var author: String,
    var updateTimeMills: Long,
    var enabled: Boolean,
    var format: Int,
    var versionCode: Int
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(ruleString)
        parcel.writeString(author)
        parcel.writeLong(updateTimeMills)
        parcel.writeByte(if (enabled) 1 else 0)
        parcel.writeInt(format)
        parcel.writeInt(versionCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleInfo

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (ruleString != other.ruleString) return false
        if (author != other.author) return false
        if (format != other.format) return false
        if (versionCode != other.versionCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + ruleString.hashCode()
        result = 31 * result + format
        result = 31 * result + versionCode
        return result
    }

    companion object CREATOR : Parcelable.Creator<RuleInfo> {
        override fun createFromParcel(parcel: Parcel): RuleInfo {
            return RuleInfo(parcel)
        }

        override fun newArray(size: Int): Array<RuleInfo?> {
            return arrayOfNulls(size)
        }
    }


}
