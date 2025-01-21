package com.example.weatherapp.presentation.weatherWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.RemoteViews
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.local.CachePreferencesHelper
import com.example.weatherapp.extension.convertFtoCTemp
import com.example.weatherapp.extension.jsonToObjectUsingMoshi
import com.example.weatherapp.extension.selectImageWeather
import com.example.weatherapp.presentation.weatherWidget.broadCast.ActionClickBroadcastReceiver
import com.example.weatherapp.utils.Constant

class WeatherWidgetProvider : AppWidgetProvider() {
    private var remoteViews: RemoteViews? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateUIWidget(appWidgetId, context, appWidgetManager)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }


    private fun updateUIWidget(
        appWidgetId: Int,
        context: Context,
        appWidgetManager: AppWidgetManager,
    ) {
        remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_layout)
        setViewData(remoteViews!!, context)
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun setViewData(views: RemoteViews, context: Context) {
        val cachePreferencesHelper = CachePreferencesHelper(context)
        val tempValue = "${convertFtoCTemp(jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)?.days?.get(0)?.tempmax!!)}°C - " +
                "${convertFtoCTemp(jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)?.days?.get(0)?.tempmin!!)}°C"

        val temp = "${convertFtoCTemp(jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)?.days?.get(0)?.temp!!)}°C"
        views.setTextViewText(R.id.tvTemp, temp)
        views.setTextViewText(R.id.tvDescription,jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)?.days?.get(0)?.description)
        views.setImageViewResource(R.id.ivWeather, selectImageWeather(jsonToObjectUsingMoshi(cachePreferencesHelper.dataWeather)?.days?.get(0)?.icon.toString()))
        views.setOnClickPendingIntent(R.id.llWidget, createPendingIntentToOpenLink(context))
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent != null) {
            val action = intent.action
            if (!TextUtils.isEmpty(action) && action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(
                        ComponentName(context, WeatherWidgetProvider::class.java))
                    for (appWidgetId in appWidgetIds) {
                        updateUIWidget(appWidgetId, context, appWidgetManager)
                    }

            }
        }
    }

    private fun createPendingIntentToOpenLink(
        context: Context,
    ): PendingIntent {
        val launchIntent = Intent(
            context,
            ActionClickBroadcastReceiver::class.java
        )
        launchIntent.putExtra(Constant.EXTRA_PACKAGE_NAME,BuildConfig.APPLICATION_ID)
        val uniqueRequestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueRequestCode,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }
    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {

        updateUIWidget(appWidgetId, context, appWidgetManager)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }
}