package com.wonddak.mtmanger.di

import org.koin.core.context.startKoin
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.dsl.KoinAppDeclaration

fun sharedModule() = dataBaseModule + repositoryModule + viewmodelModule + configModule