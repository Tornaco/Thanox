package github.tornaco.android.nitro.framework.host.manager.data.converter;

import android.content.pm.ApplicationInfo;

import androidx.room.TypeConverter;

import github.tornaco.android.nitro.framework.host.manager.data.ParcelUtils;

public class ApplicationInfoConverter {

    @TypeConverter
    public static ApplicationInfo activityInfoFromBytes(byte[] data) {
        return ParcelUtils.unmarshall(data, ApplicationInfo.CREATOR);
    }

    @TypeConverter
    public static byte[] activityInfoToBytes(ApplicationInfo applicationInfo) {
        return ParcelUtils.marshall(applicationInfo);
    }
}
