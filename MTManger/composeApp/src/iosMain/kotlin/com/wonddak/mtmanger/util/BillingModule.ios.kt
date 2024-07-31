package com.wonddak.mtmanger.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class BillingModule {
    val _removeAdStatus = MutableStateFlow(false)
    actual val removeAdStatus: StateFlow<Boolean>
        get() = _removeAdStatus
}