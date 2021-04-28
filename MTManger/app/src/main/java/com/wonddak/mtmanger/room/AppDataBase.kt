package com.wonddak.mtmanger.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MtData::class, Person::class,BuyGood::class,categoryList::class,Plan::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MtDataDao(): MtDataDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "MtDatabaase.db"
                ).createFromAsset("categoryList.db")
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}