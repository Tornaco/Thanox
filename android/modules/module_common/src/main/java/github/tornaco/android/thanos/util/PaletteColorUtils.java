package github.tornaco.android.thanos.util;

import android.content.Context;

/**
 * Created by guohao4 on 2017/10/28.
 * Email: Tornaco@163.com
 */

public abstract class PaletteColorUtils {

    public interface PickReceiver {
        void onColorReady(int color);
    }

    // TODO
    public static void pickPrimaryColor(Context context, final PickReceiver receiver,
                                        String pkg, final int defColor) {
        receiver.onColorReady(defColor);
    }
}
