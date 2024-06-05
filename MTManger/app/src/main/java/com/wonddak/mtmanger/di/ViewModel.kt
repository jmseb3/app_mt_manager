package com.wonddak.mtmanger.di

import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module{
    viewModelOf(::MTViewModel)
}