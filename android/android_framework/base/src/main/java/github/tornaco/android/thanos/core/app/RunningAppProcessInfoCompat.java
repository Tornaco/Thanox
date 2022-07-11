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

package github.tornaco.android.thanos.core.app;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class RunningAppProcessInfoCompat implements Parcelable {
    public String processName;
    public int pid;
    public int uid;
    public String[] pkgList;
    public int flags;
    public int importance;
    public int lru;
    public int processState;
    public int importanceReasonPid;
    public int importanceReasonCode;
    public ComponentName importanceReasonComponent;

    public RunningAppProcessInfoCompat() {
    }

    protected RunningAppProcessInfoCompat(Parcel in) {
        processName = in.readString();
        pid = in.readInt();
        uid = in.readInt();
        pkgList = in.readStringArray();
        flags = in.readInt();
        importance = in.readInt();
        lru = in.readInt();
        processState = in.readInt();
        importanceReasonPid = in.readInt();
        importanceReasonCode = in.readInt();
        importanceReasonComponent = ComponentName.readFromParcel(in);
    }

    public static final Creator<RunningAppProcessInfoCompat> CREATOR = new Creator<RunningAppProcessInfoCompat>() {
        @Override
        public RunningAppProcessInfoCompat createFromParcel(Parcel in) {
            return new RunningAppProcessInfoCompat(in);
        }

        @Override
        public RunningAppProcessInfoCompat[] newArray(int size) {
            return new RunningAppProcessInfoCompat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(processName);
        parcel.writeInt(pid);
        parcel.writeInt(uid);
        parcel.writeStringArray(pkgList);
        parcel.writeInt(flags);
        parcel.writeInt(importance);
        parcel.writeInt(lru);
        parcel.writeInt(processState);
        parcel.writeInt(importanceReasonPid);
        parcel.writeInt(importanceReasonCode);
        ComponentName.writeToParcel(this.importanceReasonComponent, parcel);
    }

    @Override
    public String toString() {
        return "RunningAppProcessInfoCompat{" +
                "processName='" + processName + '\'' +
                ", pid=" + pid +
                ", uid=" + uid +
                ", pkgList=" + Arrays.toString(pkgList) +
                '}';
    }

    public static RunningAppProcessInfoCompat from(ActivityManager.RunningAppProcessInfo legacy) {
        RunningAppProcessInfoCompat compat = new RunningAppProcessInfoCompat();
        compat.processName = legacy.processName;
        compat.pid = legacy.pid;
        compat.uid = legacy.uid;
        compat.pkgList = legacy.pkgList;
        compat.flags = legacy.flags;
        compat.importance = legacy.importance;
        compat.lru = legacy.lru;
        compat.processState = legacy.processState;
        compat.importanceReasonPid = legacy.importanceReasonPid;
        compat.importanceReasonCode = legacy.importanceReasonCode;
        compat.importanceReasonComponent = legacy.importanceReasonComponent;
        return compat;
    }
}
