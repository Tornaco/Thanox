package now.fortuitous.thanos.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import github.tornaco.android.thanos.R
import now.fortuitous.thanos.sf.SFActivity

class SmartFreezeAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                /* context = */ context,
                /* requestCode = */  0,
                /* intent = */ Intent(context, SFActivity::class.java).apply {
                    putExtra(SFActivity.EXTRA_EXPAND_SEARCH, true)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                },
                /* flags = */ PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.smart_freeze_appwidget
            ).apply {
                setOnClickPendingIntent(R.id.search_layout, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}