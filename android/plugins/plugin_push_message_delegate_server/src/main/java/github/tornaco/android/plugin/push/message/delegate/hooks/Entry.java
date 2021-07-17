package github.tornaco.android.plugin.push.message.delegate.hooks;

import android.annotation.SuppressLint;
import android.content.Context;

import github.tornaco.android.plugin.push.message.delegate.Logging;
import github.tornaco.android.plugin.push.message.delegate.server.PushDelegateServer;

@SuppressWarnings("UnstableApiUsage")
public class Entry {

    @SuppressLint("StaticFieldLeak")
    private static final PushDelegateServer SERVER = new PushDelegateServer();

    private static void initLogging() {
        Logging.main("PushDelegate");
    }

    public static void boot(Context context) {
        initLogging();
        SERVER.start(context);
        SERVER.systemReady();
    }
}
