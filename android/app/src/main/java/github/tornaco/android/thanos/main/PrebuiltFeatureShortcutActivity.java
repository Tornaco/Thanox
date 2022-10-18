package github.tornaco.android.thanos.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.elvishew.xlog.XLog;

import java.util.Objects;

import github.tornaco.android.thanos.dashboard.Tile;
import github.tornaco.android.thanos.util.BitmapUtil;
import github.tornaco.android.thanos.util.ShortcutReceiver;

public class PrebuiltFeatureShortcutActivity extends Activity {
    private static final String KEY_FEATURE_ID = "key_feature_id";

    public static Intent createIntent(Context context, int featureId) {
        XLog.d("PrebuiltFeatureShortcutActivity, createIntent: " + featureId);
        Intent intent = new Intent(context, PrebuiltFeatureShortcutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_FEATURE_ID, featureId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int featId = getIntent().getIntExtra(KEY_FEATURE_ID, Integer.MIN_VALUE);
        XLog.d("PrebuiltFeatureShortcutActivity, featId: " + featId);
        if (PrebuiltFeatureIds.INSTANCE.isValidId(featId)) {
            new PrebuiltFeatureLauncher(this, () -> null).launch(featId);
        }
        finish();
    }

    public static class ShortcutHelper {

        public static void addShortcut(Context context, Tile tile) {
            if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
                Bitmap resource = BitmapUtil.getBitmap(context, ResourcesCompat.getDrawable(context.getResources(), tile.getIconRes(), null));
                Intent shortcutInfoIntent = PrebuiltFeatureShortcutActivity.createIntent(context, tile.getId());
                shortcutInfoIntent.setAction(Intent.ACTION_VIEW);
                ShortcutInfoCompat info = new ShortcutInfoCompat.Builder(context, "Shortcut-of-thanox-for-feature-" + tile.getId())
                        .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                        .setShortLabel(tile.getTitle())
                        .setIntent(shortcutInfoIntent)
                        .build();
                ShortcutManagerCompat.requestPinShortcut(context, info, ShortcutReceiver.getPinRequestAcceptedIntent(context).getIntentSender());
            }
        }
    }

}
