package com.wonddak.mtmanger.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.java.KoinJavaComponent

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext : Context = KoinJavaComponent.getKoin().get()
    val dbFile = appContext.getDatabasePath(DB_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).createFromAsset("categoryList.db")
}