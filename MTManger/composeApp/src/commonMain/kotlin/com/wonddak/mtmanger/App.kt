package com.wonddak.mtmanger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.di.dataBaseModule
import com.wonddak.mtmanger.di.repositoryModule
import com.wonddak.mtmanger.di.viewmodelModule
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
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
internal fun App() {
    KoinApplication(application = {
        modules(dataBaseModule + repositoryModule + viewmodelModule)
    }) {
        AppTheme {
            val mtViewModel: MTViewModel = koinInject()
            val payViewModel: PayViewModel = koinInject()
            val navController = rememberNavController()
            var showSplash by remember {
                mutableStateOf(true)
            }
            if (showSplash) {
                SplashView() {
                    showSplash = false
                }
            } else {
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(mtViewModel.snackBarMsg) {
                    mtViewModel.snackBarMsg?.let { snackBarMsg ->
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = snackBarMsg.msg,
                            actionLabel = snackBarMsg.label,
                            withDismissAction = false,
                            duration = SnackbarDuration.Short
                        )
                        when (snackbarResult) {
                            SnackbarResult.Dismissed -> {
                                mtViewModel.snackBarMsg = null
                            }

                            SnackbarResult.ActionPerformed -> {
                                snackBarMsg.action.invoke()
                                mtViewModel.snackBarMsg = null
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
                            if (!payViewModel.removeAdStatus) {
                                AdvertView()
                            }
                            NavGraph(
                                navController = navController,
                                mtViewModel,
                            )
                        }
                    }
                }
            }

        }
    }
}
