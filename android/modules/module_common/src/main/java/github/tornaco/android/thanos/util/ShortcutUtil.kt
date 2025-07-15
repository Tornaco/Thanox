package github.tornaco.android.thanos.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import github.tornaco.android.thanos.core.pm.AppInfo
import github.tornaco.android.thanos.module.common.R
import java.util.Objects

object ShortcutUtil {
    fun addShortcut(context: Context, appInfo: AppInfo, intent: () -> Intent) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            // Post load with glide.
            GlideApp.with(context)
                .asBitmap()
                .load(appInfo)
                .error(R.mipmap.ic_fallback_app_icon)
                .fallback(R.mipmap.ic_fallback_app_icon)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val shortcutInfoIntent: Intent = intent()
                        shortcutInfoIntent.setAction(Intent.ACTION_VIEW)
                        val info = ShortcutInfoCompat.Builder(
                            context,
                            "Shortcut-of-thanox-for-" + appInfo.pkgName
                        )
                            .setIcon(IconCompat.createWithBitmap(Objects.requireNonNull(resource)))
                            .setShortLabel(appInfo.appLabel)
                            .setIntent(shortcutInfoIntent)
                            .build()
                        ShortcutManagerCompat.requestPinShortcut(
                            context,
                            info,
                            ShortcutReceiver.getPinRequestAcceptedIntent(context).intentSender
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Noop.
                    }
                })
        }
    }
}