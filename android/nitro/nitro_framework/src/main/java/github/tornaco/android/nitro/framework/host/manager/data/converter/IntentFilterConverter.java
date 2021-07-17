package github.tornaco.android.nitro.framework.host.manager.data.converter;

import android.content.IntentFilter;

import androidx.room.TypeConverter;

import github.tornaco.android.nitro.framework.host.manager.data.ParcelUtils;

public class IntentFilterConverter {

    @TypeConverter
    public static IntentFilter activityInfoFromBytes(byte[] data) {
        return ParcelUtils.unmarshall(data, IntentFilter.CREATOR);
    }

    @TypeConverter
    public static byte[] activityInfoToBytes(IntentFilter filter) {
        return ParcelUtils.marshall(filter);
    }
}
