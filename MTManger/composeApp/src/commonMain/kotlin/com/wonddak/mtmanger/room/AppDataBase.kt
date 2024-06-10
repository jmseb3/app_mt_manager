package com.wonddak.mtmanger.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [MtData::class, Person::class, BuyGood::class, categoryList::class, Plan::class],
    version = 7,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun MtDataDao(): MtDataDao
    abstract fun PlanDao(): PlanDao
    abstract fun PersonDao(): PersonDao
    abstract fun BuyGoodDao(): BuyGoodDao
    abstract fun CategoryListDao(): CategoryListDao
}

internal const val DB_NAME = "MtDatabaase.db"

private val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE Plan ADD COLUMN imgBytes BLOB")
    }
}
private val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_Person_mtId` ON `Person` (`mtId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_BuyGood_mtId` ON `BuyGood` (`mtId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_Plan_mtId` ON `Plan` (`mtId`)")
    }
}
expect fun getDatabaseBuilder() : RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(): AppDatabase {
    return getDatabaseBuilder()
        .addMigrations(MIGRATION_5_6)
        .addMigrations(MIGRATION_6_7)
//        .fallbackToDestructiveMigrationOnDowngrade()
//        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}