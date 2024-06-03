package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.Plan

@Dao
interface PlanDao {
    @Query("UPDATE `Plan` SET imgSrc= :imgSrc WHERE planId= :planId")
    fun updatePlanById(planId: Int, imgSrc: String)

    @Query("UPDATE `Plan` SET imgBytes= :imgBytes WHERE planId= :planId")
    fun updateImgBytePlanById(planId: Int, imgBytes: ByteArray)

    @Query("UPDATE `Plan` SET imgsrc=\"\", imgBytes=NULL WHERE planId= :planId")
    fun clearPlanImgById(planId: Int)

    @Query("UPDATE `Plan` SET nowDay= :nowDay , nowPlanTitle= :nowPlanTitle, simpleText= :simpleText WHERE planId= :planId")
    fun updatePlanDialogById(planId: Int, nowDay: String, nowPlanTitle: String, simpleText: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlan(plan: Plan)

    @Query("DELETE FROM `Plan` WHERE planId =:id")
    fun deletePlanById(id: Int)
}