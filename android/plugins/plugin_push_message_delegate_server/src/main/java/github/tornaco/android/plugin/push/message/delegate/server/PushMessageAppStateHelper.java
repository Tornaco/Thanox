package github.tornaco.android.plugin.push.message.delegate.server;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import github.tornaco.android.thanos.core.push.PushChannel;
import lombok.Synchronized;

/**
 * Created by Tornaco on 2018/3/20 13:31.
 * God bless no bug!
 */

public class PushMessageAppStateHelper {

    public static final long PUSH_INTENT_HANDLE_INTERVAL_MILLS = 30 * 1000;

    // Some apps received Push event, give it a little while to handle it.
    private static final Map<String, GcmEvent> PUSH_EVENT_MAP = new HashMap<>();

    public static boolean isGcmIntent(Intent intent) {
        return PushChannel.GCM.match(intent);
    }

    public static boolean isFcmIntent(Intent intent) {
        return PushChannel.FCM.match(intent);
    }

    public static boolean isMIPushIntent(Intent intent) {
        return PushChannel.MIPUSH.match(intent);
    }

    public static boolean isPushIntent(Intent intent) {
        return isFcmIntent(intent) || isGcmIntent(intent) || isMIPushIntent(intent);
    }

    @Synchronized
    public static boolean isHandlingPushIntent(String who) {
        final GcmEvent event = PUSH_EVENT_MAP.get(who);
        if (event == null) return false;

        long interval = System.currentTimeMillis() - event.getEventTime();
        // Handle invalid.
        if (interval < 0) {
            // This is bad!!!
            event.setEventTime(0);
            return false;
        }
        return interval <= PUSH_INTENT_HANDLE_INTERVAL_MILLS;
    }

    @Synchronized
    public static void onGcmIntentReceived(String who) {
        GcmEvent event = PUSH_EVENT_MAP.get(who);
        if (event == null) event = new GcmEvent();

        // Update event.
        event.setEventTime(System.currentTimeMillis());
        event.setEventPackage(who);
        PUSH_EVENT_MAP.put(who, event);
    }
}
