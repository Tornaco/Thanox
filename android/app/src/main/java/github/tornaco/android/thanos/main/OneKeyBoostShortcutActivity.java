package github.tornaco.android.thanos.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.util.Objects;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.T;
import github.tornaco.android.thanos.util.ShortcutReceiver;

public class OneKeyBoostShortcutActivity extends Activity {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, OneKeyBoostShortcutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            sendBroadcast(new Intent(T.Actions.ACTION_RUNNING_PROCESS_CLEAR));
        } finally {
            finish();
        }
    }

    public static class ShortcutHelper {

        public static void addShortcut(Context context) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
                Bitmap resource = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_boost_round);
                Intent shortcutInfoIntent = OneKeyBoostShortcutActivity.createIntent(context);
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
                ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "Shortcut-of-thanox-for-one-key-boost")
                        .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                        .setShortLabel(context.getString(R.string.feature_title_one_key_boost))
                        .setIntent(shortcutInfoIntent)
                        .build();
                ShortcutManagerCompat.requestPinShortcut(context, info, ShortcutReceiver.getPinRequestAcceptedIntent(context).getIntentSender());
            }
        }
    }

}
