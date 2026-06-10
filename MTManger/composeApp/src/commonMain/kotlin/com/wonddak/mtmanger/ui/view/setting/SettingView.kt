package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.util.AppUtil

@Composable
fun SettingView(
    navigateCategory: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, match2, RoundedCornerShape(8.dp))
        ) {
            SettingRow(
                icon = Icons.AutoMirrored.Filled.List,
                title = "카테고리 편집",
                onClick = navigateCategory
            )
            HorizontalDivider(
                color = match2
            )
            SettingRow(
                icon = Icons.Filled.Email,
                title = "문의하기",
                onClick = {
                    AppUtil.sendMail()
                }
            )
            HorizontalDivider(
                color = match2
            )
            SettingRow(
                icon = Icons.Filled.Info,
                title = "버전 정보",
                value = getVersion()
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .then(
            if (onClick == null) {
                Modifier
            } else {
                Modifier.clickable(onClick = onClick)
            }
        )
        .padding(horizontal = 16.dp)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            imageVector = icon,
            contentDescription = null,
            tint = match2
        )
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = match2,
            fontFamily = maple(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        if (value == null) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = match2
            )
        } else {
            Text(
                text = value,
                color = match2,
                fontFamily = maple(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

expect fun getVersion(): String
