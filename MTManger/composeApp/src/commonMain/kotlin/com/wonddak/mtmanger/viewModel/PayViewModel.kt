package com.wonddak.mtmanger.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wonddak.mtmanger.removeAddStatus
import kotlinx.coroutines.launch

class PayViewModel() : ViewModel() {

    var removeAdStatus by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            removeAddStatus.collect {
                this@PayViewModel.removeAdStatus = it
            }
        }
    }
}