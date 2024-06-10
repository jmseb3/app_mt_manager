package com.wonddak.mtmanger.di

import android.content.SharedPreferences
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.core.Const
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val billingModule = module {
    singleOf(::BillingModule)
}

val configModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(Const.pref.name, 0)
    }
}