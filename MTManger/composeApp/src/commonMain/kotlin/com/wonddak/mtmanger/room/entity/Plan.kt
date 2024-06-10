package com.wonddak.mtmanger.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MtData::class,
            parentColumns = arrayOf("mtDataId"),
            childColumns = arrayOf("mtId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["mtId"])
    ]
)
data class Plan(
    @PrimaryKey(autoGenerate = true) val planId: Int?,
    val mtId: Int,
    @ColumnInfo(name = "nowday")
    val nowDay: String = "",
    @ColumnInfo(name = "nowplantitle")
    val nowPlanTitle: String = "",
    @ColumnInfo(name = "simpletext")
    val simpleText: String = "",
    @ColumnInfo(name = "imgsrc")
    val imgSrc: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val imgBytes: ByteArray? = null
)
