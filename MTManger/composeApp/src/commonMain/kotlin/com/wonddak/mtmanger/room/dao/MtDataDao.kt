package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.MtDataList
import kotlinx.coroutines.flow.Flow

@Dao
interface MtDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMtData(mtData: MtData) :Long

    @Delete
    suspend fun deleteMtData(mtData: MtData)

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    fun getMtData(): Flow<List<MtData>>

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    suspend fun getMtDataData(): List<MtData>

    @Query("SELECT * FROM MtData WHERE mtDataId= :mtDataId")
    suspend fun getMtDataById(mtDataId: Int?): MtData

    @Query("DELETE From MtData")
    suspend fun clearMtDataS()

    @Transaction
    @Query("SELECT * FROM MtData WHERE mtDataId=:mtId")
    fun getMtDataList(mtId: Int): Flow<MtDataList?>
}