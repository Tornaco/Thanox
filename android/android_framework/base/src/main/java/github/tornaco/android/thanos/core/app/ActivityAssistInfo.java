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

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.ComponentNameBrief;

@Keep
public class ActivityAssistInfo implements Parcelable {
    public ComponentNameBrief name;
    public int uid;

    public ActivityAssistInfo(ComponentNameBrief name, int uid) {
        this.name = name;
        this.uid = uid;
    }

    protected ActivityAssistInfo(Parcel in) {
        name = in.readParcelable(ComponentNameBrief.class.getClassLoader());
        uid = in.readInt();
    }

    public static final Creator<ActivityAssistInfo> CREATOR = new Creator<ActivityAssistInfo>() {
        @Override
        public ActivityAssistInfo createFromParcel(Parcel in) {
            return new ActivityAssistInfo(in);
        }

        @Override
        public ActivityAssistInfo[] newArray(int size) {
            return new ActivityAssistInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(name, i);
        parcel.writeInt(uid);
    }

    @Override
    public String toString() {
        return "ActivityAssistInfo{" +
                "name=" + name +
                ", uid=" + uid +
                '}';
    }
}
