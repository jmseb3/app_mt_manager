
package com.wonddak.mtmanger.room

import androidx.room.*

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

data class MtDataAndPerson(
        @Embedded val mtdata: MtData,

        @Relation(
                parentColumn = "mtDataId",
                entityColumn = "mtId"
        )

        val Personlists: List<Person>
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
)

data class MtDataAndBuyGood(
        @Embedded val mtdata: MtData,

        @Relation(
                parentColumn = "mtDataId",
                entityColumn = "mtId"
        )

        val buyGoodList: List<BuyGood>
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
data class Plan(
        @PrimaryKey(autoGenerate = true) val planId: Int?,
        val mtId: Int,
        val nowday: String = "날짜를 선택해주세요",
        val nowplantitle: String = "제목을 입력해주세요",
        val simpletext: String = "계획을 입력해주세요",
        val imgsrc: String = ""

)

data class MtDataAndPlan(
        @Embedded val mtdata: MtData,

        @Relation(
                parentColumn = "mtDataId",
                entityColumn = "mtId"
        )

        val buyGoodList: List<BuyGood>
)

@Entity
data class categoryList(
        @PrimaryKey(autoGenerate = true)
        val id: Int?,
        val name: String
)