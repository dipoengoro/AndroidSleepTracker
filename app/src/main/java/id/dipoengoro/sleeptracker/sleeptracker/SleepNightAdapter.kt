package id.dipoengoro.sleeptracker.sleeptracker

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.dipoengoro.sleeptracker.R
import id.dipoengoro.sleeptracker.TextItemViewHolder
import id.dipoengoro.sleeptracker.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

    var data = listOf<SleepNight>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) =
        data[position].let {
            if (it.sleepQuality <= 1) {
                holder.textView.setTextColor(Color.RED)
            } else {
                holder.textView.setTextColor(Color.BLACK)
            }
            holder.textView.text = it.sleepQuality.toString()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder =
        TextItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.text_item_view, parent, false) as TextView
        )
}