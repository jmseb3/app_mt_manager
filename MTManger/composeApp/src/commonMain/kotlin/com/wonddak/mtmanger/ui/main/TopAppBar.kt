package com.wonddak.mtmanger.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.back_arrow
import mtmanger.composeapp.generated.resources.ic_baseline_settings_24
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppContent(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    TopAppBar(
        title = {
            DefaultText(
                text = "MT 매니저",
                color = match1
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = match2,
        ),
        actions = {
            AnimatedVisibility(visible = currentRoute != Const.MT_LIST) {
                IconButton(
                    onClick = {
                        navController.navigate(Const.SETTING) {
                            this.launchSingleTop = true
                        }
                    },
                    enabled = currentRoute != Const.SETTING
                ) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_baseline_settings_24),
                        contentDescription = null,
                        tint = match2,
                    )
                }
            }
        },
        navigationIcon = {
            AnimatedVisibility(navController.isSetting() || navController.isMTList()) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.back_arrow),
                        contentDescription = null,
                        tint = match1,
                    )
                }
            }
        }
    )
}