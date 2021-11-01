package github.tornaco.thanos.android.module.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.elvishew.xlog.XLog;

import java.util.Objects;
import java.util.UUID;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.util.ShortcutReceiver;

public class ProfileShortcutEngineActivity extends Activity {
    private static final String EXTRA_PROFILE_FACT_VALUE = "profile.extra.fact.value";

    public static Intent createIntent(Context context, String factValue) {
        Intent intent = new Intent(context, ProfileShortcutEngineActivity.class);
        intent.putExtra(EXTRA_PROFILE_FACT_VALUE, factValue);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            String factValue = getIntent().getStringExtra(EXTRA_PROFILE_FACT_VALUE);
            XLog.i("factValue= %s", factValue);
            if (!TextUtils.isEmpty(factValue)) {
                ThanosManager.from(this).ifServiceInstalled(thanosManager -> thanosManager.getProfileManager().publishStringFact(factValue, 0));
            }
        }
        finish();
    }

    public static class ShortcutHelper {

        public static void addShortcut(Context context, String label, String factValue) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
                Bitmap resource = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_fallback_app_icon);
                Intent shortcutInfoIntent = ProfileShortcutEngineActivity.createIntent(context, factValue);
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
                ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "Shortcut-of-thanox-for-profile-engine" + UUID.randomUUID().toString())
                        .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                        .setShortLabel(label)
                        .setIntent(shortcutInfoIntent)
                        .build();
                ShortcutManagerCompat.requestPinShortcut(context, info, ShortcutReceiver.getPinRequestAcceptedIntent(context).getIntentSender());
            }
        }
    }

}
