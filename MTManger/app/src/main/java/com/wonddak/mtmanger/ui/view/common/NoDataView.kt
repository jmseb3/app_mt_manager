package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match2

@Composable
fun NoDataBase(
    mainId :Int,
    content : @Composable () ->Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (mainId == 0) {
            NoDataView()
        } else {
            content()
        }
    }
}
@Composable
fun NoDataView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "현재 떠나는 MT가 없어요\n메인화면에서 추가해주세요",
            color = match2,
            fontSize = 20.sp,
            fontFamily = maple,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun NoDataViewPreview(){
    NoDataView()
}