package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.ui.view.common.ByteArrayImageView

@Composable
internal fun PlanImageView(modifier: Modifier,plan: Plan) {
    if (plan.imgBytes != null) {
        ByteArrayImageView(modifier,plan.imgBytes)
    } else if (plan.imgSrc.isNotEmpty()) {
        AsyncImage(
            model = plan.imgSrc,
            contentDescription = "",
            modifier = modifier,
            alignment = Alignment.Center
        )
    }
}