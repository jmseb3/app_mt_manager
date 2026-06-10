package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "앱은 광고로 운영돼요",
            color = match2,
            fontFamily = maple(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "광고 활동 정보 공유 여부를 선택할 수 있어요. 허용하지 않아도 앱은 계속 사용할 수 있습니다.",
            color = match2,
            fontFamily = maple(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        Text(
            text = "다음 화면에서 iOS 권한 안내가 표시됩니다.",
            color = match2.copy(alpha = 0.78f),
            fontFamily = maple(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            onClick = {
                requestAD(finish)
            },
            border = BorderStroke(2.dp, match2)
        ) {
            DefaultText(text = "권한 설정 계속하기")
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
