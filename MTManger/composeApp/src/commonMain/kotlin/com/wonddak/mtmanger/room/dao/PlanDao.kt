package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wonddak.mtmanger.room.entity.Plan

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: Plan)

    @Update()
    suspend fun updatePlan(plan: Plan): Int

    @Query("UPDATE `Plan` SET imgBytes= :imgBytes WHERE planId= :planId")
    suspend fun updateImgBytePlanById(planId: Int, imgBytes: ByteArray)

    @Query("UPDATE `Plan` SET imgsrc=\"\", imgBytes=NULL WHERE planId= :planId")
    suspend fun clearPlanImgById(planId: Int)

    @Query("DELETE FROM `Plan` WHERE planId =:id")
    suspend fun deletePlanById(id: Int)
}