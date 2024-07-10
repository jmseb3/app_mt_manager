package com.wonddak.mtmanger.util

import kotlinx.coroutines.flow.StateFlow


expect class  BillingModule() {
    val removeAdStatus : StateFlow<Boolean>
}
