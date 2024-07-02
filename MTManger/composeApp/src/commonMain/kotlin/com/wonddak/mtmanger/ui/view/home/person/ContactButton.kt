package com.wonddak.mtmanger.ui.view.home.person

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.room.entity.*
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.common.DefaultText
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_baseline_contact_phone_24
import org.jetbrains.compose.resources.painterResource

@Composable
internal expect fun  ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit
)

@Composable
internal fun ContactButtonContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultText(
            text = "연락처에서 불러오기",
            color = match1
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            painter = painterResource(resource = Res.drawable.ic_baseline_contact_phone_24),
            contentDescription = null,
            tint = match1
        )
    }
}