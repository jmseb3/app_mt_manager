package com.wonddak.mtmanger.di

import android.content.Context
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
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
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideMtDataDao(
        appDataBase: AppDatabase
    ): MtDataDao = appDataBase.MtDataDao()

    @Singleton
    @Provides
    fun provideBuyGoodDao(
        appDataBase: AppDatabase
    ): BuyGoodDao = appDataBase.BuyGoodDao()

    @Singleton
    @Provides
    fun provideCategoryListDao(
        appDataBase: AppDatabase
    ): CategoryListDao = appDataBase.CategoryListDao()

    @Singleton
    @Provides
    fun providePersonDao(
        appDataBase: AppDatabase
    ): PersonDao = appDataBase.PersonDao()

    @Singleton
    @Provides
    fun providePlanDao(
        appDataBase: AppDatabase
    ): PlanDao = appDataBase.PlanDao()


}