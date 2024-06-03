package com.wonddak.mtmanger.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wonddak.mtmanger.room.categoryList
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryListDao {
    @Query("SELECT * FROM categoryList")
    fun getCategoryDataList(): Flow<List<categoryList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryList: categoryList)

    @Query("DELETE FROM categoryList WHERE id =:id")
    fun deleteCategoryById(id: Int)
}