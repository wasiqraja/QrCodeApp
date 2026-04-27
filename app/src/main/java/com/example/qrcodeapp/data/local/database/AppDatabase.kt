package com.example.qrcodeapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qrcodeapp.data.local.dao.HistoryDao
import com.example.qrcodeapp.data.local.entitiy.HistoryCreateEntity
import com.example.qrcodeapp.data.local.entitiy.HistoryScannedEntity

@Database(
    entities    = [HistoryScannedEntity::class, HistoryCreateEntity::class],
    version     = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

}