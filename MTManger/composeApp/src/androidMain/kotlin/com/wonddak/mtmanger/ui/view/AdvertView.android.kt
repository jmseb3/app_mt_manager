package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.content.pm.ApplicationInfo
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wonddak.mtmanger.core.Const

private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/2934735716"
private const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-2369897242309575/1756100845"

@Composable
actual fun AdvertView(modifier: Modifier) {
    val isInEditMode = LocalInspectionMode.current
    val context = LocalContext.current
    val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    val id = if (Const.USE_SCREENSHOT_MOCK_DATA || !isDebuggable) {
        PROD_BANNER_AD_UNIT_ID
    } else {
        TEST_BANNER_AD_UNIT_ID
    }
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    this.setAdSize(AdSize.BANNER)
                    adUnitId = id
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { it.loadAd(AdRequest.Builder().build()) })
    }
}
