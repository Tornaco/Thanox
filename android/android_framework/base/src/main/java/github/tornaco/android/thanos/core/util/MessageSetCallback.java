package github.tornaco.android.thanos.core.util;

import android.os.Message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ReflectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSetCallback {
    // Runnable callback;

    public static Message setCallback(Message message, Runnable r) {
        ReflectionUtils.setObjectField(message, "callback", r);
        return message;
    }
}
