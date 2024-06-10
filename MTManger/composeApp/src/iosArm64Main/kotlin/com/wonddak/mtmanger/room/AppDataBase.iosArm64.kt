package com.wonddak.mtmanger.room

import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder<AppDatabase>(
        name = providePath(),
        factory =  { AppDatabase::class.instantiateImpl() }
    )
}