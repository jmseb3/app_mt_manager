package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.wonddak.mtmanger.room.entity.Plan
import org.jetbrains.skia.Image

@Composable
internal actual fun PlanImageView(
    modifier: Modifier,
    plan: Plan
) {
    if (plan.imgBytes != null) {
        Image(
            bitmap = Image.makeFromEncoded(plan.imgBytes).toComposeImageBitmap(),
            contentDescription = "",
            modifier = modifier,
            alignment = Alignment.Center
        )
    }
}

