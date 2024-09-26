package com.wonddak.mtmanger.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.wonddak.mtmanger.model.NavItem
import com.wonddak.mtmanger.ui.view.ADTrackingView
import com.wonddak.mtmanger.ui.view.common.InsertDataView
import com.wonddak.mtmanger.ui.view.home.buy.BuyView
import com.wonddak.mtmanger.ui.view.home.main.AdjustmentView
import com.wonddak.mtmanger.ui.view.home.main.MainView
import com.wonddak.mtmanger.ui.view.home.main.MtListView
import com.wonddak.mtmanger.ui.view.home.person.PersonView
import com.wonddak.mtmanger.ui.view.home.plan.PlanAddView
import com.wonddak.mtmanger.ui.view.home.plan.PlanView
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

        composable(Const.SETTING_HOME) {
            SettingView()
        }
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
) = navigation(startDestination = NavItem.Main.screenRoute, route = Const.HOME) {
    composable(NavItem.Main.screenRoute) {
        InsertDataView(
            mtViewModel, navController
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
    composable(NavItem.Person.screenRoute) {
        InsertDataView(
            mtViewModel, navController
        ) {
            PersonView(
                mtViewModel = mtViewModel
            )
        }
    }
    composable(NavItem.Buy.screenRoute) {
        InsertDataView(
            mtViewModel, navController
        ) {
            BuyView(
                mtViewModel = mtViewModel
            )
        }
    }
    composable(NavItem.Plan.screenRoute) {
        InsertDataView(
            mtViewModel, navController
        ) {
            PlanView(
                navigateNew = { start, end ->
                    navController.navigate(Const.navigationNewPlan(start, end))
                },
                navigateEdit = { start, end, id ->
                    navController.navigate(Const.navigationEditPlan(start, end, id))
                }
            )
        }
    }
    composable(
        Const.navigationNewPlanRout(),
        arguments = listOf(
            navArgument(Const.PLAN_ARG_START) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(Const.PLAN_ARG_END) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        PlanAddView(
            backStackEntry.arguments?.getString(Const.PLAN_ARG_START)!!,
            backStackEntry.arguments?.getString(Const.PLAN_ARG_END)!!,
        ) {
            mtViewModel.addPlan(it) {
                navController.navigate(NavItem.Plan.screenRoute) {
                    popUpTo(Const.navigationNewPlanRout()) {
                        inclusive = true
                    }
                }
            }
        }
    }
    composable(
        Const.navigationEditPlanRout(),
        arguments = listOf(
            navArgument(Const.PLAN_ARG_START) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(Const.PLAN_ARG_END) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(Const.PLAN_ARG_ID) {
                type = NavType.IntType
            }
        )
    ) { backStackEntry ->
        val planId = backStackEntry.arguments?.getInt(Const.PLAN_ARG_ID)
        val plan = mtViewModel.getPlanById(planId)
        if (plan == null) {
            Text("Error")
        } else {
            PlanAddView(
                backStackEntry.arguments?.getString(Const.PLAN_ARG_START)!!,
                backStackEntry.arguments?.getString(Const.PLAN_ARG_END)!!,
                plan
            ) { data ->
                mtViewModel.updatePlan(plan, data) {
                    navController.popBackStack()
                }
            }
        }
    }

}

@Composable
private inline fun <T> NavController.checkRout(
    route: (String?) -> T
): T {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return route(currentRoute)
}

@Composable
private inline fun NavController.checkRout(
    route: (String) -> Boolean
): Boolean {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    return currentRoute?.let { route(it) } ?: false
}

@Composable
fun NavController.isATT(): Boolean {
    return checkRout { it == Const.ATT }
}

@Composable
fun NavController.isSetting(): Boolean {
    return checkRout { it == Const.CATEGORY || it == Const.SETTING_HOME }
}

@Composable
fun NavController.showAd(): Boolean {
    return !checkRout {
        it == Const.CATEGORY || it == Const.SETTING_HOME || it == Const.MT_LIST || it == Const.ATT
    }
}

@Composable
fun NavController.showSettingIcon(): Boolean {
    return !checkRout {
        it == Const.CATEGORY || it == Const.SETTING_HOME || it == Const.MT_LIST || it == Const.MT_ADJUSTMENT || it.startsWith(
            Const.NEW_PLAN
        ) || it.startsWith(Const.EDIT_PLAN)
    }
}

@Composable
fun NavController.showNavigationIcon(): Boolean {
    return checkRout {
        it == Const.CATEGORY || it == Const.SETTING_HOME || it == Const.MT_LIST || it == Const.MT_ADJUSTMENT || it.startsWith(
            Const.NEW_PLAN
        ) || it.startsWith(Const.EDIT_PLAN)
    }
}

@Composable
fun NavController.showBottomNavigation(): Boolean {
    return !checkRout {
        it == Const.CATEGORY || it == Const.SETTING_HOME || it == Const.MT_LIST || it == Const.MT_ADJUSTMENT || it.startsWith(
            Const.NEW_PLAN
        ) || it.startsWith(Const.EDIT_PLAN)
    }
}

@Composable
fun NavController.getTitle(): String {
    return checkRout<String> { route ->
        when (route) {
            Const.MT_LIST -> {
                "MT 리스트"
            }

            Const.MT_ADJUSTMENT -> {
                "정산하기"
            }

            else -> {
                route?.let {
                    if (route.startsWith(Const.NEW_PLAN)) {
                        "계획 추가"
                    } else if (route.startsWith(Const.EDIT_PLAN)) {
                        "계획 수정"
                    } else {
                        val items = listOf(
                            NavItem.Person,
                            NavItem.Buy,
                            NavItem.Plan,
                        )
                        items.find { it.screenRoute == route }?.title ?: "MT 매니저"
                    }
                } ?: "MT 매니저"
            }
        }
    }
}