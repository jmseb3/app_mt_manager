package com.wonddak.mtmanger.viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.model.SnackBarMsg
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.entity.BuyGood
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.room.entity.Person
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.room.entity.PlanData
import com.wonddak.mtmanger.room.entity.SimpleBuyGood
import com.wonddak.mtmanger.room.entity.SimplePerson
import com.wonddak.mtmanger.room.entity.categoryList
import com.wonddak.mtmanger.util.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MTViewModel(
    private val mtRepository: MTRepository,
    private val storage: Storage,
) : ViewModel() {

    var snackBarMsg: SnackBarMsg? by mutableStateOf(null)
        private set

    fun showSnackBarMsg(msg: String) {
        snackBarMsg = SnackBarMsg(msg, "확인") { closeSnackBar() }
    }

    fun closeSnackBar() {
        snackBarMsg = null
    }

    private var initId by mutableStateOf(false)
    private var timeFinish by mutableStateOf(false)
    val showSplash by derivedStateOf { !initId || !timeFinish }

    var mainMtId by mutableStateOf(0)

    val totalMtList :StateFlow<List<MtData>> =
        mtRepository.getMtTotalList().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    val nowMtDataList: StateFlow<Resource<MtDataList>>
        get() = _nowMtDataList

    private var _nowMtDataList: MutableStateFlow<Resource<MtDataList>> =
        MutableStateFlow(Resource.Loading)


    var settingCategoryList by mutableStateOf(emptyList<categoryList>())

    private var observeJob: Job? = null

    init {
        viewModelScope.launch {
            launch {
                storage.id.collectLatest {
                    initId = true
                    mainMtId = it
                    observeJob?.cancel()
                    observeJob = launch {
                        mtRepository.getMtDataList(it).collect { mtDataList ->
                            _nowMtDataList.value = mtDataList
                        }
                    }
                }
            }
            launch {
                mtRepository.getCategoryList().collect {
                    settingCategoryList = it
                }
            }
            launch {
                delay(2000)
                timeFinish = true
            }
        }
    }

    fun isFirst(onFirst: () -> Unit) {
        viewModelScope.launch {
            if (storage.isFirst.first()) {
                onFirst()
            }
        }
    }

    fun clearFirst() {
        viewModelScope.launch {
            storage.clearFirst()
        }
    }

    fun setMtId(id: Int) {
        viewModelScope.launch {
            println("setMt Id :$id")
            storage.updateId(id)
        }
    }

    fun insertMtData(mtData: MtData) {
        viewModelScope.launch {
            val newId = withContext(Dispatchers.IO) {
                mtRepository.insertMtData(mtData).toInt()
            }
            setMtId(newId)
        }
    }

    fun deleteMtData(mtData: MtData) {
        viewModelScope.launch {
            mtRepository.deleteMtData(mtData).also {
                setMtId(it)
            }
        }
    }


    fun insertPerson(
        simplePerson: SimplePerson,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPerson(
                    Person(
                        null,
                        mainMtId,
                        simplePerson.name,
                        simplePerson.phoneNumber,
                        simplePerson.paymentFee.toInt()
                    )
                )
                showSnackBarMsg("${simplePerson.name}님을 추가했습니다.")
            }
        }
    }

    fun updatePerson(
        personId: Int,
        simplePerson: SimplePerson,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.updatePerson(
                    personId,
                    simplePerson
                )
                showSnackBarMsg("정보를 수정했습니다.")
            }
        }
    }

    fun insertBuyGood(
        simpleBuyGood: SimpleBuyGood,
        buyGoodId: Int? = null,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertBuyGood(
                    BuyGood(
                        buyGoodId,
                        mainMtId,
                        simpleBuyGood.category,
                        simpleBuyGood.name,
                        simpleBuyGood.count.toInt(),
                        simpleBuyGood.price.toInt()
                    )
                )
            }
        }
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
                mtRepository.clearPerson(mainMtId)
            }
        }
    }

    fun clearBuyGoodData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.clearBuyGood(mainMtId)
            }
        }
    }

    fun getPlanById(planId:Int?) :Plan? {
        if (planId == null) {
            return  null
        }
        val resource = nowMtDataList.value
        return if (resource is Resource.Success<MtDataList>) {
            resource.data?.planList?.find { it.planId == planId }
        } else {
            null
        }
    }

    fun addPlan(title: String, day: String, text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPlan(
                    Plan(
                        null,
                        mainMtId,
                        nowDay = day,
                        nowPlanTitle = title,
                        simpleText = text
                    )
                )
            }
        }
    }

    fun addPlan(planData: PlanData, onFinish: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mtRepository.insertPlan(
                    Plan(
                        null,
                        mainMtId,
                        nowDay = planData.nowDay,
                        nowPlanTitle = planData.nowPlanTitle,
                        simpleText = planData.simpleText,
                        imgBytes = planData.imgBytes
                    )
                )
            }
            onFinish()
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
            if (settingCategoryList.size > 2) {
                withContext(Dispatchers.IO) {
                    mtRepository.deleteCategoryById(categoryId)
                }
            } else {
                snackBarMsg = SnackBarMsg("적어도 하나는 존재해야 합니다.")
            }
        }
    }

}