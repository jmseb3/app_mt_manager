package com.wonddak.mtmanger.di

import android.content.Context
import android.content.SharedPreferences
import com.wonddak.mtmanger.core.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {

    @Singleton
    @Provides
    fun provideConfig(
        @ApplicationContext context: Context
    ) : SharedPreferences = context.getSharedPreferences(Const.pref.name, 0)
}