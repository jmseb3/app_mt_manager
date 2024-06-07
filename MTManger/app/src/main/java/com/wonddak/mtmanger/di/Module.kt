package com.wonddak.mtmanger.di


import android.content.SharedPreferences
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import org.koin.android.ext.koin.androidApplication
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
val dataBaseModule = module {
    single<AppDatabase> {
        AppDatabase.getInstance(androidApplication())
    }
    single<MtDataDao> {
        val database = get<AppDatabase>()
        database.MtDataDao()
    }
    single<BuyGoodDao> {
        val database = get<AppDatabase>()
        database.BuyGoodDao()
    }
    single<CategoryListDao> {
        val database = get<AppDatabase>()
        database.CategoryListDao()
    }
    single<PersonDao> {
        val database = get<AppDatabase>()
        database.PersonDao()
    }
    single<PlanDao> {
        val database = get<AppDatabase>()
        database.PlanDao()
    }
}

val repositoryModule = module {
    singleOf(::MTRepository)
}