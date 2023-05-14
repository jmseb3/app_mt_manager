package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.dialog.MTDialog
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
fun NoDataBase(
    mtViewModel: MTViewModel,
    content: @Composable () -> Unit
) {
    val mainId by mtViewModel.mainMtId.collectAsState(0)

    Column(modifier = Modifier.fillMaxSize()) {
        if (mainId == 0) {
            NoDataView(mtViewModel)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(match1)
            ) {
                content()
            }
        }
    }
}

@Composable
fun NoDataView(
    mtViewModel: MTViewModel
) {
    var showAddDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "현재 떠나는 MT가 없어요\n아래버튼을 눌러 추가해주세요",
            color = match2,
            fontSize = 20.sp,
            fontFamily = maple,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { showAddDialog = true },
            border = BorderStroke(2.dp, match2),
        ) {
            DefaultText(text = "MT 떠나기")
        }
    }
    if (showAddDialog) {
        MTDialog(
            null,
            onDismiss = { showAddDialog = false },
            onAdd = { title, fee, start, end ->
                mtViewModel.insertMtData(
                    MtData(
                        null,
                        title,
                        fee.toInt(),
                        start,
                        end
                    )
                )
                showAddDialog = false

            }
        )
    }
}