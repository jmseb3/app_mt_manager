package com.wonddak.mtmanger

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.wonddak.mtmanger.ui.view.home.main.AppContext
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private var backKeyPressedTime: Long = 0

    private val mtViewModel: MTViewModel by viewModel()
    private val payViewModel: PayViewModel by viewModel()

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
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        this.onBackPressedDispatcher.addCallback(this, callback)
        AppContext.apply { set(this@MainActivity) }
        setContent {
            App()
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                window.insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
//                window.insetsController?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            } else {
//                window.decorView.systemUiVisibility = (
//                        // Do not let system steal touches for showing the navigation bar
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                // Hide the nav bar and status bar
//                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                // Keep the app content behind the bars even if user swipes them up
//                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
//                // make navbar translucent - do this already in hideSystemUI() so that the bar
//            }
        }
    }
}



