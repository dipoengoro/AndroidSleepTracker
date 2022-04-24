package id.dipoengoro.sleeptracker.sleeptracker

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dipoengoro.sleeptracker.R
import id.dipoengoro.sleeptracker.convertDurationToFormatted
import id.dipoengoro.sleeptracker.convertNumericQualityToString
import id.dipoengoro.sleeptracker.database.SleepNight

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) =
    item?.run {
        text = convertDurationToFormatted(
            startTimeMilli, endTimeMilli, context.resources
        )
    }

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?) =
    item?.run {
        text = convertNumericQualityToString(
            sleepQuality, context.resources
        ).also {
            gravity = when (it.length) {
                in 0..5 -> Gravity.CENTER
                else -> Gravity.START
            }
        }
    }

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) =
    item?.run {
        setImageResource(
            when (sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_launcher_foreground
            }
        )
    }

@BindingAdapter("listSleep")
fun RecyclerView.setListSleep(item: List<SleepNight>?) =
    item?.run {
        (adapter as SleepNightAdapter).addHeaderAndSubmitList(this)
        GridLayoutManager(context, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (position) {
                    0 -> 3
                    else -> 1
                }
            }
        }.also {
            layoutManager = it
        }

    }