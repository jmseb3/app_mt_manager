package com.wonddak.mtmanger

import android.app.Application
import com.wonddak.mtmanger.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MtManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MtManagerApplication)
            modules(sharedModule())
        }
    }
}
