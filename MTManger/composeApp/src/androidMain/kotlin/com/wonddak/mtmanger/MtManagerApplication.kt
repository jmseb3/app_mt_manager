package com.wonddak.mtmanger

import android.app.Application
import com.wonddak.mtmanger.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MtManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MtManagerApplication)
            // Load modules
            modules(sharedModule())
        }
    }
}