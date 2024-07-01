package com.wonddak.mtmanger.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.util.removeAddStatus
import kotlinx.coroutines.launch

class PayViewModel() : ViewModel() {

    var removeAdStatus by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            removeAddStatus.collect {
                println("removeAdStatus : $it")
                this@PayViewModel.removeAdStatus = it
            }
        }
    }
}