package com.wonddak.mtmanger.ui.view.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match2

@Composable
fun DefaultText(
    modifier: Modifier = Modifier,
    color: Color = match2,
    text: String,
    fontSize : TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null
) {
    Text(
        modifier = modifier,
        text = text,
        fontFamily = maple,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        color = color,
        fontWeight = fontWeight
    )
}