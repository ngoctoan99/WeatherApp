package com.example.weatherapp.extension

import android.text.format.DateUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.formatToViewDateTimeDefaults(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("EEEE hh:mm a", Locale.getDefault())
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }

}


fun String.formatToViewDateTimeDefaultsTZ(): String {

    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("EEEE hh:mm a", Locale.getDefault())
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }

}

fun String.convertToAddress(): String {
//    val sentence = "The address is sfgdfgdgf 123 thao dien phuong6 quân6 ho chi minh city"
    val pattern: Pattern = Pattern.compile("\\d+\\s+\\w+\\s+\\w+\\s+\\w+\\s+\\w+")
    val matcher: Matcher = pattern.matcher(this)

    if (matcher.find()) {
        Timber.d("address ${matcher.group()}")
        return matcher.group()

        // Do something with the extracted address, such as displaying it on the screen
    }
    return ""
}

fun Date.formatToTruncatedDateTime(): String {
    val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: yyyy-MM-dd
 */
fun Date.formatToServerDateDefaults(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToServerTimeDefaults(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: dd/MM/yyyy HH:mm:ss
 */
fun Date.formatToViewDateTimeDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}


/**
 * Pattern: dd/MM/yyyy
 */
fun Date.formatToViewDateDefaults(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Pattern: HH:mm:ss
 */
fun Date.formatToViewTimeDefaults(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(this)
}

fun isToday(whenInMillis: Long): Boolean {
    return DateUtils.isToday(whenInMillis)
}

fun isTomorrow(whenInMillis: Long): Boolean {
    return DateUtils.isToday(whenInMillis - DateUtils.DAY_IN_MILLIS)
}

fun isYesterday(whenInMillis: Long): Boolean {
    return DateUtils.isToday(whenInMillis + DateUtils.DAY_IN_MILLIS)
}

fun Date.isYesterday(): Boolean = DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)

fun Date.isToday(): Boolean = DateUtils.isToday(this.time)


fun String.checkIsToday(): Boolean {

    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.parse(this)?.isToday() ?: false
    } catch (e: Exception) {
        false
    }
}

fun getCurrentTime(): String {
    val currentTime = System.currentTimeMillis()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val date = Date(currentTime)
    return sdf.format(date)
}





fun isWithinPreviousDay(timeString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    //dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val time = dateFormat.parse(timeString)?.time ?: return false
    val currentTime = System.currentTimeMillis()
    val previousDay = currentTime - 86400000L // one day in milliseconds
    return time >= previousDay && time <= currentTime
}

fun isWithinPreviousWeek(timeString: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    //dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val time = dateFormat.parse(timeString)?.time ?: return false
    val currentTime = System.currentTimeMillis()
    val previousWeek = currentTime - 604800000L // one week in milliseconds

    return time >= previousWeek && time <= currentTime
}



fun String.formatDateDayZ(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        // sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("h:mm a", Locale.getDefault())
        // formatDate.timeZone = TimeZone.getTimeZone("UTC")
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }
}

fun String.formatDateWeekZ(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //    sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("EEE h:mm a", Locale.getDefault())
        //  formatDate.timeZone = TimeZone.getTimeZone("UTC")
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }
}
fun String.formatDateZ(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //   sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("MMM dd 'AT' h:mm a", Locale.getDefault())
        //   formatDate.timeZone = TimeZone.getTimeZone("UTC")
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }
}


fun String.formatDayMonthYear(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //   sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        //   formatDate.timeZone = TimeZone.getTimeZone("UTC")
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }
}


fun String.formatHourDayMonthYear(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        //   sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val values = sdf.parse(this)
        val formatDate = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault())
        //   formatDate.timeZone = TimeZone.getTimeZone("UTC")
        return formatDate.format(values)
    } catch (e: Exception) {
        ""
    }
}


fun compareTwoDateZ(dateStr1: String, dateStr2: String): Boolean {
    try {
        Timber.i("HAOHAO dateStr1: $dateStr1")
        Timber.i("HAOHAO dateStr2: $dateStr2")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date1 = dateFormat.parse(dateStr1)
        val date2 = dateFormat.parse(dateStr2)


        return date1 > date2

    }catch (ex:Exception){
        return false
    }

}

fun String.formatToViewDateTime(): String {
    return try {

        if(isWithinPreviousDay(this)){
            this.formatDateDayZ()
        }else if(isWithinPreviousWeek(this)){
            this.formatDateWeekZ()

        }else{
            this.formatDateZ()
        }

    } catch (e: Exception) {
        ""
    }

}
fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}