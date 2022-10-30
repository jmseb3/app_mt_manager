package com.wonddak.mtmanger.di

import android.content.Context
import com.wonddak.mtmanger.BillingModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Billing {

    @Singleton
    @Provides
    fun provideBillingModule(
        @ApplicationContext context: Context
    ) : BillingModule = BillingModule(context)
}