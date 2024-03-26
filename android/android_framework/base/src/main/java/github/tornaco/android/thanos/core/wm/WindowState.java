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

package github.tornaco.android.thanos.core.wm;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class WindowState implements Parcelable {
    public String packageName;
    public int uid;
    public boolean visible;
    public int type;

    public WindowState(String packageName, int uid, boolean visible, int type) {
        this.packageName = packageName;
        this.uid = uid;
        this.visible = visible;
        this.type = type;
    }

    protected WindowState(Parcel in) {
        packageName = in.readString();
        uid = in.readInt();
        visible = in.readByte() != 0;
        type = in.readInt();
    }

    public static final Creator<WindowState> CREATOR = new Creator<WindowState>() {
        @Override
        public WindowState createFromParcel(Parcel in) {
            return new WindowState(in);
        }

        @Override
        public WindowState[] newArray(int size) {
            return new WindowState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(packageName);
        parcel.writeInt(uid);
        parcel.writeByte((byte) (visible ? 1 : 0));
        parcel.writeInt(type);
    }

    @Override
    public String toString() {
        return "WindowState{" +
                "packageName='" + packageName + '\'' +
                ", uid=" + uid +
                ", visible=" + visible +
                ", type=" + type + " " + WindowTypeMapping.INSTANCE.getMap().get(String.valueOf(type)) +
                '}';
    }
}
