package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.entity.Plan

@Dao
interface PlanDao {
    @Query("UPDATE `Plan` SET imgSrc= :imgSrc WHERE planId= :planId")
    suspend fun updatePlanById(planId: Int, imgSrc: String)

    @Query("UPDATE `Plan` SET imgBytes= :imgBytes WHERE planId= :planId")
    suspend fun updateImgBytePlanById(planId: Int, imgBytes: ByteArray)

    @Query("UPDATE `Plan` SET imgsrc=\"\", imgBytes=NULL WHERE planId= :planId")
    suspend fun clearPlanImgById(planId: Int)

    @Query("UPDATE `Plan` SET nowDay= :nowDay , nowPlanTitle= :nowPlanTitle, simpleText= :simpleText WHERE planId= :planId")
    suspend fun updatePlanDialogById(planId: Int, nowDay: String, nowPlanTitle: String, simpleText: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: Plan)

    @Query("DELETE FROM `Plan` WHERE planId =:id")
    suspend fun deletePlanById(id: Int)
}