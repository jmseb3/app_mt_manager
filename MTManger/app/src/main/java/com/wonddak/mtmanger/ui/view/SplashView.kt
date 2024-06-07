package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import kotlinx.coroutines.delay

@Composable
fun SplashView(
    removeSplash : () -> Unit = {}
) {
    LaunchedEffect(true) {
        delay(2_000)
        removeSplash()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(match2),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = null,
            modifier = Modifier.size(250.dp, 300.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontFamily = maple,
            fontSize = 30.sp,
            color = match1,
            fontWeight = FontWeight.Bold
        )

    }
}

@Preview
@Composable
fun SplashViewPreview() {
    SplashView()
}