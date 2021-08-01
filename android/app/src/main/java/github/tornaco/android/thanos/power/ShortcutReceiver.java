package github.tornaco.android.thanos.power;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import io.reactivex.Completable;
import rx2.android.schedulers.AndroidSchedulers;

public class ShortcutReceiver extends BroadcastReceiver {

    private static final String ACTION_PIN_REQUEST_ACCEPTED =
            "ShortcutReceiver.ACTION_PIN_REQUEST_ACCEPTED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {

        } else if (ACTION_PIN_REQUEST_ACCEPTED.equals(intent.getAction())) {
            Completable.fromRunnable(() ->
                    Toast.makeText(context, "\uD83D\uDC4D", Toast.LENGTH_LONG).show())
                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    public static PendingIntent getPinRequestAcceptedIntent(Context context) {
        final Intent intent = new Intent(ACTION_PIN_REQUEST_ACCEPTED);
        intent.setComponent(new ComponentName(context, ShortcutReceiver.class));

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
