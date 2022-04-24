package id.dipoengoro.sleeptracker.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.dipoengoro.sleeptracker.database.SleepDatabaseDao
import id.dipoengoro.sleeptracker.database.SleepNight

class SleepDetailViewModel(
    sleepNightKey: Long = 0L,
    dataSource: SleepDatabaseDao
) : ViewModel() {
    private val database = dataSource

    private val night = MediatorLiveData<SleepNight>()
    fun getNight() = night

    init {
        night.addSource(database.getNightWithId(sleepNightKey), night::setValue)
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }
}