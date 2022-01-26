package com.saklayen.scheduler.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.saklayen.scheduler.database.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletsDao: BaseDao<Wallet> {
    @Query("SELECT rowid, currencyName, balance FROM wallet")
    fun getWallets(): Flow<List<Wallet>>?

    @Query("SELECT COUNT(currencyName) FROM wallet")
    fun getWalletCounts(): Flow<Int>?
}