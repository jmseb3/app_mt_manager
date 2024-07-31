package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.entity.BuyGood

@Dao
interface BuyGoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyGood(buyGood: BuyGood)

    @Query("DELETE FROM BuyGood WHERE buyGoodId = :buyGoodId")
    suspend fun deleteBuyGoodById(buyGoodId: Int)

    @Query("DELETE FROM BuyGood WHERE mtId =:id")
    suspend fun clearBuyGoods(id: Int)

}