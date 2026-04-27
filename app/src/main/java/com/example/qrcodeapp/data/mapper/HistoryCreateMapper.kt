package com.example.qrcodeapp.data.mapper

import com.example.qrcodeapp.data.local.entitiy.HistoryCreateEntity
import com.example.qrcodeapp.domain.model.History

fun HistoryCreateEntity.toDomain(): History {
    return History(
        id = this.id,
        data = this.data,
        imageRes = this.imageRes,
        createdAt = this.createdAt
    )
}

fun History.toEntityCreate(): HistoryCreateEntity {
    return HistoryCreateEntity(
        id = this.id,
        data = this.data,
        imageRes = this.imageRes,
        createdAt = this.createdAt
    )
}