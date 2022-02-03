package com.saklayen.scheduler.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "schedule")
@Fts4
@Parcelize
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val rowid: Int = 0,
    val time: String,
    val appName: String,
    val packageName: String,
    val requestCode: String,
    val tag: String,
    val isStarted: Boolean
) : Parcelable
