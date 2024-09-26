package com.wonddak.mtmanger.ui.main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonddak.mtmanger.model.NavItem
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavItem.Main,
        NavItem.Person,
        NavItem.Buy,
        NavItem.Plan,
    )

    NavigationBar(
        containerColor = match2,
    ) {
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
                    Icon(
                        painter = painterResource(resource = item.icon),
                        contentDescription = null
                    )
                },
                label = {
                    DefaultText(
                        text = item.title,
                        color = if (currentRoute == item.screenRoute) match1 else Color.White
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = match1,
                    unselectedIconColor = Color.White,
                    indicatorColor = match2
                ),
            )
        }
    }
}
