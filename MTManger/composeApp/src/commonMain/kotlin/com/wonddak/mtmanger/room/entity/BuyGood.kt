package com.wonddak.mtmanger.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wonddak.mtmanger.toPriceString

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

    fun getItemList() = listOf(
        category,
        name,
        getCountString(),
        getPriceString(),
        getTotalString()
    )

    fun toSimple(): SimpleBuyGood =
        SimpleBuyGood(category, name, count.toString(), price.toString())
}

data class SimpleBuyGood(
    var category: String = "",
    var name: String = "",
    var count: String = "",
    var price: String = ""
) {
    fun isConfirm(): Boolean {
        return category.isNotEmpty() && name.isNotEmpty() && count.isNotEmpty() && price.isNotEmpty()
    }
}