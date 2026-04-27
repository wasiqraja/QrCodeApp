package com.example.qrcodeapp.data.mapper

import com.example.qrcodeapp.data.local.entitiy.HistoryScannedEntity
import com.example.qrcodeapp.domain.model.History

fun HistoryScannedEntity.toDomain(): History {
    return History(
        id = this.id,
        data = this.data,
        imageRes = this.imageRes,
        createdAt = this.createdAt
    )
}

fun History.toEntity(): HistoryScannedEntity {
    return HistoryScannedEntity(
        id = this.id,
        data = this.data,
        imageRes = this.imageRes,
        createdAt = this.createdAt
    )
}