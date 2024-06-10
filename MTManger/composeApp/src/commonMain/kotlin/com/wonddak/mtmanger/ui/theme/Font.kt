package com.wonddak.mtmanger.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.maplestory
import org.jetbrains.compose.resources.Font

@Composable
fun maple() = FontFamily(
    Font(Res.font.maplestory, weight = FontWeight.Normal)
)

