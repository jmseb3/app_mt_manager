package com.wonddak.mtmanger.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataAndPerson
import com.wonddak.mtmanger.room.Person

class MTViewModel(private val repository : MTRepository):ViewModel() {
    val mainMtId : LiveData<Int>
        get() = _mainMtId
    private val _mainMtId = MutableLiveData<Int>()

    val mtDataList : LiveData<List<MtDataAndPerson>>
        get() = _mtDataList
    private val _mtDataList = MutableLiveData<List<MtDataAndPerson>>()

    fun getMtDataById() : MtData{
        return repository.getMtDataById(mainMtId.value?:0)
    }

    fun getPerson() : LiveData<List<Person>> {
        return  repository.getPerson(mainMtId.value?:0)
    }

    fun getMtId(){
        _mainMtId.value = repository.getMtId()
    }

}