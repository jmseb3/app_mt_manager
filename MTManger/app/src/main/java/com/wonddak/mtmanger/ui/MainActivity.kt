package com.wonddak.mtmanger.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wonddak.mtmanger.ui.main.BottomNavigationBar
import com.wonddak.mtmanger.ui.main.NavGraph
import com.wonddak.mtmanger.ui.main.TopAppContent
import com.wonddak.mtmanger.ui.main.isMTList
import com.wonddak.mtmanger.ui.main.isSetting
import com.wonddak.mtmanger.ui.theme.MTMangerTheme
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.AdvertView
import com.wonddak.mtmanger.ui.view.SplashView
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private var backKeyPressedTime: Long = 0

    private val mtViewModel: MTViewModel by viewModel()
    private val preferences: SharedPreferences by inject()

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this@MainActivity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this, callback)
        val mainmtid: Int = preferences.getInt("id", 0)
        mtViewModel.setMtId(mainmtid)
        setContent {
            MTMangerTheme {
                val navController = rememberNavController()
                var showSplash by remember {
                    mutableStateOf(true)
                }

                if (showSplash) {
                    SplashView() {
                        showSplash = false
                    }
                } else {
                    Scaffold(
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
                                val removeAd by mtViewModel.removeAdStatus.collectAsState(true)
                                if (!removeAd) {
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
}



