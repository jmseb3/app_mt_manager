package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.room.entity.MtData
import kotlinx.coroutines.flow.Flow

@Dao
interface MtDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMtData(mtData: MtData) :Long

    @Delete
    fun deleteMtData(mtData: MtData)

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    fun getMtData(): Flow<List<MtData>>

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    fun getMtDatadata(): List<MtData>

    @Query("SELECT * FROM MtData WHERE mtDataId= :MtDataId")
    fun getMtDataById(MtDataId: Int?): MtData

    @Query("SELECT * FROM MtData WHERE mtTitle= :MtTitle")
    fun getMtDataByTitle(MtTitle: String): MtData

    @Query("DELETE FROM MtData WHERE mtTitle= :MtTitle")
    fun deleteMtDataByTitle(MtTitle: String)

    @Query("DELETE From MtData")
    fun clearMtDataS()

    @Transaction
    @Query("SELECT * FROM MtData WHERE mtDataId=:mtid")
    fun getMtDataList(mtid: Int): Flow<MtDataList?>
}