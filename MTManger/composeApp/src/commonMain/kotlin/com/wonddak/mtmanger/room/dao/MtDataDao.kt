package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.MtDataList
import kotlinx.coroutines.flow.Flow

@Dao
interface MtDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMtData(mtData: MtData) :Long

    @Update
    suspend fun updateMtData(mtData: MtData)

    @Delete
    suspend fun deleteMtData(mtData: MtData)

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    fun getMtData(): Flow<List<MtData>>

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    suspend fun getMtDatadata(): List<MtData>

    @Query("SELECT * FROM MtData WHERE mtDataId= :MtDataId")
    suspend fun getMtDataById(MtDataId: Int?): MtData

    @Query("SELECT * FROM MtData WHERE mtTitle= :MtTitle")
    suspend fun getMtDataByTitle(MtTitle: String): MtData

    @Query("DELETE FROM MtData WHERE mtTitle= :MtTitle")
    suspend fun deleteMtDataByTitle(MtTitle: String)

    @Query("DELETE From MtData")
    suspend fun clearMtDataS()

    @Transaction
    @Query("SELECT * FROM MtData WHERE mtDataId=:mtId")
    fun getMtDataList(mtId: Int): Flow<MtDataList?>
}