package com.wonddak.mtmanger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.ui.main.BottomNavigationBar
import com.wonddak.mtmanger.ui.main.NavGraph
import com.wonddak.mtmanger.ui.main.TopAppContent
import com.wonddak.mtmanger.ui.main.isATT
import com.wonddak.mtmanger.ui.main.showAd
import com.wonddak.mtmanger.ui.main.showBottomNavigation
import com.wonddak.mtmanger.ui.theme.AppTheme
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.AdvertView
import com.wonddak.mtmanger.ui.view.SplashView
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
internal fun App() {
    KoinContext() {
        AppTheme {
            HomeScreen()
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen() {
    val mtViewModel: MTViewModel = koinViewModel()
    val payViewModel: PayViewModel = koinViewModel()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mtViewModel.snackBarMsg) {
        mtViewModel.snackBarMsg?.let { snackBarMsg ->
            snackbarHostState.currentSnackbarData?.dismiss()
            val snackbarResult = snackbarHostState.showSnackbar(
                message = snackBarMsg.msg,
                actionLabel = snackBarMsg.label,
                withDismissAction = false,
                duration = SnackbarDuration.Short
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    mtViewModel.closeSnackBar()
                }

                SnackbarResult.ActionPerformed -> {
                    snackBarMsg.action.invoke()
                    mtViewModel.closeSnackBar()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data
                )
            }
        },
        topBar = {
            if (!navController.isATT()) {
                TopAppContent(navController)
            }
        },
        bottomBar = {
            if (!navController.isATT()) {
                AnimatedVisibility(
                    navController.showBottomNavigation(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    BottomNavigationBar(navController = navController)
                }
            }
        },
        containerColor = match1,
        contentWindowInsets = WindowInsets(0)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .navigationBarsPadding()
        ) {
            Column {
                if (navController.showAd() && !payViewModel.removeAdStatus) {
                    AdvertView(Modifier.wrapContentSize().defaultMinSize(minHeight = 50.dp))
                }
                NavGraph(
                    navController = navController,
                    mtViewModel,
                )
            }
        }
    }
    if (mtViewModel.showSplash) {
        SplashView()
    }
}