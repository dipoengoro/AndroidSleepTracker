package id.dipoengoro.sleeptracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import id.dipoengoro.sleeptracker.database.SleepDatabaseDao

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

}