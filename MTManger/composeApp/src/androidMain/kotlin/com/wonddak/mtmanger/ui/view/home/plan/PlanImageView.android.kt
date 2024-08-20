package com.wonddak.mtmanger.ui.view.home.plan

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.ui.view.common.ByteArrayImageView

@Composable
internal actual fun PlanImageView(
    modifier: Modifier,
    plan: Plan
) {
    if (plan.imgBytes != null) {
        ByteArrayImageView(modifier,plan.imgBytes)
    } else if (plan.imgSrc.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(
                model = Uri.parse(plan.imgSrc)  // or ht
            ),
            contentDescription = "",
            modifier = modifier,
            alignment = Alignment.Center
        )
    }
}