package com.wonddak.mtmanger.viewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MTViewModel @Inject constructor(
    private val mtRepository: MTRepository,
    private val pref: SharedPreferences
) : ViewModel() {
    private var _bottomMenuStatus = MutableStateFlow<Boolean>(true)
    val bottomMenuStatus: StateFlow<Boolean> = _bottomMenuStatus

    private var _topBackButtonStatus = MutableStateFlow<Boolean>(true)
    val topBackButtonStatus: StateFlow<Boolean> = _topBackButtonStatus


    val mainMtId: StateFlow<Int>
        get() = _mainMtId
    private val _mainMtId = MutableStateFlow(0)

    val nowMtDataList: StateFlow<Resource<MtDataList>>
        get() = _nowMtDataList

    private var _nowMtDataList: MutableStateFlow<Resource<MtDataList>> =
        MutableStateFlow(Resource.Loading)


    val categoryList: StateFlow<List<categoryList>>
        get() = _categoryList

    private var _categoryList: MutableStateFlow<List<categoryList>> = MutableStateFlow(emptyList())


    private var _personFoldStatus = MutableStateFlow<Boolean>(false)
    val personFoldStatus: StateFlow<Boolean> = _personFoldStatus

    private var _buyGoodFoldStatus = MutableStateFlow<Boolean>(false)
    val buyGoodFoldStatus: StateFlow<Boolean> = _buyGoodFoldStatus


    fun togglePersonFoldStatus() {
        _personFoldStatus.value = !_personFoldStatus.value
    }

    fun toggleBuyGoodFoldStatus() {
        _buyGoodFoldStatus.value = !_buyGoodFoldStatus.value
    }

    init {
        viewModelScope.launch {
            mainMtId.collectLatest { it ->
                mtRepository.getMtDataList(it).collectLatest { mtDataList ->
                    _nowMtDataList.value = mtDataList

                    if (mtDataList is Resource.Success) {
                        mtDataList.data?.let { mtData ->
                            Log.i("JWH","MtData : ${mtData.mtdata}")
                            mtData.personList.forEach {
                                Log.i("JWH","personList : ${it}")

                            }
                            mtData.buyGoodList.forEach {
                                Log.i("JWH","BuyList : ${it}")

                            }
                            mtData.planList.forEach {
                                Log.i("JWH","PlanList : ${it}")

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

    fun addEmptyPlan() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPlan(Plan(
                    null,
                    mainMtId.value
                ))
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

    fun setBottomMenuStatus(show: Boolean) {
        _bottomMenuStatus.value = show
    }

    fun toggleBottomMenuStatus() {
        _bottomMenuStatus.value = !_bottomMenuStatus.value
    }

    fun setTopButtonStatus(show: Boolean) {
        _topBackButtonStatus.value = show
    }
}