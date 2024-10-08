package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.util.AppUtil
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SettingView(
    payViewModel: PayViewModel = koinViewModel(),
    navigateCategory: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(10.dp)
            .noRippleClickable(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = navigateCategory,
            border = BorderStroke(2.dp, match2),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultText(text = "카테고리 편집")
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = match2)
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column {
                val removeAdStatus = payViewModel.removeAdStatus
                SettingAdFooter(removeAdStatus)
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        AppUtil.sendMail()
                    },
                    border = BorderStroke(2.dp, match2),
                ) {
                    DefaultText(text = "문의하기")
                }
            }
            HorizontalDivider(
                color = match2
            )
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefaultText(text = "버전 정보")
                    DefaultText(text = getVersion())
                }
            }
        }
    }
}

expect fun getVersion(): String