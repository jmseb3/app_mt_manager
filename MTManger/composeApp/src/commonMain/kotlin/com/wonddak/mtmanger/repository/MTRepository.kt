package com.wonddak.mtmanger.repository

import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.BuyGood
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.Person
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.room.entity.SimplePerson
import com.wonddak.mtmanger.room.entity.categoryList
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MTRepository(
    private val mtDataDao: MtDataDao,
    private val buyGoodDao: BuyGoodDao,
    private val categoryListDao: CategoryListDao,
    private val personDao: PersonDao,
    private val planDao: PlanDao
) {

    fun getMtDataList(id: Int) = flow {
        emit(Resource.Loading)
        mtDataDao.getMtDataList(id).collect {
            emit(Resource.Success(it))
        }

    }

    fun getCategoryList() = flow {
        categoryListDao.getCategoryDataList().collect { categoryList ->
            emit(categoryList)
        }
    }

    suspend fun checkEndDateById(mtId: Int, tempDate: String): Boolean {
//        val transFormat = SimpleDateFormat("yyyy.MM.dd")

        val nowDate = mtDataDao.getMtDataById(mtId)

//        val mtEndData = transFormat.parse(nowDate.mtEnd)
//        val mtStartData = transFormat.parse(nowDate.mtStart)
//        val nowCheckData = transFormat.parse(tempDate)

//        return (nowCheckData in mtStartData..mtEndData)
        return true
    }

    suspend fun insertMtData(mtData: MtData): Long {
        return mtDataDao.insertMtData(mtData)
    }

    suspend fun updatePerson(personId: Int, simplePerson: SimplePerson) {
        personDao.updatePersonData(
            simplePerson.name,
            simplePerson.paymentFee.toInt(),
            simplePerson.phoneNumber,
            personId
        )
    }

    suspend fun insertPerson(person: Person) {
        personDao.insertPerson(person)
    }

    suspend fun clearPerson(mtId: Int) {
        personDao.clearPersons(mtId)
    }

    suspend fun deletePersonById(personId: Int) {
        personDao.deletePersonById(personId)
    }

    suspend fun insertBuyGood(buyGood: BuyGood) {
        buyGoodDao.insertBuyGood(buyGood)
    }

    suspend fun clearBuyGood(mtId: Int) {
        buyGoodDao.clearBuyGoods(mtId)
    }

    suspend fun deleteBuyGoodById(buyGoodId: Int) {
        buyGoodDao.deleteBuyGoodById(buyGoodId)
    }

    suspend fun insertPlan(plan: Plan) {
        planDao.insertPlan(plan)
    }

    suspend fun updatePlanImgSrcById(planId: Int, imgSrc: String = "") {
        planDao.updatePlanById(planId, imgSrc)
    }

    suspend fun updatePlanImgBytesById(planId: Int, img: ByteArray) {
        planDao.updateImgBytePlanById(planId, img)
    }

    suspend fun clearPlanImgById(planId: Int) {
        planDao.clearPlanImgById(planId)
    }

    suspend fun updatePlanById(planId: Int, day: String, title: String, text: String) {
        planDao.updatePlanDialogById(planId, day, title, text)
    }

    suspend fun deletePlanById(planId: Int) {
        planDao.deletePlanById(planId)
    }

    suspend fun insertCategory(categoryList: categoryList) {
        categoryListDao.insertCategory(categoryList)
    }

    suspend fun deleteCategoryById(categoryId: Int) {
        categoryListDao.deleteCategoryById(categoryId)
    }

    fun getMtTotalList(): Flow<List<MtData>> {
        return mtDataDao.getMtData()
    }

}