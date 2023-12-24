/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.main;

import android.app.Activity;
import android.app.ActivityManager;
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
            finishAffinity();
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            tryRemoveAllTasks(am);
        }
    }

    private void tryRemoveAllTasks(ActivityManager am) {
        try {
            for (ActivityManager.AppTask task : am.getAppTasks()) {
                task.finishAndRemoveTask();
            }
        } catch (Throwable ignored) {
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
