package com.wonddak.mtmanger.di

import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMtRepository(
        mtDataDao: MtDataDao,
        buyGoodDao: BuyGoodDao,
        categoryListDao: CategoryListDao,
        personDao: PersonDao,
        planDao: PlanDao,
    ): MTRepository = MTRepository(mtDataDao, buyGoodDao, categoryListDao, personDao, planDao)
}