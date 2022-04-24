package id.dipoengoro.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(night: SleepNight)

    @Update
    fun update(night: SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Long): SleepNight

    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getToNight(): SleepNight?

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :sleepNightKey")
    fun getNightWithId(sleepNightKey: Long): LiveData<SleepNight>
}