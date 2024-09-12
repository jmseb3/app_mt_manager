package com.wonddak.mtmanger.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.byUnicodePattern
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

    val simpleTitle: String
        get() = "$mtTitle\n$mtStart ~ $mtEnd"

    fun getDateList(): List<Instant> {
        val startDate = Instant.parse(
            mtStart.replace(
                ".",
                "-"
            ) + "T00:00:00Z"
        )

        val endDate = Instant.parse(
            mtEnd.replace(
                ".",
                "-"
            ) + "T00:00:00Z"
        )
        if (startDate == endDate) {
            return listOf(startDate)
        }
        val data = mutableListOf<Instant>(startDate)
        var nextDate = startDate.plus(1.toDuration(DurationUnit.DAYS))
        while (true) {
            data.add(nextDate)
            nextDate = nextDate.plus(1.toDuration(DurationUnit.DAYS))
            if (nextDate == endDate) {
                data.add(nextDate)
                break
            }
        }
        return data
    }

    fun getMapDate(): Map<Int, Map<Int, List<Int>>> {
        val result = mutableMapOf<Int, MutableMap<Int, MutableList<Int>>>()
        for (instant in getDateList()) {
            val date = instant.format(DateTimeComponents.Format {
                byUnicodePattern("yyyy-MM-dd")
            })
            val (yyyy, mm, dd) = date.split("-").map { it.toInt() }
            if (result.containsKey(yyyy)) {
                val yearMap = result[yyyy]!!
                if (yearMap.containsKey(mm)) {
                    result[yyyy]!![mm]!!.add(dd)
                } else {
                    result[yyyy]!![mm] = mutableListOf(dd)
                }
            } else {
                result[yyyy] = mutableMapOf(mm to mutableListOf(dd))
            }
        }
        return result
    }
}

data class MtDataList(
    @Embedded val mtData: MtData,
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
        get() = totalPersonCount == 0

    val totalPersonCount
        get() = personList.size

    val getDistributionPrice: Pair<Int, Int>
        get() = if (totalPersonCount > 0) {
            if (availableAmount >= 0) {
                availableAmount / totalPersonCount to availableAmount % totalPersonCount
            } else {
                -availableAmount / totalPersonCount to -availableAmount % totalPersonCount
            }
        } else {
            0 to 0
        }
}