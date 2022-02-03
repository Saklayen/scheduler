package com.saklayen.scheduler.database.repositories

import com.saklayen.scheduler.database.AppDatabase
import com.saklayen.scheduler.database.model.Schedule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleRepositories @Inject constructor(val database: AppDatabase) {
    suspend fun getScheduledApps(isStarted: Boolean): Flow<List<Schedule>>? {
        return database.scheduleDao.getScheduledApps(isStarted)
    }
    suspend fun updateSchedule(schedule: Schedule){
        database.scheduleDao.update(schedule)
    }
    suspend fun updateSchedule(rowId: Int, time: String){
        database.scheduleDao.updateSchedule(rowId,time)
    }
    suspend fun insertSchedule(schedule: Schedule){
        database.scheduleDao.insert(schedule)
    }
    suspend fun checkConflict(time: String): Flow<Boolean>? {
       return database.scheduleDao.checkConflict(time)
    }
    suspend fun deleteSchedule(rowId: Int) {
       return database.scheduleDao.deleteSchedule(rowId)
    }

    suspend fun updateScheduleStatus(requestCode: Int, isStarted: Boolean){
        return database.scheduleDao.updateScheduleStatus(requestCode,isStarted)
    }

}