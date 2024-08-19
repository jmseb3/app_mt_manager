package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.ui.view.common.ByteArrayImageView

@Composable
internal actual fun PlanImageView(
    modifier: Modifier,
    plan: Plan
) {
    if (plan.imgBytes != null) {
        ByteArrayImageView(modifier,plan.imgBytes)
    }
}

