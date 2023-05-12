package com.wonddak.mtmanger.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataList
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.room.categoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MTViewModel @Inject constructor(
    private val mtRepository: MTRepository,
    private val pref: SharedPreferences
) : ViewModel() {
    private var _bottomMenuStatus = MutableStateFlow<Boolean>(false)
    val bottomMenuStatus: StateFlow<Boolean> = _bottomMenuStatus

    private var _topBackButtonStatus = MutableStateFlow<Boolean>(true)
    val topBackButtonStatus: StateFlow<Boolean> = _topBackButtonStatus


    private var _removeAdStatus = MutableStateFlow<Boolean>(false)
    val removeAdStatus: StateFlow<Boolean> = _removeAdStatus

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


    private var _personFoldStatus = MutableStateFlow<Boolean>(false)
    val personFoldStatus: StateFlow<Boolean> = _personFoldStatus

    private var _categoryFoldStatus = MutableStateFlow<Boolean>(true)
    val categoryFoldStatus: StateFlow<Boolean> = _categoryFoldStatus

    fun togglePersonFoldStatus() {
        _personFoldStatus.value = !_personFoldStatus.value
    }

    fun toggleCategoryFoldStatus() {
        _categoryFoldStatus.value = !_categoryFoldStatus.value
    }

    init {
        viewModelScope.launch {
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

        viewModelScope.launch {
            mtRepository.getCategoryList().collect {
                _categoryList.value = it
            }
        }
    }

    fun setMtId(id: Int) {
        _mainMtId.value = id
        val editor = pref.edit()
        editor.putInt("id", id)
        editor.apply()
    }

    suspend fun checkEndDateById(tempDate: String): Boolean {
        return mtRepository.checkEndDateById(mainMtId.value ?: 0, tempDate)
    }

    suspend fun insertMtData(mtData: MtData): Long {
        return mtRepository.insertMtData(mtData)
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

    fun getMtTotalLIst(): LiveData<List<MtData>> {
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
    fun addPlan(title: String,day: String,text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPlan(
                    Plan(
                        null,
                        mainMtId.value,
                        nowday =  day,
                        nowplantitle = title,
                        simpletext = text
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

    fun setBottomMenuStatus(show: Boolean) {
        _bottomMenuStatus.value = show
    }

    fun toggleBottomMenuStatus() {
        _bottomMenuStatus.value = !_bottomMenuStatus.value
    }

    fun setTopButtonStatus(show: Boolean) {
        _topBackButtonStatus.value = show
    }

    fun setRemoveAddStatus(show: Boolean) {
        _removeAdStatus.value = show
    }
}