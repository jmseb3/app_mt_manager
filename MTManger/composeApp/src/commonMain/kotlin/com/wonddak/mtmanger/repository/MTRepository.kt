package com.wonddak.mtmanger.repository

import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import com.wonddak.mtmanger.room.entity.BuyGood
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.Person
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.room.entity.SimplePerson
import com.wonddak.mtmanger.room.entity.categoryList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MTRepository(
    private val mtDataDao: MtDataDao,
    private val buyGoodDao: BuyGoodDao,
    private val categoryListDao: CategoryListDao,
    private val personDao: PersonDao,
    private val planDao: PlanDao
) {

    fun getMtDataList(id: Int) = flow {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            emit(Resource.Success(ScreenshotMockData.mtDataList))
            return@flow
        }
        emit(Resource.Loading)
        mtDataDao.getMtDataList(id).collect {
            emit(Resource.Success(it))
        }
    }

    fun getCategoryList() : Flow<List<categoryList>> = flow {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            emit(ScreenshotMockData.categoryList)
            return@flow
        }
        categoryListDao.getCategoryDataList().collect { categoryList ->
            if(categoryList.isEmpty()) {
                val default = listOf(
                    "식재료",
                    "주류",
                    "차비",
                    "식사",
                    "마트"
                ).map { categoryList(null, it) }
                categoryListDao.insertCategoryList(default)
            } else {
                emit(categoryList)
            }
        }
    }
    suspend fun checkMtDataOver2() :Boolean {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return ScreenshotMockData.mtTotalList.size >= 2
        }
        return mtDataDao.getMtDataData().size >=2
    }
    suspend fun insertMtData(mtData: MtData): Long {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return Const.SCREENSHOT_MOCK_MT_ID.toLong()
        }
        return mtDataDao.insertMtData(mtData)
    }

    suspend fun updateMtData(mtData: MtData) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        mtDataDao.updateMtData(mtData)
    }
    suspend fun deleteMtData(mtData: MtData) : Int {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return Const.SCREENSHOT_MOCK_MT_ID
        }
        val preList = mtDataDao.getMtDataData().toList()
        var findIndex  = -1
        for (idx in preList.indices){
            val item = preList[idx]
            if (item == mtData) {
                findIndex = idx
                break
            }
        }
        mtDataDao.deleteMtData(mtData)
        return if (findIndex == -1) {
            -1
        } else {
            if (preList.size <= 1) {
                -1
            } else {
                preList[(findIndex-1).takeIf { it >=0 }  ?: 1 ].mtDataId!!
            }
        }
    }

    suspend fun updatePerson(personId: Int, simplePerson: SimplePerson) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        personDao.updatePersonData(
            simplePerson.name,
            simplePerson.paymentFee.toInt(),
            simplePerson.phoneNumber,
            personId
        )
    }

    suspend fun insertPerson(person: Person) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        personDao.insertPerson(person)
    }

    suspend fun clearPerson(mtId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        personDao.clearPersons(mtId)
    }

    suspend fun deletePersonById(personId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        personDao.deletePersonById(personId)
    }

    suspend fun insertBuyGood(buyGood: BuyGood) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        buyGoodDao.insertBuyGood(buyGood)
    }

    suspend fun updateBuyGood(buyGood: BuyGood) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        buyGoodDao.updateBuyGood(buyGood)
    }

    suspend fun clearBuyGood(mtId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        buyGoodDao.clearBuyGoods(mtId)
    }

    suspend fun deleteBuyGoodById(buyGoodId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        buyGoodDao.deleteBuyGoodById(buyGoodId)
    }

    suspend fun insertPlan(plan: Plan) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        planDao.insertPlan(plan)
    }

    suspend fun updatePlanImgBytesById(planId: Int, img: ByteArray) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        planDao.updateImgBytePlanById(planId, img)
    }

    suspend fun clearPlanImgById(planId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        planDao.clearPlanImgById(planId)
    }

    suspend fun updatePlan(plan: Plan) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        planDao.updatePlan(plan)
    }

    suspend fun deletePlanById(planId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        planDao.deletePlanById(planId)
    }

    suspend fun insertCategory(categoryList: categoryList) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        categoryListDao.insertCategory(categoryList)
    }

    suspend fun deleteCategoryById(categoryId: Int) {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return
        }
        categoryListDao.deleteCategoryById(categoryId)
    }

    fun getMtTotalList(): Flow<List<MtData>> {
        if (Const.USE_SCREENSHOT_MOCK_DATA) {
            return flowOf(ScreenshotMockData.mtTotalList)
        }
        return mtDataDao.getMtData()
    }

}
