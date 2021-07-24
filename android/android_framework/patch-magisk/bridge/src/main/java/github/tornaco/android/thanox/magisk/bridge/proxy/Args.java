package github.tornaco.android.thanox.magisk.bridge.proxy;

import java.util.HashMap;
import java.util.Map;

public class Args {
    private static final Map<String, Integer> KEY_TO_INDEX = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getFirstTypeOfArgOrNull(Class<? extends T> clazzToLookup, Object[] args, String cacheKey) {
        int index = getFirstTypeOfArgIndexOr(clazzToLookup, args, cacheKey, -1);
        if (index != -1) {
            return (T) args[index];
        }
        return null;
    }

    public static int getFirstTypeOfArgIndexOr(Class<?> clazzToLookup, Object[] args, String cacheKey, int orElse) {
        if (args == null || args.length == 0) {
            return orElse;
        }

        Integer cachedIndex = KEY_TO_INDEX.get(cacheKey);
        if (cachedIndex != null && cachedIndex > 0 && cachedIndex < args.length) {
            return cachedIndex;
        }

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg != null && arg.getClass() == clazzToLookup) {
                // Cache.
                KEY_TO_INDEX.put(cacheKey, i);
                return i;
            }
        }
        return orElse;
    }
}
