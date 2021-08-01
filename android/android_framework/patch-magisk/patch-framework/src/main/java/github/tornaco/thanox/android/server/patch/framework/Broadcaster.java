package github.tornaco.thanox.android.server.patch.framework;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.elvishew.xlog.XLog;

import github.tornaco.android.thanos.services.BootStrap;

public class Broadcaster implements Runnable {
    private final Context context;

    public static void install(Context context) {
        new Broadcaster(context).run();
    }

    public Broadcaster(Context context) {
        this.context = context;
    }

    @SuppressLint("InlinedApi")
    private void registerReceivers() {
        XLog.w("Broadcaster registerReceivers with context: %s", context);
        if (context == null) {
            return;
        }

        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        packageFilter.addAction(Intent.ACTION_QUERY_PACKAGE_RESTART);
        packageFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        packageFilter.addDataScheme("package");
        context.registerReceiver(new DelegateReceiver(), packageFilter);

        IntentFilter packageNoDataFilter = new IntentFilter();
        packageNoDataFilter.addAction(Intent.ACTION_UID_REMOVED);
        packageNoDataFilter.addAction(Intent.ACTION_USER_STOPPED);
        packageNoDataFilter.addAction(Intent.ACTION_PACKAGES_SUSPENDED);
        packageNoDataFilter.addAction(Intent.ACTION_PACKAGES_UNSUSPENDED);
        context.registerReceiver(new DelegateReceiver(), packageNoDataFilter);

        IntentFilter packageExtFilter = new IntentFilter();
        packageExtFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        packageExtFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        context.registerReceiver(new DelegateReceiver(), packageExtFilter);

        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(new DelegateReceiver(), screenFilter);

        IntentFilter userFilter = new IntentFilter();
        userFilter.addAction(Intent.ACTION_USER_PRESENT);
        userFilter.addAction(Intent.ACTION_USER_INITIALIZE);
        userFilter.addAction(Intent.ACTION_USER_FOREGROUND);
        userFilter.addAction(Intent.ACTION_USER_BACKGROUND);
        userFilter.addAction(Intent.ACTION_USER_ADDED);
        userFilter.addAction(Intent.ACTION_USER_STARTED);
        userFilter.addAction(Intent.ACTION_USER_STARTING);
        userFilter.addAction(Intent.ACTION_USER_STOPPING);
        userFilter.addAction(Intent.ACTION_USER_STOPPED);
        userFilter.addAction(Intent.ACTION_USER_REMOVED);
        userFilter.addAction(Intent.ACTION_USER_SWITCHED);
        userFilter.addAction(Intent.ACTION_USER_UNLOCKED);
        userFilter.addAction(Intent.ACTION_USER_INFO_CHANGED);
        context.registerReceiver(new DelegateReceiver(), userFilter);

        IntentFilter otherFilter = new IntentFilter();
        otherFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        otherFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(new DelegateReceiver(), otherFilter);

        IntentFilter btFilter = new IntentFilter();
        btFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        btFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        context.registerReceiver(new DelegateReceiver(), btFilter);

        XLog.i("Broadcaster registerReceiver complete.");
    }

    @Override
    public final void run() {
        registerReceivers();
    }

    static class DelegateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            XLog.v("Broadcaster onReceive: %s", intent);
            // Redirect.
            BootStrap.THANOS_X.getActivityManagerService().checkBroadcastingIntent(intent);
        }
    }
}