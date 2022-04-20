package id.dipoengoro.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import id.dipoengoro.sleeptracker.database.SleepDatabaseDao
import id.dipoengoro.sleeptracker.database.SleepNight
import id.dipoengoro.sleeptracker.formatNights
import kotlinx.coroutines.*

class SleepTrackerViewModel(
    private val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()
    val nights = database.getAllNights()

    val nightString = Transformations.map(nights) {
        formatNights(it, application.resources)
    }

    val startButtonEnable = Transformations.map(tonight) {
        null == it
    }

    val stopButtonEnable = Transformations.map(tonight) {
        null != it
    }

    val clearButtonEnable = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    fun doneNavigating() { _navigateToSleepQuality.value = null }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? =
        withContext(Dispatchers.IO) {
            var night = database.getToNight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) =
        withContext(Dispatchers.IO) {
            database.insert(night)
        }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) =
        withContext(Dispatchers.IO) {
            database.update(night)
        }

    fun onClear() = uiScope.launch {
        clear()
        tonight.value = null
    }

    private suspend fun clear() = withContext(Dispatchers.IO) {
        database.clear()
    }

}