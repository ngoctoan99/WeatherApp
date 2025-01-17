package com.example.weatherapp.extension


import android.content.Context

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
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
        DayOfWeek.SUNDAY -> "Sunday`"
    }
}






