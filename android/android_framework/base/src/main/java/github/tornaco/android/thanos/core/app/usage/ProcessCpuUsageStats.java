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

package github.tornaco.android.thanos.core.app.usage;

import android.os.Parcel;
import android.os.Parcelable;

public class ProcessCpuUsageStats implements Parcelable {
    // Device total.
    public long totalTime;
    public long processTime;
    public String cpuRatioString;

    public long pid;
    public String processName;

    protected ProcessCpuUsageStats(Parcel in) {
        totalTime = in.readLong();
        processTime = in.readLong();
        cpuRatioString = in.readString();
        pid = in.readLong();
        processName = in.readString();
    }

    public ProcessCpuUsageStats() {
    }

    public static final Creator<ProcessCpuUsageStats> CREATOR = new Creator<ProcessCpuUsageStats>() {
        @Override
        public ProcessCpuUsageStats createFromParcel(Parcel in) {
            return new ProcessCpuUsageStats(in);
        }

        @Override
        public ProcessCpuUsageStats[] newArray(int size) {
            return new ProcessCpuUsageStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(totalTime);
        parcel.writeLong(processTime);
        parcel.writeString(cpuRatioString);
        parcel.writeLong(pid);
        parcel.writeString(processName);
    }
}
