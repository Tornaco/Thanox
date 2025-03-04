package github.tornaco.android.thanos.core.app.start

import android.os.Parcel
import android.os.Parcelable

class StartResult : Parcelable {

    @JvmField
    var res: Boolean = false
    @JvmField
    var why: String

    private constructor(`in`: Parcel) {
        res = `in`.readByte().toInt() != 0
        why = `in`.readString()
    }

    constructor(res: Boolean, why: String) {
        this.res = res
        this.why = why
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeByte((if (res) 1 else 0).toByte())
        parcel.writeString(why)
    }

    override fun toString(): String {
        return "res: $res, why: $why"
    }

    companion object {
        @JvmField
        val BY_PASS_WHITE_LISTED = StartResult(true, "BY_PASS_WHITE_LISTED")
        @JvmField
        val BY_PASS_RECEIVED_PUSH = StartResult(true, "BY_PASS_RECEIVED_PUSH")
        @JvmField
        val BY_PASS_CALLER_WHITE_LISTED = StartResult(true, "BY_PASS_CALLER_WHITE_LISTED")
        @JvmField
        val BY_PASS_HOST_TYPE_WHITE_LISTED = StartResult(true, "BY_PASS_HOST_TYPE_WHITE_LISTED")
        @JvmField
        val BY_PASS_DEFAULT = StartResult(true, "BY_PASS_DEFAULT")
        @JvmField
        val BY_PASS_START_PROCESS_TYPE_NO_CHECK = StartResult(true, "BY_PASS_START_PROCESS_TYPE_NO_CHECK")
        @JvmField
        val BY_PASS_ACCESSIBILITY_SERVICE = StartResult(true, "BY_PASS_ACCESSIBILITY_SERVICE")
        @JvmField
        val BY_PASS_DEFAULT_THANOS_ERROR = StartResult(true, "BY_PASS_DEFAULT_THANOS_ERROR")
        @JvmField
        val BY_PASS_DEFAULT_THANOS_TIMEOUT = StartResult(true, "BY_PASS_DEFAULT_THANOS_TIMEOUT")
        @JvmField
        val BY_PASS_BAD_ARGS = StartResult(true, "BY_PASS_BAD_ARGS")
        @JvmField
        val BY_PASS_START_BLOCKED_DISABLED = StartResult(true, "BY_PASS_START_BLOCKED_DISABLED")
        @JvmField
        val BY_PASS_UI_PRESENT = StartResult(true, "BY_PASS_UI_PRESENT")
        @JvmField
        val BY_PASS_DEFAULT_IME_SERVICE = StartResult(true, "BY_PASS_DEFAULT_IME_SERVICE")
        @JvmField
        val BY_PASS_PROCESS_RUNNING = StartResult(true, "BY_PASS_PROCESS_RUNNING")
        @JvmField
        val BY_PASS_SAME_CALLING_UID = StartResult(true, "BY_PASS_SAME_CALLING_UID")
        @JvmField
        val BY_PASS_SMS_APP = StartResult(true, "BY_PASS_SMS_APP")
        @JvmField
        val BY_PASS_WALLPAPER_COMPONENT = StartResult(true, "BY_PASS_WALLPAPER_COMPONENT")
        @JvmField
        val BLOCKED_STRUGGLE = StartResult(false, "BLOCKED_STRUGGLE")
        @JvmField
        val BLOCKED_PROCESS_IS_KILLED = StartResult(false, "BLOCKED_PROCESS_IS_KILLED")
        @JvmField
        val BLOCKED_IN_BLOCK_LIST = StartResult(false, "BLOCKED_IN_BLOCK_LIST")
        @JvmField
        val BLOCKED_COMPONENT_IS_DISABLED = StartResult(false, "BLOCKED_COMPONENT_IS_DISABLED")
        @JvmField
        val BLOCKED_MAYBE_PUSH_SDK_ACTIVITY = StartResult(false, "BLOCKED_MAYBE_PUSH_SDK_ACTIVITY")
        @JvmField
        val BLOCKED_PRE_START_HYPEROS = StartResult(false, "BLOCKED_PRE_START_HYPEROS")

        @JvmField
        val BLOCKED_USER_RULE = StartResult(false, "BLOCKED_USER_RULE")
        @JvmField
        val BLOCKED_STANDBY = StartResult(false, "BLOCKED_STANDBY")
        @JvmField
        val BYPASS_USER_RULE = StartResult(true, "BYPASS_USER_RULE")

        @JvmField
        val BLOCKED_BLOCK_API = StartResult(false, "BLOCKED_BLOCK_API")

        @JvmField
        val CREATOR: Parcelable.Creator<StartResult> = object : Parcelable.Creator<StartResult> {

            override fun createFromParcel(`in`: Parcel): StartResult {
                return StartResult(`in`)
            }

            override fun newArray(size: Int): Array<StartResult?> {
                return arrayOfNulls(size)
            }
        }
    }
}
