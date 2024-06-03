package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.BuyGood

@Dao
interface BuyGoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuyGood(buyGood: BuyGood)

    @Query("DELETE FROM BuyGood WHERE buyGoodId = :buyGoodId")
    fun deleteBuyGoodById(buyGoodId: Int)

    @Query("DELETE FROM BuyGood WHERE mtId =:id")
    fun clearBuyGoods(id: Int)

}