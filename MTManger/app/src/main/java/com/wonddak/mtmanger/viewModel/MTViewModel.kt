package com.wonddak.mtmanger.viewModel

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataList
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.room.categoryList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MTViewModel(
    private val mtRepository: MTRepository,
    private val pref: SharedPreferences,
    private val billing: BillingModule,
) : ViewModel() {
    val removeAdStatus
        get() = billing.removeAddStatus.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val mainMtId: StateFlow<Int>
        get() = _mainMtId
    private val _mainMtId = MutableStateFlow(0)

    val nowMtDataList: StateFlow<Resource<MtDataList>>
        get() = _nowMtDataList

    private var _nowMtDataList: MutableStateFlow<Resource<MtDataList>> =
        MutableStateFlow(Resource.Loading)


    val settingCategoryList: StateFlow<List<categoryList>>
        get() = _categoryList

    private var _categoryList: MutableStateFlow<List<categoryList>> = MutableStateFlow(emptyList())


    init {
        viewModelScope.launch {
            launch {
                mainMtId.collectLatest { it ->
                    mtRepository.getMtDataList(it).collectLatest { mtDataList ->
                        _nowMtDataList.value = mtDataList

                        if (mtDataList is Resource.Success) {
                            mtDataList.data?.let { mtData ->
                                Log.i("JWH", "MtData : ${mtData.mtdata}")
                                mtData.personList.forEach {
                                    Log.i("JWH", "personList : ${it}")

                                }
                                mtData.buyGoodList.forEach {
                                    Log.i("JWH", "BuyList : ${it}")

                                }
                                mtData.planList.forEach {
                                    Log.i("JWH", "PlanList : ${it}")

                                }
                            }
                        }
                    }
                }
            }
            launch {
                mtRepository.getCategoryList().collect {
                    _categoryList.value = it
                }
            }
            launch {
                mainMtId.collect {
                    Log.i("JWH", "id is $it")
                }
            }
        }
    }

    fun setMtId(id: Int) {
        _mainMtId.value = id
        val editor = pref.edit()
        editor.putInt("id", id)
        editor.apply()
    }


    fun insertMtData(mtData: MtData) {
        viewModelScope.launch {
            val newId = withContext(Dispatchers.IO) {
                mtRepository.insertMtData(mtData).toInt()
            }
            setMtId(newId)
        }
    }


    fun insertPerson(
        name: String,
        fee: Int,
        number: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPerson(
                    Person(
                        null,
                        mainMtId.value,
                        name,
                        number,
                        fee
                    )
                )
            }
        }
    }

    fun insertBuyGood(
        category: String,
        name: String,
        count: Int,
        price: Int,
        buyGoodId: Int? = null
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertBuyGood(
                    BuyGood(
                        buyGoodId,
                        mainMtId.value,
                        category,
                        name,
                        count,
                        price
                    )
                )
            }
        }
    }

    suspend fun updateMtData(
        title: String,
        fee: Int,
        start: String,
        end: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertMtData(
                    MtData(
                        mainMtId.value ?: 0,
                        title,
                        fee,
                        start,
                        end
                    )
                )
            }
        }

    }

    fun getMtTotalLIst(): Flow<List<MtData>> {
        return mtRepository.getMtTotalList()
    }

    fun deletePerson(personId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.deletePersonById(personId)
            }
        }
    }

    fun deleteBuyGood(buyGoodId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.deleteBuyGoodById(buyGoodId)
            }
        }
    }

    fun clearPersonData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.clearPerson(mainMtId.value)
            }
        }
    }

    fun clearBuyGoodData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.clearBuyGood(mainMtId.value)
            }
        }
    }

    fun addPlan(title: String, day: String, text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPlan(
                    Plan(
                        null,
                        mainMtId.value,
                        nowDay = day,
                        nowPlanTitle = title,
                        simpleText = text
                    )
                )
            }
        }
    }

    fun updatePlanImgSrc(planId: Int, imgSrc: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.updatePlanImgSrcById(planId, imgSrc)
            }
        }
    }

    fun updatePlanImgByte(planId: Int, imgSrc: ByteArray) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.updatePlanImgBytesById(planId, imgSrc)
            }
        }
    }

    fun clearPlanImgSrc(planId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.clearPlanImgById(planId)
            }
        }
    }

    fun updatePlanById(planId: Int, day: String, title: String, text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.updatePlanById(planId, day, title, text)
            }
        }
    }

    fun deletePlanById(planId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.deletePlanById(planId)
            }
        }
    }

    fun insertCategory(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertCategory(categoryList(null, name))
            }
        }
    }

    fun updateCategory(categoryId: Int, name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertCategory(categoryList(categoryId, name))
            }
        }
    }

    fun deleteCategoryById(categoryId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.deleteCategoryById(categoryId)
            }
        }
    }

    fun startPay(activity: Activity) {
        billing.purchaseRemoveAdRequest(activity)
    }

}