package com.example.mystoryapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.example.mystoryapp.R
import com.example.mystoryapp.view.detailstory.DetailStoryActivity

/**
 * Implementation of App Widget functionality.
 */
class ListStoryWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            val storyId = intent.getStringExtra(EXTRA_ITEM)
            val intentDetailStoryActivity = Intent(context, DetailStoryActivity::class.java)
            intentDetailStoryActivity.putExtra(DetailStoryActivity.EXTRA_ID, storyId)

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intentDetailStoryActivity,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else 0
            )
            pendingIntent.send()
        }
    }

    companion object {
        private const val CLICK_ACTION = "com.example.mystoryapp.CLICK_ACTION"
        const val EXTRA_ITEM = "com.example.mystoryapp.EXTRA_ITEM"

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()


            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.list_story_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val clickIntent = Intent(context, ListStoryWidget::class.java)
            clickIntent.action = CLICK_ACTION
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val clickPendingIntent = PendingIntent.getBroadcast(
                context, 0, clickIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else 0
            )
            views.setPendingIntentTemplate(R.id.stack_view, clickPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}