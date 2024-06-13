package com.wonddak.mtmanger

import android.app.Application
import com.wonddak.mtmanger.di.billingModule
import com.wonddak.mtmanger.di.configModule
import com.wonddak.mtmanger.di.dataBaseModule
import com.wonddak.mtmanger.di.repositoryModule
import com.wonddak.mtmanger.di.sharedModule
import com.wonddak.mtmanger.di.startKoinCommon
import com.wonddak.mtmanger.di.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MtManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoinCommon {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MtManagerApplication)
            // Load modules
            modules(billingModule  + sharedModule())
        }
    }
}