package com.wonddak.mtmanger.ui.view.home.plan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.Plan

@Composable
internal expect fun PlanImageView(modifier: Modifier,plan: Plan)