package com.wonddak.mtmanger.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.util.BillingModule
import kotlinx.coroutines.launch

class PayViewModel(
    private val billingModule: BillingModule
) : ViewModel() {

    var removeAdStatus by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            billingModule.removeAdStatus.collect {
                println("removeAdStatus : $it")
                this@PayViewModel.removeAdStatus = it
            }
        }
    }
}