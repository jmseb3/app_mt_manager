package com.wonddak.mtmanger.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.model.BottomNavItem
import com.wonddak.mtmanger.ui.theme.MTMangerTheme
import com.wonddak.mtmanger.ui.view.BuyView
import com.wonddak.mtmanger.ui.view.MainView
import com.wonddak.mtmanger.ui.view.PersonView
import com.wonddak.mtmanger.ui.view.PlanView
import com.wonddak.mtmanger.ui.view.common.NoDataBase
import com.wonddak.mtmanger.viewModel.MTViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var backKeyPressedTime: Long = 0

    private val mtViewModel: MTViewModel by viewModels()

    @Inject
    lateinit var billingModule: BillingModule

    @Inject
    lateinit var preferences: SharedPreferences

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this@MainActivity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)

        val mainmtid: Int = preferences.getInt("id", 0)

        mtViewModel.setMtId(mainmtid)
        lifecycleScope.launchWhenResumed {
            billingModule.removeAddStatus.collect {
                mtViewModel.setRemoveAddStatus(it)
            }
        }

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
            NoDataBase(
                mtViewModel
            ) {
                MainView(mtViewModel = mtViewModel)
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
                    Text(text = item.title)
                }
            )
        }
    }
}