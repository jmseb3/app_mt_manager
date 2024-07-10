package com.wonddak.mtmanger.di


import com.wonddak.mtmanger.repository.MTRepository
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.dao.BuyGoodDao
import com.wonddak.mtmanger.room.dao.CategoryListDao
import com.wonddak.mtmanger.room.dao.MtDataDao
import com.wonddak.mtmanger.room.dao.PersonDao
import com.wonddak.mtmanger.room.dao.PlanDao
import com.wonddak.mtmanger.room.getRoomDatabase
import com.wonddak.mtmanger.util.BillingModule
import com.wonddak.mtmanger.util.DataStoreProvider
import com.wonddak.mtmanger.util.DeviceActionHelper
import com.wonddak.mtmanger.util.Storage
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val configModule = module {
    singleOf(::DataStoreProvider)
    single<Storage> {
        Storage(get())
    }
}

val dataBaseModule = module {
    single<AppDatabase> {
        getRoomDatabase()
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
    single<MTRepository> {
        MTRepository(get(), get(), get(), get(), get())
    }
}

val deviceModule = module {
    single<DeviceActionHelper> {
        DeviceActionHelper()
    }
    single<BillingModule> {
        BillingModule()
    }
}

val viewmodelModule = module {
    viewModelOf(::MTViewModel)
    viewModelOf(::PayViewModel)
}

fun sharedModule() = dataBaseModule + repositoryModule + viewmodelModule + configModule + deviceModule