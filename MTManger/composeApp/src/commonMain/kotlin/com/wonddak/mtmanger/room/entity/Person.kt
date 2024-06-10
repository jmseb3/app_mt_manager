package com.wonddak.mtmanger.room.entity

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
data class Person(
    @PrimaryKey(autoGenerate = true) val personId: Int?,
    val mtId: Int,
    val name: String,
    val phoneNumber: String,
    val paymentFee: Int
) {
    fun toSimplePerson(): SimplePerson {
        return SimplePerson(name, phoneNumber, paymentFee.toString())
    }
}

data class SimplePerson(
    var name: String = "",
    var phoneNumber: String = "",
    var paymentFee: String = ""
) {
    fun isConfirm(): Boolean {
        return name.isNotEmpty() && phoneNumber.isNotEmpty() && paymentFee.isNotEmpty()
    }
}