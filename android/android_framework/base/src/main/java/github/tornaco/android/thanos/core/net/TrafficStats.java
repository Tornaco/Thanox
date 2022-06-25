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

package github.tornaco.android.thanos.core.net;

import android.os.Parcel;
import android.os.Parcelable;

public class TrafficStats implements Parcelable {
    public long rxBytes;
    public long txBytes;

    protected TrafficStats(Parcel in) {
        rxBytes = in.readLong();
        txBytes = in.readLong();
    }

    public TrafficStats(long rxBytes, long txBytes) {
        this.rxBytes = rxBytes;
        this.txBytes = txBytes;
    }

    public static final Creator<TrafficStats> CREATOR = new Creator<TrafficStats>() {
        @Override
        public TrafficStats createFromParcel(Parcel in) {
            return new TrafficStats(in);
        }

        @Override
        public TrafficStats[] newArray(int size) {
            return new TrafficStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(rxBytes);
        parcel.writeLong(txBytes);
    }
}
