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
) {
    val imageExist :Boolean
    get() = imgBytes?.isNotEmpty() ?: imgSrc.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Plan

        if (planId != other.planId) return false
        if (mtId != other.mtId) return false
        if (nowDay != other.nowDay) return false
        if (nowPlanTitle != other.nowPlanTitle) return false
        if (simpleText != other.simpleText) return false
        if (imgSrc != other.imgSrc) return false
        if (imgBytes != null) {
            if (other.imgBytes == null) return false
            if (!imgBytes.contentEquals(other.imgBytes)) return false
        } else if (other.imgBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = planId ?: 0
        result = 31 * result + mtId
        result = 31 * result + nowDay.hashCode()
        result = 31 * result + nowPlanTitle.hashCode()
        result = 31 * result + simpleText.hashCode()
        result = 31 * result + imgSrc.hashCode()
        result = 31 * result + (imgBytes?.contentHashCode() ?: 0)
        return result
    }
}

data class PlanData(
    val nowDay: String = "",
    val nowPlanTitle: String = "",
    val simpleText: String = "",
    val imgBytes: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlanData

        if (nowDay != other.nowDay) return false
        if (nowPlanTitle != other.nowPlanTitle) return false
        if (simpleText != other.simpleText) return false
        if (imgBytes != null) {
            if (other.imgBytes == null) return false
            if (!imgBytes.contentEquals(other.imgBytes)) return false
        } else if (other.imgBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nowDay.hashCode()
        result = 31 * result + nowPlanTitle.hashCode()
        result = 31 * result + simpleText.hashCode()
        result = 31 * result + (imgBytes?.contentHashCode() ?: 0)
        return result
    }
}