package com.wonddak.mtmanger.ui.view.home.plan

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.wonddak.mtmanger.room.entity.Plan

@Composable
internal actual fun PlanImageView(
    modifier: Modifier,
    plan: Plan
) {
    if (plan.imgBytes != null) {
        Image(
            bitmap = BitmapFactory.decodeByteArray(
                plan.imgBytes,
                0,
                plan.imgBytes.size
            ).asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        )
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