package com.wonddak.mtmanger.di

import android.content.Context
import android.content.SharedPreferences
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.room.MtDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMtRepository(
        mtDataDao: MtDataDao
    ) : MTRepository = MTRepository(mtDataDao)
}