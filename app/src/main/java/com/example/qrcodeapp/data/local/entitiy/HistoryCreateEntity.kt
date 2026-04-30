package com.example.qrcodeapp.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_create_table")
data class HistoryCreateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val originalString: String,
    val resultedText: String,
    val type: String,
    val dateString: String,
    val isFavourite: Boolean = false
)


