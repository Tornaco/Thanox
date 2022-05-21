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

package github.tornaco.android.thanos.core.os;

import android.os.Parcel;
import android.os.Parcelable;

public class SwapInfo implements Parcelable {
    public long totalSwap;
    public long freeSwap;

    public SwapInfo(long totalSwap, long freeSwap) {
        this.totalSwap = totalSwap;
        this.freeSwap = freeSwap;
    }

    protected SwapInfo(Parcel in) {
        totalSwap = in.readLong();
        freeSwap = in.readLong();
    }

    public static final Creator<SwapInfo> CREATOR = new Creator<SwapInfo>() {
        @Override
        public SwapInfo createFromParcel(Parcel in) {
            return new SwapInfo(in);
        }

        @Override
        public SwapInfo[] newArray(int size) {
            return new SwapInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(totalSwap);
        parcel.writeLong(freeSwap);
    }
}
