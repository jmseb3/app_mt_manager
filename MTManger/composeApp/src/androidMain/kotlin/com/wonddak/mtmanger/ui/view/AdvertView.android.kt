package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wonddak.mtmanger.BuildConfig

@Composable
actual fun AdvertView(modifier: Modifier) {
    val isInEditMode = LocalInspectionMode.current
    val id = if (BuildConfig.DEBUG) {
        "ca-app-pub-3940256099942544/2934735716"
    } else {
        "ca-app-pub-2369897242309575/1756100845"
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