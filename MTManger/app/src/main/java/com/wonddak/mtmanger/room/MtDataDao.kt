package com.wonddak.mtmanger.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MtDataDao {

//    mtdata 관련

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMtData(mtData: MtData) :Long

    @Delete
    fun deleteMtData(mtData: MtData)

    @Query("SELECT * FROM MtData ORDER BY mtDataId")
    fun getMtData(): LiveData<List<MtData>>

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

    // 참가자 관련
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Delete
    fun deletePerson(person: Person)

    @Query("SELECT * FROM Person where mtId= :mtid")
    fun getPerson(mtid: Int): List<Person>

    @Query("SELECT * FROM Person")
    fun getPersondata(): List<Person>

    @Query("SELECT * FROM Person WHERE personId= :personId")
    fun getPersonById(personId: Int?): Person

    @Query("DELETE FROM Person WHERE personId = :personId")
    fun deletePersonById(personId: Int)

    @Query("DELETE FROM person WHERE mtId= :mtid")
    fun clearPersons(mtid: Int)
// 상품관련

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBuyGood(buyGood: BuyGood)

    @Delete
    fun deleteBuyGood(buyGood: BuyGood)

    @Query("SELECT * FROM BuyGood where mtId= :mtid")
    fun getBuyGood(mtid: Int): LiveData<List<BuyGood>>

    @Query("SELECT * FROM BuyGood where mtId= :mtid")
    fun getBuyGoodById(mtid: Int): List<BuyGood>

    @Query("SELECT * FROM BuyGood")
    fun getBuyGooddata(): List<BuyGood>


    @Query("DELETE FROM BuyGood WHERE buyGoodId = :buyGoodId")
    fun deleteBuyGoodById(buyGoodId: Int)

    @Query("DELETE FROM BuyGood WHERE mtId =:mtid")
    fun clearBuyGoods(mtid: Int)

    //    카테고리 관련

    @Query("SELECT * FROM categoryList")
    fun getCategorydata(): List<categoryList>

    @Query("SELECT * FROM categoryList")
    fun getCategoryDataList(): Flow<List<categoryList>>

    @Query("SELECT * FROM categoryList WHERE id=:id")
    fun getCategorydatabyId(id:Int): categoryList

    @Query("SELECT * FROM categoryList")
    fun getCategory(): LiveData<List<categoryList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(categoryList: categoryList)

    @Delete
    fun deleteCategory(categoryList: categoryList)

    @Query("DELETE FROM categoryList WHERE id =:id")
    fun deleteCategoryById(id: Int)

//    계획관련

    @Query("SELECT * FROM `Plan` where mtId= :mtid")
    fun getPlanById(mtid: Int): List<Plan>

    @Query("SELECT * FROM `Plan` where mtId= :mtid")
    fun getPlandata(mtid: Int): List<Plan>

    @Query("SELECT * FROM `Plan` where mtId= :mtid")
    fun getPlan(mtid: Int): LiveData<List<Plan>>

    @Query("SELECT * FROM `Plan` WHERE planId= :plainId")
    fun getPlanById(plainId: Int?): Plan

    @Query("UPDATE `Plan` SET imgsrc= :imgsrc WHERE planId= :planId")
    fun updatePlanbyid(planId:Int,imgsrc:String)

    @Query("UPDATE `Plan` SET imgBytes= :imgBytes WHERE planId= :planId")
    fun updateImgBytePlanById(planId:Int,imgBytes:ByteArray)
    @Query("UPDATE `Plan` SET imgsrc=\"\", imgBytes=NULL WHERE planId= :planId")
    fun clearPlanImgById(planId:Int)

    @Query("UPDATE `Plan` SET nowday= :nowday , nowplantitle= :nowplantitle, simpletext= :simpletext WHERE planId= :planId")
    fun updatePlandialogbyid(planId:Int,nowday:String,nowplantitle:String,simpletext:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlan(plan: Plan)

    @Delete
    fun deletePlan(plan: Plan)

    @Query("DELETE FROM `Plan` WHERE planId =:id")
    fun deletePlanById(id: Int)

//    트랙잭션

    @Transaction
    @Query("SELECT * FROM MtData WHERE mtDataId=:mtid")
    fun getMtDataList(mtid: Int): Flow<MtDataList>
}