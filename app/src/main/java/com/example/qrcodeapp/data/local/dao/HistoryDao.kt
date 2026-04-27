package com.example.qrcodeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qrcodeapp.data.local.entitiy.HistoryCreateEntity
import com.example.qrcodeapp.data.local.entitiy.HistoryScannedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entity: HistoryScannedEntity)

    @Query("SELECT * FROM history_table ORDER BY createdAt DESC")
    fun getAllHistory(): Flow<List<HistoryScannedEntity>>

    @Query("SELECT * FROM history_table WHERE id = :id")
    suspend fun getHistoryById(id: Int): HistoryScannedEntity?

    @Delete
    suspend fun deleteHistory(entity: HistoryScannedEntity)

    @Query("DELETE FROM history_table")
    suspend fun deleteAllHistory()


    /**
     *  this is history-created
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryCreate(entity: HistoryCreateEntity)

    @Query("SELECT * FROM history_create_table ORDER BY createdAt DESC")
    fun getAllHistoryCreate(): Flow<List<HistoryCreateEntity>>

    @Query("SELECT * FROM history_create_table WHERE id = :id")
    suspend fun getHistoryCreateById(id: Int): HistoryCreateEntity?

    @Delete
    suspend fun deleteHistoryCreate(entity: HistoryCreateEntity)

    @Query("DELETE FROM history_create_table")
    suspend fun deleteAllHistoryCreate()

}