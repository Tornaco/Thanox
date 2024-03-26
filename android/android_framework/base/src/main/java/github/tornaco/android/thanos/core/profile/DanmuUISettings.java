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

package github.tornaco.android.thanos.core.profile;

import android.os.Parcel;
import android.os.Parcelable;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class DanmuUISettings implements Parcelable {
    public static final int COLOR_AUTO = -1;

    private final float alpha;
    private final int backgroundColor;
    private final int textColor;
    private final int textSizeSp;
    private final long duration;

    public DanmuUISettings(float alpha, int backgroundColor, int textColor, int textSizeSp, long duration) {
        this.alpha = alpha;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textSizeSp = textSizeSp;
        this.duration = duration;
    }

    protected DanmuUISettings(Parcel in) {
        alpha = in.readFloat();
        backgroundColor = in.readInt();
        textColor = in.readInt();
        textSizeSp = in.readInt();
        duration = in.readLong();
    }

    public float getAlpha() {
        return alpha;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSizeSp() {
        return textSizeSp;
    }

    public long getDuration() {
        return duration;
    }

    public static final Creator<DanmuUISettings> CREATOR = new Creator<DanmuUISettings>() {
        @Override
        public DanmuUISettings createFromParcel(Parcel in) {
            return new DanmuUISettings(in);
        }

        @Override
        public DanmuUISettings[] newArray(int size) {
            return new DanmuUISettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(alpha);
        parcel.writeInt(backgroundColor);
        parcel.writeInt(textColor);
        parcel.writeInt(textSizeSp);
        parcel.writeLong(duration);
    }
}
