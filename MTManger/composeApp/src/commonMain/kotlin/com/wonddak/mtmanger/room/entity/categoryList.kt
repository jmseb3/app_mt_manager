package com.wonddak.mtmanger.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class categoryList(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
)