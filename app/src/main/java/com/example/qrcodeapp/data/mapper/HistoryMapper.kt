package com.example.qrcodeapp.data.mapper

import com.example.qrcodeapp.data.local.entitiy.HistoryScannedEntity
import com.example.qrcodeapp.domain.model.History

fun HistoryScannedEntity.toDomain(): History {
    return History(
        id = this.id,
        originalString = this.originalString,
        resultedText = this.resultedText,
        type = this.type,
        dateString = this.dateString,
        isFavourite=this.isFavourite

    )
}

fun History.toEntity(): HistoryScannedEntity {
    return HistoryScannedEntity(
        id = this.id,
        originalString = this.originalString,
        resultedText = this.resultedText,
        type = this.type,
        dateString = this.dateString,
        isFavourite=this.isFavourite
    )
}