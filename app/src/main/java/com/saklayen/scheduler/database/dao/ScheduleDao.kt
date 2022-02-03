package com.saklayen.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.saklayen.scheduler.database.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao : BaseDao<Schedule> {
    @Query("select *,  `rowid` FROM schedule where isStarted = :isStarted")
     fun getScheduledApps(isStarted: Boolean): Flow<List<Schedule>>?

    @Query("SELECT EXISTS (SELECT 1 FROM schedule WHERE time = :time)")
     fun checkConflict(time: String): Flow<Boolean>?

    @Query("delete from schedule where rowid = :rowId")
    suspend fun deleteSchedule(rowId: Int)

    @Query("update schedule set time = :time where rowid = :rowId")
    suspend fun updateSchedule(rowId: Int, time: String)

    @Query("update schedule set isStarted = :isStarted where requestCode = :requestCode")
    suspend fun updateScheduleStatus(requestCode: Int, isStarted: Boolean)
}