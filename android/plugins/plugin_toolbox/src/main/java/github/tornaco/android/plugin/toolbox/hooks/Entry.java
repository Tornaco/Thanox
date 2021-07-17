package github.tornaco.android.plugin.toolbox.hooks;

import android.content.Context;

import github.tornaco.android.plugin.toolbox.Logging;


public class Entry {


    private static void initLogging() {
        Logging.main("Toolbox");
    }

    public static void boot(Context context) {
        initLogging();
    }
}
