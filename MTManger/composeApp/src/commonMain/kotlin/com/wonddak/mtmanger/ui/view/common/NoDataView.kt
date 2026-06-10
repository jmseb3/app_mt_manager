package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
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
    var showAddDialog by remember {
        mutableStateOf(false)
    }

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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "현재 선택된 MT 정보를 불러올 수 없어요.\n목록에서 다시 선택해 주세요.",
                            color = match2,
                            fontSize = 18.sp,
                            fontFamily = maple(),
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp),
                            onClick = {
                                navController.navigate(Const.MT_LIST) {
                                    launchSingleTop = true
                                }
                            },
                            border = BorderStroke(2.dp, match2),
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                        ) {
                            DefaultText(text = "MT 목록")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp),
                            onClick = {
                                showAddDialog = true
                            },
                            border = BorderStroke(2.dp, match2),
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
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
    if (showAddDialog) {
        MTDialog(
            null,
            onDismiss = { showAddDialog = false },
            onAdd = { data ->
                mtViewModel.insertMtData(data)
                showAddDialog = false
            }
        )
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "아직 떠나는 MT가 없어요",
            color = match2,
            fontSize = 22.sp,
            fontFamily = maple(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "첫 MT를 만들고 참가자, 구매 내역, 계획을 관리해 보세요.",
            color = match2,
            fontSize = 16.sp,
            fontFamily = maple(),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            modifier = Modifier
                .widthIn(min = 160.dp)
                .heightIn(min = 48.dp),
            onClick = { showAddDialog = true },
            border = BorderStroke(2.dp, match2),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            DefaultText(text = "MT 만들기")
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
