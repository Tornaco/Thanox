package github.tornaco.android.thanos.core.util;

import android.os.Message;
import android.util.Log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ReflectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessagePoolSync {
    // private static final Object sPoolSync = new Object();
    public static final Object sPoolSync;

    static {
        sPoolSync = ReflectionUtils.getStaticObjectField(Message.class, "sPoolSync");
        Log.d("MessagePoolSync", "sPoolSync=" + sPoolSync);
    }
}
