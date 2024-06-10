package com.wonddak.mtmanger.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class MtData(
    @PrimaryKey(autoGenerate = true) val mtDataId: Int?,
    val mtTitle: String,
    val fee: Int,
    val mtStart: String,
    val mtEnd: String,
) {
    constructor(title: String, fee: Int, mtStart: String, mtEnd: String) : this(
        null,
        title,
        fee,
        mtStart,
        mtEnd
    )
}

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