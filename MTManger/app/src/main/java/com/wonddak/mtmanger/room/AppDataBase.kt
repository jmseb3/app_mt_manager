package com.wonddak.mtmanger.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao

@Database(
    entities = [MtData::class, Person::class,BuyGood::class,categoryList::class,Plan::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MtDataDao(): MtDataDao
    abstract fun PlanDao(): PlanDao
    abstract fun PersonDao(): PersonDao
    abstract fun BuyGoodDao(): BuyGoodDao
    abstract fun CategoryListDao(): CategoryListDao

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
                    .addMigrations(MIGRATION_5_6)
                    .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}