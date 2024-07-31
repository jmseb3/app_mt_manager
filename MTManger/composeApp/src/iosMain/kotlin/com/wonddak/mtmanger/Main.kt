package com.wonddak.mtmanger

import androidx.compose.ui.window.ComposeUIViewController
import com.wonddak.mtmanger.di.sharedModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin {
        modules(sharedModule())
    }
    return ComposeUIViewController { App() }
}
