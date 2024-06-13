package com.wonddak.mtmanger.di

import com.wonddak.mtmanger.util.BillingModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val billingModule = module {
    singleOf(::BillingModule)
}