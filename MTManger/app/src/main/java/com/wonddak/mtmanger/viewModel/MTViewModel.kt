package com.wonddak.mtmanger.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataAndPerson
import com.wonddak.mtmanger.room.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MTViewModel @Inject constructor(
    private val mtRepository: MTRepository
) : ViewModel() {
    private var _bottomMenuStatus = MutableStateFlow<Boolean>(true)
    val bottomMenuStatus :StateFlow<Boolean> = _bottomMenuStatus

    val mainMtId: LiveData<Int>
        get() = _mainMtId
    private val _mainMtId = MutableLiveData<Int>()

    val mtDataList: LiveData<List<MtDataAndPerson>>
        get() = _mtDataList
    private val _mtDataList = MutableLiveData<List<MtDataAndPerson>>()

    fun getPerson(): LiveData<List<Person>> {
        return mtRepository.getPerson(mainMtId.value ?: 0)
    }

    fun setMtId(id: Int) {
        _mainMtId.value = id
    }

    suspend fun checkEndDateById(tempDate: String): Boolean {
        return mtRepository.checkEndDateById(mainMtId.value ?: 0, tempDate)
    }

    suspend fun insertMtData(mtData: MtData): Long {
        return mtRepository.insertMtData(mtData)
    }

    suspend fun updateMtData(
        title: String,
        fee: Int,
        start: String,
        end: String
    ) {
        mtRepository.insertMtData(MtData(
            mainMtId.value ?:0,
            title,
            fee,
            start,
            end
        ))
    }

    suspend fun getMtDataById(): MtData {
        return mtRepository.getMtDataById(mainMtId.value ?: 0)
    }

    fun getMtTotalLIst() : LiveData<List<MtData>>{
        return mtRepository.getMtTotalList()
    }

    fun setBottomMenuStatus(show:Boolean){
        _bottomMenuStatus.value = show
    }
}