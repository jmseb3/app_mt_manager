package com.wonddak.mtmanger.room

import androidx.room.*
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
        return count.toPriceString("")
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
    val nowday: String = "",
    val nowplantitle: String = "",
    val simpletext: String = "",
    val imgsrc: String = "",
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

fun getDummyData(): MtDataList {
    val mtData = MtData(1, "test", 100000, "2023.05.05", "2023.05.06")
    val person = Person(
        1,
        1,
        "person",
        "010-7183-7659",
        5000
    )
    val buyGood = BuyGood(
        1,
        1,
        "test",
        "good",
        3, 500
    )
    val plan = Plan(
        1,
        1,
    )
    return MtDataList(
        mtData,
        listOf(person, person),
        listOf(buyGood, buyGood),
        listOf(plan, plan)
    )
}