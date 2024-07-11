package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.AppTrackingTransparency.ATTrackingManager

@Composable
actual fun ADTrackingView(
    finish: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultText(text = "무료앱은 광고로 유지 됩니다.")
        DefaultText(text = "사용자의 광고 활동 정보(예: 광고를 본 후 수행한 작업) 를 광고 서비스 제공업체와 공유할지 결정하세요. 공유된 정보는 개인에게 최적화된 광고 경험을 제공하기 위해 사용합니다.")
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                requestAD(finish)
            },
            border = BorderStroke(2.dp, match2)
        ) {
            DefaultText(text = "계속")
        }
    }
}


fun requestAD(finish: () -> Unit) {
    ATTrackingManager.requestTrackingAuthorizationWithCompletionHandler { status ->
        CoroutineScope(Dispatchers.Main).launch {
            finish()
        }
    }
}