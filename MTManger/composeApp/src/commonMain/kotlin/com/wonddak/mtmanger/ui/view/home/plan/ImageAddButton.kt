package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match1
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.add_photo
import org.jetbrains.compose.resources.painterResource

@Composable
internal expect fun ImageAddButton(
    modifier: Modifier,
    color: Color = match1,
    action: (ByteArray) -> Unit
)

@Composable
internal fun ImageAddButtonIcon(
    color :Color = match1
) {
    Icon(
        modifier = Modifier.size(18.dp),
        painter = painterResource(resource = Res.drawable.add_photo),
        contentDescription = null,
        tint = color
    )
}