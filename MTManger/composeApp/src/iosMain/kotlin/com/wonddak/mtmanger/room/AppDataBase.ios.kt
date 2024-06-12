package com.wonddak.mtmanger.room

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

//actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
//    val dbFilePath = NSHomeDirectory() + "/" + DB_NAME
//    return Room.databaseBuilder<AppDatabase>(
//        name = dbFilePath,
//        factory =  { AppDatabase::class.instantiateImpl() }
//    )
//}

@OptIn(ExperimentalForeignApi::class)
internal fun providePath() :String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSLibraryDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return (requireNotNull(documentDirectory).path + "/$DB_NAME")
}