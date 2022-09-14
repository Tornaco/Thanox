/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.android.thanos.core.alarm

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class AlarmRecord(
    val alarm: Alarm,
    val isEnabled: Boolean = false,
    val createTimeMillis: Long,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Alarm::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(alarm, flags)
        parcel.writeByte(if (isEnabled) 1 else 0)
        parcel.writeLong(createTimeMillis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlarmRecord> {
        override fun createFromParcel(parcel: Parcel): AlarmRecord {
            return AlarmRecord(parcel)
        }

        override fun newArray(size: Int): Array<AlarmRecord?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Represent an Alarm that alert at a time, that can repeat on week days.
 * @param label Unique label for this alarm.
 * */
data class Alarm(
    val label: String,
    val triggerAt: TimeOfADay,
    val repeat: Repeat = Repeat(),
) : Parcelable {

    val triggerDateToday: Date
        get() = android.icu.util.Calendar.getInstance().apply {
            time = Date()
            set(android.icu.util.Calendar.HOUR_OF_DAY, triggerAt.hour)
            set(android.icu.util.Calendar.MINUTE, triggerAt.minutes)
            set(android.icu.util.Calendar.SECOND, triggerAt.seconds)
        }.time

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(TimeOfADay::class.java.classLoader),
        parcel.readParcelable(Repeat::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeParcelable(triggerAt, flags)
        parcel.writeParcelable(repeat, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alarm> {
        override fun createFromParcel(parcel: Parcel): Alarm {
            return Alarm(parcel)
        }

        override fun newArray(size: Int): Array<Alarm?> {
            return arrayOfNulls(size)
        }
    }
}

data class Repeat(
    val days: List<WeekDay> = emptyList(),
) : Parcelable {
    val isNo = days.isEmpty()
    val isEveryDay = days.containsAll(WeekDay.values().toList())

    constructor(parcel: Parcel) : this(parcel.readStringArray().map {
        WeekDay.valueOf(it)
    })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(days.map { it.name }.toTypedArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Repeat> {
        override fun createFromParcel(parcel: Parcel): Repeat {
            return Repeat(parcel)
        }

        override fun newArray(size: Int): Array<Repeat?> {
            return arrayOfNulls(size)
        }
    }

    fun isRepeatToday(): Boolean {
        val today = Calendar.getInstance().apply {
            time = Date()
        }.get(Calendar.DAY_OF_WEEK)

        return days.firstOrNull {
            it.toCalendarInt() == today
        } != null
    }
}

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    fun toCalendarInt() = when (this) {
        SUNDAY -> Calendar.SUNDAY
        SATURDAY -> Calendar.SATURDAY
        FRIDAY -> Calendar.FRIDAY
        THURSDAY -> Calendar.THURSDAY
        WEDNESDAY -> Calendar.WEDNESDAY
        TUESDAY -> Calendar.TUESDAY
        MONDAY -> Calendar.MONDAY
    }
}


data class TimeOfADay(
    val hour: Int,
    val minutes: Int,
    val seconds: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(hour)
        parcel.writeInt(minutes)
        parcel.writeInt(seconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeOfADay> {
        override fun createFromParcel(parcel: Parcel): TimeOfADay {
            return TimeOfADay(parcel)
        }

        override fun newArray(size: Int): Array<TimeOfADay?> {
            return arrayOfNulls(size)
        }
    }
}

fun Alarm.getTriggerTimeOffset(): Long {
    val timeMillisOffset = (triggerDateToday.time - System.currentTimeMillis())
    val nextTriggerDayOfWeekOffset = getNextTriggerDayOfWeekOffset()
    return timeMillisOffset + (nextTriggerDayOfWeekOffset * 24 * 60 * 60 * 1000)
}

fun Alarm.getNextTriggerDayOfWeekOffset(): Int {
    // No repeat
    if (repeat.isNo) {
        return 0
    }

    val todayWeekToday =
        Calendar.getInstance().apply { time = Date() }.get(Calendar.DAY_OF_WEEK)

    // Today will trigger
    val hasRepeatToday =
        repeat.days.firstOrNull { it.toCalendarInt() == todayWeekToday } != null
    val isCurrentBeforeTodayTriggerTime = System.currentTimeMillis() < triggerDateToday.time
    if (hasRepeatToday && isCurrentBeforeTodayTriggerTime) {
        return 0
    }

    // Today will not, find next day
    val nextRepeatDay = repeat.days.firstOrNull { it.toCalendarInt() > todayWeekToday }
    return if (nextRepeatDay != null) {
        // nextDayOffsetFromToday
        nextRepeatDay.toCalendarInt() - todayWeekToday
    } else {
        // This is last day of repeat, just find the min one.
        // 1 2 3 4 5 6 7
        // 5 -> 1 = 3 = 7 - 5 + 1
        // 3 -> 2 = 6 = 7 - 3 + 2
        val minRepeatDay = repeat.days.minBy { it.toCalendarInt() }.toCalendarInt()
        // nextWeekDayOffset
        7 - todayWeekToday + minRepeatDay
    }
}