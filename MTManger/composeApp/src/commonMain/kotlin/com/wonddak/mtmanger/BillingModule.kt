package com.wonddak.mtmanger

import kotlinx.coroutines.flow.MutableStateFlow


expect class  BillingModule {

}

var removeAddStatus: MutableStateFlow<Boolean> = MutableStateFlow(true)