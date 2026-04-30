package com.example.qrcodeapp.data.mapper

import com.example.qrcodeapp.data.local.entitiy.HistoryCreateEntity
import com.example.qrcodeapp.domain.model.History

fun HistoryCreateEntity.toDomain(): History {
    return History(
        id = this.id,
        originalString = this.originalString,
        resultedText = this.resultedText,
        type = this.type,
        dateString = this.dateString,
        isFavourite = this.isFavourite
    )
}

fun History.toEntityCreate(): HistoryCreateEntity {
    return HistoryCreateEntity(
        id = this.id,
        originalString = this.dateString,
        resultedText = this.resultedText,
        type = this.type,
        dateString = this.dateString,
        isFavourite = this.isFavourite
    )
}