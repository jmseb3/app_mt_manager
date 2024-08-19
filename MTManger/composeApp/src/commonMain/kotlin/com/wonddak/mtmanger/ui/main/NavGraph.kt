package com.wonddak.mtmanger.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.model.BottomNavItem
import com.wonddak.mtmanger.ui.view.ADTrackingView
import com.wonddak.mtmanger.ui.view.common.NoDataBase
import com.wonddak.mtmanger.ui.view.home.buy.BuyView
import com.wonddak.mtmanger.ui.view.home.main.AdjustmentView
import com.wonddak.mtmanger.ui.view.home.main.MainView
import com.wonddak.mtmanger.ui.view.home.main.MtListView
import com.wonddak.mtmanger.ui.view.home.person.PersonView
import com.wonddak.mtmanger.ui.view.home.plan.PlanAddView
import com.wonddak.mtmanger.ui.view.home.plan.PlanView
import com.wonddak.mtmanger.ui.view.setting.CategoryView
import com.wonddak.mtmanger.ui.view.setting.SettingView
import com.wonddak.mtmanger.ui.view.useADT
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel,
) {
    if (useADT) {
        LaunchedEffect(true) {
            mtViewModel.isFirst {
                navController.navigate(Const.ATT)
            }
        }
    }
    NavHost(navController = navController, startDestination = Const.HOME) {
        homeGraph(navController, mtViewModel)

        settingGraph(navController, mtViewModel)

        composable(Const.MT_LIST) {
            MtListView { data ->
                if (navController.popBackStack()) {
                    mtViewModel.showSnackBarMsg("${data.mtTitle}로 변경했어요.")
                    mtViewModel.setMtId(data.mtDataId!!)
                }
            }
        }
        composable(Const.MT_ADJUSTMENT) {
            AdjustmentView(mtViewModel)
        }
        composable(Const.ATT) {
            ADTrackingView {
                mtViewModel.clearFirst()
                navController.popBackStack()
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel,
) {
    navigation(startDestination = BottomNavItem.Main.screenRoute, route = Const.HOME) {
        composable(BottomNavItem.Main.screenRoute) {
            NoDataBase(
                mtViewModel
            ) {
                MainView(
                    mtViewModel = mtViewModel,
                    showMTList = {
                        navController.navigate(Const.MT_LIST) {
                            launchSingleTop = true
                        }
                    },
                    showAdjustment = {
                        navController.navigate(Const.MT_ADJUSTMENT) {
                            launchSingleTop = true
                        }
                    }
                )
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
                PlanView() { start,end ->
                    navController.navigate(Const.NEW_PLAN + "?start=$start&end=$end")
                }
            }
        }
        composable(
            Const.NEW_PLAN + "?start={start}&end={end}",
            arguments = listOf(
                navArgument("start") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("end") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {backStackEntry ->
            PlanAddView(
                backStackEntry.arguments?.getString("start")!!,
                backStackEntry.arguments?.getString("end")!!,
            )
        }
    }
}

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel,
) {
    navigation(startDestination = Const.SETTING_HOME, route = Const.SETTING) {
        composable(Const.SETTING_HOME) {
            SettingView() {
                navController.navigate(Const.CATEGORY)
            }
        }
        composable(Const.CATEGORY) {
            CategoryView(
                Modifier
                    .fillMaxSize(),
                mtViewModel.settingCategoryList,
                update = { id, input ->
                    mtViewModel.updateCategory(id, input)
                },
                delete = {
                    mtViewModel.deleteCategoryById(it)
                },
                insert = {
                    mtViewModel.insertCategory(it)
                }
            )
        }
    }
}

@Composable
fun NavController.isATT(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.ATT
}

@Composable
fun NavController.isSetting(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.CATEGORY || currentRoute == Const.SETTING_HOME
}

@Composable
fun NavController.isMTList(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.MT_LIST
}

@Composable
fun NavController.isAdjustment(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute == Const.MT_ADJUSTMENT
}

@Composable
fun NavController.isPlanNew(): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute?.startsWith(Const.NEW_PLAN) ?: false
}

@Composable
fun NavController.showSettingIcon(): Boolean {
    return !isSetting() && !isMTList() && !isAdjustment() && !isPlanNew()
}

@Composable
fun NavController.showNavigationIcon(): Boolean {
    return isSetting() || isMTList() || isAdjustment() || isPlanNew()
}

@Composable
fun NavController.showBottomNavigation(): Boolean {
    return !isSetting() && !isMTList() && !isAdjustment() && !isPlanNew()

}

@Composable
fun NavController.getTitle(): String {
    return if (this.isSetting()) {
        "설정"
    } else if (this.isMTList()) {
        "MT 리스트"
    } else if (this.isAdjustment()) {
        "정산하기"
    } else {
        "MT 매니저"
    }
}