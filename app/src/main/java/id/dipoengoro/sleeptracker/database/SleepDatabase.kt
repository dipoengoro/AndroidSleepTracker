package id.dipoengoro.sleeptracker.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        @InternalCoroutinesApi
        fun getInstance(app: Application): SleepDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    app,
                    SleepDatabase::class.java,
                    "sleep_history_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}