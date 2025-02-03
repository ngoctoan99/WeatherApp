package com.example.weatherapp.extension


import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.WeatherModel
import com.example.weatherapp.presentation.weatherWidget.WeatherWidgetProvider
import com.example.weatherapp.utils.ToastTypes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}

fun convertFtoCTemp(valueTemp : Double) : Int{
    val valueTempConvert = (valueTemp - 32) / 1.8
    return valueTempConvert.roundToInt()
}

fun convertTime(time:String):String{
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh a", Locale.getDefault())
    val date = inputFormat.parse(time)
    return outputFormat.format(date!!)
}


fun selectImageWeather(icon:String):Int{
    return if(icon == "cloudy"){
        R.drawable.ic_weather_cloudy
    }else if(icon.contains("partly-cloudy",ignoreCase = true)){
        R.drawable.ic_weather_cloud
    }
    else if(icon.contains("clear",ignoreCase = true)){
        R.drawable.ic_weather_sun
    }
    else if(icon.contains("rain",ignoreCase = true)){
        R.drawable.ic_weather_cloud_rain
    }
    else {
        R.drawable.ic_weather_sun
    }
}

fun convertDateToDayOfWeek(dateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val date = LocalDate.parse(dateString, formatter)
    val dayOfWeek = date.dayOfWeek
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
    }
}

fun jsonToStringUsingMoshi(weatherModel: WeatherModel): String {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()) .build()
    val adapter = moshi.adapter(WeatherModel::class.java)
    return adapter.toJson(weatherModel)
}

fun jsonToObjectUsingMoshi(jsonStr: String): WeatherModel? {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()) .build()
    val adapter = moshi.adapter(WeatherModel::class.java)
    return adapter.fromJson(jsonStr)
}

fun updateDataWidget(context: Context){
    val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
    intent.setComponent(
        ComponentName(
            context,
            WeatherWidgetProvider::class.java
        )
    )
    context.sendBroadcast(intent)
}
private var toast: Toast? = null
private var isCanCloseToast: Boolean = false
internal fun Context.showToast(message: String, messageDetail:String="", toastTypes: ToastTypes, isLongTime:Boolean=false, canClose: Boolean = true) {
    isCanCloseToast = canClose
    toast?.cancel()
    val inflater = LayoutInflater.from(this)
    val layout: View = inflater.inflate(R.layout.layout_custom_toast, null)
    val tvTitleToast: TextView = layout.findViewById(R.id.tvTitleToast)
    val tvDesToast: TextView = layout.findViewById(R.id.tvDesToast)
    val ivToast: ImageView = layout.findViewById(R.id.ivToast)
    //  val ivCloseToast: ImageView = layout.findViewById(R.id.ivCloseToast)
    val cardRootToast: CardView = layout.findViewById(R.id.cardRootToast)
    val rltContent: RelativeLayout = layout.findViewById(R.id.rltContent)

    tvTitleToast.text = message
    tvDesToast.text=messageDetail
    when(toastTypes){
        ToastTypes.SUCCESS-> {
            ivToast.setImageResource(R.drawable.ic_toast_success)
            cardRootToast.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_success))
            rltContent.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_success_light))
            //      ivCloseToast.setColorFilter(ContextCompat.getColor(this,R.color.color_toast_success))
        }
        ToastTypes.INFO->{
            ivToast.setImageResource(R.drawable.ic_toast_info)
            cardRootToast.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_info))
            rltContent.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_info_light))
            //       ivCloseToast.setColorFilter(ContextCompat.getColor(this,R.color.color_toast_info))

        }
        ToastTypes.WARNING->{
            ivToast.setImageResource(R.drawable.ic_toast_warning)
            cardRootToast.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_warning))
            rltContent.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_warning_light))
            //    ivCloseToast.setColorFilter(ContextCompat.getColor(this,R.color.color_toast_warning))

        }
        ToastTypes.ERROR->{
            ivToast.setImageResource(R.drawable.ic_toast_error)
            cardRootToast.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_error))
            rltContent.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_error_light))
            //     ivCloseToast.setColorFilter(ContextCompat.getColor(this,R.color.color_toast_error))

        }
        else ->{
            cardRootToast.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_default))
            rltContent.setBackgroundColor(ContextCompat.getColor(this,R.color.color_toast_default_light))
            //      ivCloseToast.setColorFilter(ContextCompat.getColor(this,R.color.color_toast_default))

        }
    }

    if(toastTypes==ToastTypes.DEFAULT){
        ivToast.visibility = View.GONE
        tvTitleToast.setTextColor(ContextCompat.getColor(this,R.color.white))
        tvDesToast.setTextColor(ContextCompat.getColor(this,R.color.white))

    }else{
        ivToast.visibility = View.VISIBLE
        tvTitleToast.setTextColor(ContextCompat.getColor(this,R.color.black))
        tvDesToast.setTextColor(ContextCompat.getColor(this,R.color.black))
    }
    tvDesToast.visibility=if(messageDetail.isEmpty()) View.GONE else View.VISIBLE

    toast = Toast(this)
    toast!!.duration = if(isLongTime)Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    toast!!.setGravity(Gravity.TOP, 0, 0)
    toast!!.view = layout
    toast!!.show()

    /* // Create the custom snackbar
     val customSnackbar = Snackbar.make(layout, "", Snackbar.LENGTH_SHORT)
     (customSnackbar.view as ViewGroup).addView(layout)
     // Show the custom snackbar
     customSnackbar.show()*/
}







