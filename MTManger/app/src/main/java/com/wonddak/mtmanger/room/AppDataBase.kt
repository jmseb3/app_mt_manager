package com.wonddak.mtmanger.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [MtData::class, Person::class,BuyGood::class,categoryList::class,Plan::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MtDataDao(): MtDataDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Plan ADD COLUMN imgBytes BLOB")
            }
        }
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "MtDatabaase.db"
                ).createFromAsset("categoryList.db")
//                    .addMigrations(MIGRATION_5_6)
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}