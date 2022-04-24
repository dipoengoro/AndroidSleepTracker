package id.dipoengoro.sleeptracker.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dipoengoro.sleeptracker.R
import id.dipoengoro.sleeptracker.database.SleepNight
import id.dipoengoro.sleeptracker.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(
    private val clickListener: SleepNightListener
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) { submitList(items) }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {
            is ViewHolder -> {
                holder.bind(
                    (getItem(position) as DataItem.SleepNightItem).sleepNight,
                    clickListener
                )
            }
            else -> {}
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    class ViewHolder(private val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder =
                ViewHolder(
                    ListItemSleepNightBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

        }

        fun bind(item: SleepNight, clickListener: SleepNightListener) = with(binding) {
            sleep = item
            itemClickListener = clickListener
            executePendingBindings()
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder =
                TextViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.header,
                        parent,
                        false
                    )
                )
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean =
        oldItem == newItem
}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onCLick(night: SleepNight) = clickListener(night.nightId)
}

sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id: Long
            get() = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    abstract val id: Long
}