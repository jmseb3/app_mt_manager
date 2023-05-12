package com.wonddak.mtmanger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.model.BottomNavItem
import com.wonddak.mtmanger.ui.theme.MTMangerTheme
import com.wonddak.mtmanger.ui.view.BuyView
import com.wonddak.mtmanger.ui.view.MainView
import com.wonddak.mtmanger.ui.view.PersonView
import com.wonddak.mtmanger.ui.view.PlanView
import com.wonddak.mtmanger.viewModel.MTViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    private val mtViewModel: MTViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MTMangerTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) {
                    Box(Modifier.padding(it)) {
                        NavGraph(
                            navController = navController,
                            mtViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    mtViewModel: MTViewModel
) {
    NavHost(navController = navController, startDestination = BottomNavItem.Main.screenRoute) {

        composable(BottomNavItem.Main.screenRoute) {
            MainView(mtViewModel = mtViewModel)
        }
        composable(BottomNavItem.Person.screenRoute) {
            PersonView(mtViewModel)
        }
        composable(BottomNavItem.Buy.screenRoute) {
            BuyView(mtViewModel = mtViewModel)
        }
        composable(BottomNavItem.Plan.screenRoute) {
            PlanView(mtViewModel)
        }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Main,
        BottomNavItem.Person,
        BottomNavItem.Buy,
        BottomNavItem.Plan,
    )

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                label = {
                    Text(text = item.screenRoute)
                }
            )
        }
    }
}