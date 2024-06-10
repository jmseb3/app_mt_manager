package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.model.SnackBarMsg
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.viewModel.MTViewModel
import org.koin.compose.koinInject

@Composable
fun MtListView(
    mtViewModel: MTViewModel = koinInject(),
    close: () -> Unit
) {
    val mtList by mtViewModel.getMtTotalLIst().collectAsState(initial = emptyList())
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Text(
                text = "MT 리스트",
                color = match2,
                fontSize = 30.sp,
                fontFamily = maple(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, match2, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            ) {
                items(mtList) { mtData ->
                    MtListItem(mtData = mtData) {
                        mtViewModel.snackBarMsg = SnackBarMsg(
                            "${mtData.mtTitle}로 변경했어요."
                        )
                        mtViewModel.setMtId(mtData.mtDataId!!)
                        close()
                    }
                    HorizontalDivider(
                        color = match2
                    )
                }
            }
        }
    }
}

@Composable
fun MtListItem(
    mtData: MtData,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultText(
            text = mtData.mtTitle
        )
        DefaultText(
            text = "${mtData.mtStart} ~ ${mtData.mtEnd}"
        )
    }
}