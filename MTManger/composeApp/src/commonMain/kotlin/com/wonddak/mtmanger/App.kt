package com.wonddak.mtmanger

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.wonddak.mtmanger.model.NavItem
import com.wonddak.mtmanger.ui.main.NavGraph
import com.wonddak.mtmanger.ui.main.showAd
import com.wonddak.mtmanger.ui.theme.AppTheme
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.AdvertView
import com.wonddak.mtmanger.ui.view.SplashView
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun App() {
    KoinContext() {
        AppTheme {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    val mtViewModel: MTViewModel = koinViewModel()
    val payViewModel: PayViewModel = koinViewModel()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val adaptiveInfo = currentWindowAdaptiveInfo()

    val isCompact = with(adaptiveInfo) {
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    }
    val customNavSuiteType = if (isCompact) {
        NavigationSuiteType.NavigationBar
    } else {
        NavigationSuiteType.NavigationRail
    }

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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = match2,
            selectedIconColor = match1,
            unselectedIconColor = Color.White
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = match2,
            selectedIconColor = match1,
            unselectedIconColor = Color.White
        )
    )
    NavigationSuiteScaffold(
        modifier = Modifier.safeDrawingPadding(),
        navigationSuiteItems = {
            arrayListOf(
                NavItem.Main,
                NavItem.Person,
                NavItem.Buy,
                NavItem.Plan,
                NavItem.Setting
            ).forEach { navItem ->
                val selected = currentRoute == navItem.screenRoute
                item(
                    icon = {
                        Icon(
                            painterResource(navItem.icon),
                            contentDescription = navItem.title
                        )
                    },
                    label = {
                        Text(
                            text = navItem.title,
                            color = if (selected) match1 else Color.White
                        )
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(navItem.screenRoute) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = myNavigationSuiteItemColors
                )
            }
        },
        layoutType = customNavSuiteType,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = match2,
            navigationRailContainerColor = match2
        )
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data
                    )
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
    }
    if (mtViewModel.showSplash) {
        SplashView()
    }
}