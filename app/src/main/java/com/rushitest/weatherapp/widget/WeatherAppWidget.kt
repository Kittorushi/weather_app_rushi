package com.rushitest.weatherapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews
import com.rushitest.weatherapp.R
import com.rushitest.weatherapp.features.weather_info_show.view.MainActivity


/**
 * Implementation of App Widget functionality.
 */
class WeatherAppWidget : AppWidgetProvider() {


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }


        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        val views: RemoteViews = RemoteViews(
            context.packageName,
            R.layout.weather_app_widget
        ).apply {
            setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)
        }

        }

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
 }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {


    // access sharedPreferences to get temp
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WeatherTemp", Context.MODE_PRIVATE)
    // Construct the RemoteViews object
    val widgetText = sharedPreferences.getString("temp", "")

    val views = RemoteViews(context.packageName, R.layout.weather_app_widget)
    views.setTextViewText(R.id.appwidget_text, "$widgetText °C")
    appWidgetManager.updateAppWidget(appWidgetId, views)



}