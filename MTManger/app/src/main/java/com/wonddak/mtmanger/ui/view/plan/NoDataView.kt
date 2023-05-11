package com.wonddak.mtmanger.ui.view.plan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1

@Composable
fun NoDataView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "현재 떠나는 MT가 없어요 \n 메인화면에서 추가해주세요",
            color = match1,
            fontSize = 20.sp,
            fontFamily = maple
        )
    }
}

@Preview
@Composable
fun NoDataViewPreview(){
    NoDataView()
}