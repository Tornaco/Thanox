package now.fortuitous.thanos.main

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import github.tornaco.android.thanos.R
import now.fortuitous.thanos.process.v2.ProcessManageActivityV2Delegate
import now.fortuitous.thanos.sf.SFActivity
import tornaco.apps.thanox.ThanosShizukuMainActivity

class ShortcutInit(val context: Context) {
    companion object {
        const val ID_THANOX_PM = "thanox_pm"
        const val ID_THANOX_SF = "thanox_sf"
        const val ID_THANOS_PM = "thanos_pm"
        const val ID_THANOS_SF = "thanos_sf"
    }

    private fun installForThanox() {
        if (!hasDynamicShortcut(ID_THANOX_SF)) {
            addDynamicShortcut(
                ID_THANOX_SF,
                SFActivity::class.java,
                R.drawable.shortcut_smart_freeze,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze)
            )
        }
        if (!hasDynamicShortcut(ID_THANOX_PM)) {
            addDynamicShortcut(
                ID_THANOX_PM,
                ProcessManageActivityV2Delegate::class.java,
                R.drawable.ic_shortcut_process_manage,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_process_manage)
            )
        }
    }

    private fun installForThanos() {
        if (!hasDynamicShortcut(ID_THANOS_SF)) {
            addDynamicShortcut(
                ID_THANOS_SF,
                ThanosShizukuMainActivity::class.java,
                R.drawable.shortcut_smart_freeze,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze),
                "SF"
            )
        }

        if (!hasDynamicShortcut(ID_THANOS_PM)) {
            addDynamicShortcut(
                ID_THANOS_PM,
                ThanosShizukuMainActivity::class.java,
                R.drawable.ic_shortcut_process_manage,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_process_manage),
                "PM"
            )
        }
    }

    fun initOnBootThanos() {
        installForThanos()
        removeDynamicShortcuts(listOf(ID_THANOX_PM, ID_THANOX_SF))
    }

    fun initOnBootThanox() {
        installForThanox()
        removeDynamicShortcuts(listOf(ID_THANOS_PM, ID_THANOS_SF))
    }

    fun addDynamicShortcut(
        id: String,
        clazz: Class<*>,
        iconRes: Int,
        title: String,
        data: String? = null
    ): Boolean {
        val shortcutInfoIntent = Intent(context, clazz).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data?.let {
                setData(it.toUri())
            }
        }
        shortcutInfoIntent.action = Intent.ACTION_VIEW

        val icon = IconCompat.createWithResource(
            context,
            iconRes
        )

        val shortcutInfo = ShortcutInfoCompat.Builder(context, id)
            .setShortLabel(title)
            .setLongLabel(title)
            .setIcon(icon)
            .setIntent(shortcutInfoIntent)
            .build()

        return ShortcutManagerCompat.pushDynamicShortcut(context, shortcutInfo)
    }

    fun removeDynamicShortcuts(id: List<String>) {
        ShortcutManagerCompat.removeDynamicShortcuts(context, id)
    }

    fun hasDynamicShortcut(id: String): Boolean {
        return ShortcutManagerCompat.getDynamicShortcuts(context).any {
            it.id == id
        }
    }
}