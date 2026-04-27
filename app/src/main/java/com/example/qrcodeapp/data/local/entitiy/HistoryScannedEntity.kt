package com.example.qrcodeapp.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryScannedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val data: String,
    val imageRes: Int,
    val createdAt: Long = System.currentTimeMillis()
)


