package com.wonddak.mtmanger.di

import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewmodelModule = module {
    viewModelOf(::MTViewModel)
    viewModelOf(::PayViewModel)
}