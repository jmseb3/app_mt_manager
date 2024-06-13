package com.wonddak.mtmanger.util

import kotlinx.coroutines.flow.MutableStateFlow


expect class  BillingModule {

}

var removeAddStatus: MutableStateFlow<Boolean> = MutableStateFlow(true)