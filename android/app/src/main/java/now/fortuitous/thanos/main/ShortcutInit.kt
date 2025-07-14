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
        const val ID_XPOSED_PM = "thanox_pm"
        const val ID_XPOSED_SF = "thanox_sf"

        // Refactor-version
        const val ID_XPOSED_SF_2 = "thanox_sf_2"

        const val ID_SHIZUKU_PM = "thanos_pm"
        const val ID_SHIZUKU_SF = "thanos_sf"
    }

    private fun installForXposed() {
        if (!hasDynamicShortcut(ID_XPOSED_SF_2)) {
            removeDynamicShortcuts(listOf(ID_XPOSED_SF))
            addDynamicShortcut(
                ID_XPOSED_SF,
                SFActivity::class.java,
                R.drawable.shortcut_smart_freeze,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze)
            )
        }
        if (!hasDynamicShortcut(ID_XPOSED_PM)) {
            addDynamicShortcut(
                ID_XPOSED_PM,
                ProcessManageActivityV2Delegate::class.java,
                R.drawable.ic_shortcut_process_manage,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_process_manage)
            )
        }
    }

    private fun installForShizuku() {
        if (!hasDynamicShortcut(ID_SHIZUKU_SF)) {
            addDynamicShortcut(
                ID_SHIZUKU_SF,
                ThanosShizukuMainActivity::class.java,
                R.drawable.shortcut_smart_freeze,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_smart_app_freeze),
                "SF"
            )
        }

        if (!hasDynamicShortcut(ID_SHIZUKU_PM)) {
            addDynamicShortcut(
                ID_SHIZUKU_PM,
                ThanosShizukuMainActivity::class.java,
                R.drawable.ic_shortcut_process_manage,
                context.getString(github.tornaco.android.thanos.res.R.string.feature_title_process_manage),
                "PM"
            )
        }
    }

    fun initOnBootShizuku() {
        installForShizuku()
        removeDynamicShortcuts(listOf(ID_XPOSED_PM, ID_XPOSED_SF))
    }

    fun initOnBootXposed() {
        installForXposed()
        removeDynamicShortcuts(listOf(ID_SHIZUKU_PM, ID_SHIZUKU_SF))
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