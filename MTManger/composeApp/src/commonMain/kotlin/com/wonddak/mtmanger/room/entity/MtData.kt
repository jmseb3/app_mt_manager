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

    val simpleTitle :String
        get() = "$mtTitle\n$mtStart ~ $mtEnd"
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

) {
    //회비 총합
    val getAllPersonPayFee
        get() = personList.sumOf { it.paymentFee }

    //지출 총합
    val sumOfGoodsFee
        get() = buyGoodList.sumOf { it.price * it.count }

    //가용 금액
    val availableAmount
        get() = getAllPersonPayFee - sumOfGoodsFee

    val isEmptyPerson
        get() = personList.isEmpty()

    val totalPersonCount
        get() = personList.size

    val getDistributionPrice : Pair<Int,Int>
        get() = if(availableAmount >= 0) {
            availableAmount / totalPersonCount to availableAmount % totalPersonCount
        } else {
            -availableAmount / totalPersonCount to -availableAmount % totalPersonCount
        }
}