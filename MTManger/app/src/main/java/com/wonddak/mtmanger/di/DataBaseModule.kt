package com.wonddak.mtmanger.di

import android.content.Context
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(
        @ApplicationContext context: Context
    ) : AppDatabase {
        return  AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideMtDataDao(
        appDataBase: AppDatabase
    ) : MtDataDao = appDataBase.MtDataDao()
}