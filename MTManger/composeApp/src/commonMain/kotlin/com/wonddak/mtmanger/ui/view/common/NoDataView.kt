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
import androidx.navigation.NavHostController
import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.home.main.MTDialog
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
inline fun InsertDataView(
    mtViewModel: MTViewModel,
    navController: NavHostController,
    content: @Composable (MtDataList) -> Unit
) {
    val planResource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState(Resource.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(match1)
    ) {
        if (mtViewModel.mainMtId <= 0) {
            NoDataView {
                mtViewModel.insertMtData(it)
            }
        } else {
            if (planResource is Resource.Success) {
                val mtDataList = (planResource as Resource.Success<MtDataList>).data
                if (mtDataList == null) {
                    Column {
                        Text("현재 설정된 데이터가 유효하지 않아요.. 확인부탁드립니다.")
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate(Const.MT_LIST) {
                                    launchSingleTop = true
                                }
                            },
                            border = BorderStroke(2.dp, match2),
                        ) {
                            DefaultText(text = "MT 목록")
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate(Const.MT_ADJUSTMENT) {
                                    launchSingleTop = true
                                }
                            },
                            border = BorderStroke(2.dp, match2),
                        ) {
                            DefaultText(text = "다른 여행 떠나기")
                        }
                    }
                } else {
                    content(mtDataList)
                }
            }
        }
    }
}

@Composable
fun NoDataView(
    insertMtData : (MtData) -> Unit
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
            fontFamily = maple(),
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
            onAdd = { data ->
                insertMtData(data)
                showAddDialog = false
            }
        )
    }
}