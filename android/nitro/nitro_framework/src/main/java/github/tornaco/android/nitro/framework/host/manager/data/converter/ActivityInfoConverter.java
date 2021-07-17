package github.tornaco.android.nitro.framework.host.manager.data.converter;

import android.content.pm.ActivityInfo;

import androidx.room.TypeConverter;

import github.tornaco.android.nitro.framework.host.manager.data.ParcelUtils;

public class ActivityInfoConverter {

    @TypeConverter
    public static ActivityInfo activityInfoFromBytes(byte[] data) {
        return ParcelUtils.unmarshall(data, ActivityInfo.CREATOR);
    }

    @TypeConverter
    public static byte[] activityInfoToBytes(ActivityInfo activityInfo) {
        return ParcelUtils.marshall(activityInfo);
    }
}
