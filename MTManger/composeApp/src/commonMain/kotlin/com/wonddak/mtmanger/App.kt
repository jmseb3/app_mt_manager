package com.wonddak.mtmanger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.ui.main.BottomNavigationBar
import com.wonddak.mtmanger.ui.main.NavGraph
import com.wonddak.mtmanger.ui.main.TopAppContent
import com.wonddak.mtmanger.ui.main.isMTList
import com.wonddak.mtmanger.ui.main.isSetting
import com.wonddak.mtmanger.ui.theme.AppTheme
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.AdvertView
import com.wonddak.mtmanger.ui.view.SplashView
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject

@Composable
internal fun App(
    mtViewModel : MTViewModel = koinInject()
) = AppTheme {
    val navController = rememberNavController()
    var showSplash by remember {
        mutableStateOf(true)
    }
    if (showSplash) {
        SplashView() {
            showSplash = false
        }
    } else {
        Scaffold(
            topBar = {
                TopAppContent(navController)
            },
            bottomBar = {
                AnimatedVisibility(
                    !navController.isSetting() && !navController.isMTList(),
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    BottomNavigationBar(navController = navController)
                }
            },
            containerColor = match1
        ) {
            Box(Modifier.padding(it)) {
                Column {
//                    val removeAd by mtViewModel.removeAdStatus.collectAsState(true)
//                    if (!removeAd) {
//                        AdvertView()
//                    }
                    NavGraph(
                        navController = navController,
                        mtViewModel,
                    )
                }
            }
        }
    }
}