package com.wonddak.mtmanger.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.wonddak.mtmanger.toPriceString

@Entity
data class MtData(
    @PrimaryKey(autoGenerate = true) val mtDataId: Int?,
    val mtTitle: String,
    val fee: Int,
    val mtStart: String,
    val mtEnd: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MtData::class,
            parentColumns = arrayOf("mtDataId"),
            childColumns = arrayOf("mtId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Person(
    @PrimaryKey(autoGenerate = true) val personId: Int?,
    val mtId: Int,
    val name: String,
    val phoneNumber: String,
    val paymentFee: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MtData::class,
            parentColumns = arrayOf("mtDataId"),
            childColumns = arrayOf("mtId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BuyGood(
    @PrimaryKey(autoGenerate = true) val buyGoodId: Int?,
    val mtId: Int,
    val category: String,
    val name: String,
    val count: Int,
    val price: Int,
) {
    fun getCountString(): String {
        return count.toPriceString()
    }

    fun getPriceString(): String {
        return price.toPriceString()

    }

    fun getTotalString(): String {
        return (price * count).toPriceString()

    }
}


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MtData::class,
            parentColumns = arrayOf("mtDataId"),
            childColumns = arrayOf("mtId"),
            onDelete = ForeignKey.CASCADE
        )
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

data class MtDataList(
    @Embedded val mtdata: MtData,
    @Relation(
        parentColumn = "mtDataId",
        entityColumn = "mtId"
    )
    val personList: List<Person>,
    @Relation(
        parentColumn = "mtDataId",
        entityColumn = "mtId"
    )
    val buyGoodList: List<BuyGood>,

    @Relation(
        parentColumn = "mtDataId",
        entityColumn = "mtId"
    )
    val planList: List<Plan>
)

@Entity
data class categoryList(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
)