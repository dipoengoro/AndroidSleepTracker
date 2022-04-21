package id.dipoengoro.sleeptracker

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import id.dipoengoro.sleeptracker.database.SleepNight
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

fun convertNumericQualityToString(quality: Int, resources: Resources): String {
    var qualityString = resources.getString(R.string.three_ok)
    when (quality) {
        -1 -> qualityString = "--"
        0 -> qualityString = resources.getString(R.string.zero_very_bad)
        1 -> qualityString = resources.getString(R.string.one_poor)
        2 -> qualityString = resources.getString(R.string.two_soso)
        4 -> qualityString = resources.getString(R.string.four_pretty_good)
        5 -> qualityString = resources.getString(R.string.five_excellent)
    }
    return qualityString
}

private fun weekdayString(startTimeMilli: Long) =
    SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)

private fun getTheCorrectConverter(type: TimeUnit, sourceDuration: Long) =
    type.convert(sourceDuration, TimeUnit.MILLISECONDS)

fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    (endTimeMilli - startTimeMilli).apply {
        SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)
            .also { weekday ->
                return when {
                    this < ONE_MINUTE_MILLIS -> getTheCorrectConverter(TimeUnit.SECONDS, this).let {
                        res.getString(R.string.seconds_length, it, weekday)
                    }
                    this < ONE_HOUR_MILLIS -> getTheCorrectConverter(TimeUnit.MINUTES, this).let {
                        res.getString(R.string.minutes_length, it, weekday)
                    }
                    else -> getTheCorrectConverter(TimeUnit.HOURS, this).let {
                        res.getString(R.string.hours_length, it, weekday)
                    }
                }
            }
    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

fun formatNights(nights: List<SleepNight>, resources: Resources): Spanned {
    val stringBuilder = StringBuilder()
    stringBuilder.apply {
        append(resources.getString(R.string.title))
        nights.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            append("\t${convertLongToDateString(it.startTimeMilli)}<br>")
            if (it.endTimeMilli != it.startTimeMilli) {
                append(resources.getString(R.string.end_time))
                append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
                append(resources.getString(R.string.quality))
                append("\t${convertNumericQualityToString(it.sleepQuality, resources)}<br>")
                append(resources.getString(R.string.hours_slept))
                append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
                append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
                append("${it.endTimeMilli.minus(it.startTimeMilli / 1000)}<br><br>")
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(stringBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

