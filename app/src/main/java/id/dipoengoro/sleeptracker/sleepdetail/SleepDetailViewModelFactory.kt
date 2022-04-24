package id.dipoengoro.sleeptracker.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.dipoengoro.sleeptracker.database.SleepDatabaseDao


@Suppress("UNCHECKED_CAST")
class SleepDetailViewModelFactory(
    private val sleepNightKey: Long = 0L,
    private val dataSource: SleepDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
            return SleepDetailViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}