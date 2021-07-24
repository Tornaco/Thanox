package github.tornaco.android.thanox.magisk.bridge.proxy;

import java.util.HashMap;
import java.util.Map;

public class Args {
    private static final Map<String, Integer> KEY_TO_INDEX = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getFirstTypeOfArgOrNull(Class<? extends T> clazzToLookup, Object[] args, String cacheKey) {
        if (args == null || args.length == 0) {
            return null;
        }

        Integer cachedIndex = KEY_TO_INDEX.get(cacheKey);
        if (cachedIndex != null && cachedIndex > 0 && cachedIndex < args.length) {
            return (T) args[cachedIndex];
        }

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg.getClass() == clazzToLookup) {
                // Cache.
                KEY_TO_INDEX.put(cacheKey, i);
                return (T) arg;
            }
        }
        return null;
    }
}
