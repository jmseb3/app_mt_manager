package com.wonddak.mtmanger.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.model.BottomNavItem
import com.wonddak.mtmanger.ui.view.BuyView
import com.wonddak.mtmanger.ui.view.MainView
import com.wonddak.mtmanger.ui.view.MtListView
import com.wonddak.mtmanger.ui.view.PersonView
import com.wonddak.mtmanger.ui.view.PlanView
import com.wonddak.mtmanger.ui.view.SettingView
import com.wonddak.mtmanger.ui.view.common.NoDataBase
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel,
    billingModule: BillingModule
) {

    NavHost(navController = navController, startDestination = Const.HOME) {
        homeGraph(navController, mtViewModel)

        composable(Const.SETTING) {
            SettingView(
                mtViewModel = mtViewModel,
                billingModule = billingModule
            ) {
                navController.popBackStack()
            }
        }
        composable(Const.MT_LIST) {
            MtListView(mtViewModel = mtViewModel) {
                navController.popBackStack()
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel
) {
    navigation(startDestination = BottomNavItem.Main.screenRoute, route = Const.HOME) {
        composable(BottomNavItem.Main.screenRoute) {
            NoDataBase(
                mtViewModel
            ) {
                MainView(mtViewModel = mtViewModel) {
                    navController.navigate(Const.MT_LIST) {
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(BottomNavItem.Person.screenRoute) {
            NoDataBase(mtViewModel) {
                PersonView(
                    mtViewModel = mtViewModel
                )
            }
        }
        composable(BottomNavItem.Buy.screenRoute) {
            NoDataBase(mtViewModel) {
                BuyView(
                    mtViewModel = mtViewModel
                )
            }
        }
        composable(BottomNavItem.Plan.screenRoute) {
            NoDataBase(mtViewModel) {
                PlanView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }
}

@Composable
fun NavController.isSetting(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.SETTING
}

@Composable
fun NavController.isMTList(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.MT_LIST
}