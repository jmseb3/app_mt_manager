package com.wonddak.mtmanger.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import javax.inject.Inject

class MTRepository @Inject constructor(
    val mtDataDao: MtDataDao
) {

    fun getMtDataList(id: Int) = flow {
        emit(Resource.Loading)
        mtDataDao.getMtDataList(id).collect {
            emit(Resource.Success(it))
        }

    }

    fun getCategoryList() = flow {
        mtDataDao.getCategoryDataList().collect { categoryList ->
            emit(categoryList)
            Log.i("JWH","get Categort $categoryList")
        }
    }

    suspend fun checkEndDateById(mtId: Int, tempDate: String): Boolean {
        val transFormat = SimpleDateFormat("yyyy.MM.dd")

        val nowDate = mtDataDao.getMtDataById(mtId)

        val mtEndData = transFormat.parse(nowDate.mtEnd)
        val mtStartData = transFormat.parse(nowDate.mtStart)
        val nowCheckData = transFormat.parse(tempDate)

        return (nowCheckData in mtStartData..mtEndData)
    }

    suspend fun insertMtData(mtData: MtData): Long {
        return mtDataDao.insertMtData(mtData)
    }

    suspend fun insertPerson(person: Person) {
        mtDataDao.insertPerson(person)
    }

    suspend fun clearPerson(mtId: Int) {
        mtDataDao.clearPersons(mtId)
    }

    suspend fun deletePersonById(personId: Int) {
        mtDataDao.deletePersonById(personId)
    }

    suspend fun insertBuyGood(buyGood: BuyGood) {
        mtDataDao.insertBuyGood(buyGood)
    }

    suspend fun clearBuyGood(mtId: Int) {
        mtDataDao.clearBuyGoods(mtId)
    }

    suspend fun deleteBuyGoodById(buyGoodId: Int) {
        mtDataDao.deleteBuyGoodById(buyGoodId)
    }

    suspend fun insertPlan(plan: Plan) {
        mtDataDao.insertPlan(plan)
    }

    suspend fun updatePlanImgSrcById(planId: Int, imgSrc: String = "") {
        mtDataDao.updatePlanbyid(planId, imgSrc)
    }

    suspend fun updatePlanImgBytesById(planId: Int, img: ByteArray) {
        mtDataDao.updateImgBytePlanById(planId, img)
    }

    suspend fun clearPlanImgById(planId: Int) {
        mtDataDao.clearPlanImgById(planId)
    }

    suspend fun updatePlanById(planId: Int, day: String, title: String, text: String) {
        mtDataDao.updatePlandialogbyid(planId, day, title, text)
    }

    suspend fun deletePlanById(planId: Int) {
        mtDataDao.deletePlanById(planId)
    }

    suspend fun insertCategory(categoryList: categoryList){
        mtDataDao.insertCategory(categoryList)
    }

    suspend fun deleteCategoryById(categoryId:Int) {
        mtDataDao.deleteCategoryById(categoryId)
    }

    fun getMtTotalList(): LiveData<List<MtData>> {
        return mtDataDao.getMtData()
    }

}