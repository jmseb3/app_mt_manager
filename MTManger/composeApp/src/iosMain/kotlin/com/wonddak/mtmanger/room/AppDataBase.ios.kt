package com.wonddak.mtmanger.room

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory

//actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
//    val dbFilePath = NSHomeDirectory() + "/" + DB_NAME
//    return Room.databaseBuilder<AppDatabase>(
//        name = dbFilePath,
//        factory =  { AppDatabase::class.instantiateImpl() }
//    )
//}

internal fun providePath() :String {
    return  NSHomeDirectory() + "/" + DB_NAME
}