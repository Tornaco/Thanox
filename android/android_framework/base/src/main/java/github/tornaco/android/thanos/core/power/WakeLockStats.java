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

package github.tornaco.android.thanos.core.power;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;
import github.tornaco.android.thanos.core.pm.Pkg;

@Keep
public class WakeLockStats implements Parcelable {
    public Pkg pkg;
    public int wakeLockCount;

    public boolean hasBlock;
    public boolean isAllBlock;

    protected WakeLockStats(Parcel in) {
        pkg = in.readParcelable(Pkg.class.getClassLoader());
        wakeLockCount = in.readInt();
        hasBlock = in.readByte() != 0;
        isAllBlock = in.readByte() != 0;
    }

    public WakeLockStats(Pkg pkg, int wakeLockCount, boolean hasBlock, boolean isAllBlock) {
        this.pkg = pkg;
        this.wakeLockCount = wakeLockCount;
        this.hasBlock = hasBlock;
        this.isAllBlock = isAllBlock;
    }

    public static final Creator<WakeLockStats> CREATOR = new Creator<WakeLockStats>() {
        @Override
        public WakeLockStats createFromParcel(Parcel in) {
            return new WakeLockStats(in);
        }

        @Override
        public WakeLockStats[] newArray(int size) {
            return new WakeLockStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pkg, flags);
        dest.writeInt(wakeLockCount);
        dest.writeByte((byte) (hasBlock ? 1 : 0));
        dest.writeByte((byte) (isAllBlock ? 1 : 0));
    }
}
