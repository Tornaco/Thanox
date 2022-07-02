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

package github.tornaco.android.thanos.core.secure.ops;

import android.os.Parcel;
import android.os.Parcelable;

public class SettingsAccessRecord implements Parcelable {
    public String name;
    public String value;
    public String callerPackageName;
    public long timeMillis;

    protected SettingsAccessRecord(Parcel in) {
        name = in.readString();
        value = in.readString();
        callerPackageName = in.readString();
        timeMillis = in.readLong();
    }

    public SettingsAccessRecord(String name, String value, String callerPackageName, long timeMillis) {
        this.name = name;
        this.value = value;
        this.callerPackageName = callerPackageName;
        this.timeMillis = timeMillis;
    }

    public static final Creator<SettingsAccessRecord> CREATOR = new Creator<SettingsAccessRecord>() {
        @Override
        public SettingsAccessRecord createFromParcel(Parcel in) {
            return new SettingsAccessRecord(in);
        }

        @Override
        public SettingsAccessRecord[] newArray(int size) {
            return new SettingsAccessRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
        parcel.writeString(callerPackageName);
        parcel.writeLong(timeMillis);
    }
}
