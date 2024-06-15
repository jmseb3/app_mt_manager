package com.wonddak.mtmanger.ui.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.*
import com.wonddak.mtmanger.toPriceString
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.dialog.MTDialog
import com.wonddak.mtmanger.viewModel.MTViewModel

@Composable
fun MainView(
    mtViewModel: MTViewModel,
    showMTList: () -> Unit
) {
    val resource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState()
    var showAddDialog by remember {
        mutableStateOf(false)
    }
    var showEditDialog by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .padding(vertical = 5.dp)
            )
            if (resource is Resource.Success) {
                (resource as Resource.Success<MtDataList>).data?.let { mtData ->
                    DefaultText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, match2, RoundedCornerShape(10.dp))
                            .padding(vertical = 5.dp),
                        text = mtData.mtdata.mtTitle,
                        fontSize = 30.sp
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .padding(vertical = 5.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, match2, RoundedCornerShape(10.dp))
                            .padding(vertical = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            DefaultText(
                                modifier = Modifier.weight(4f),
                                text = mtData.mtdata.mtStart,
                                fontSize = 23.sp
                            )
                            DefaultText(
                                modifier = Modifier.weight(1f),
                                text = "부터",
                                fontSize = 23.sp
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                                .padding(vertical = 5.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            DefaultText(
                                modifier = Modifier.weight(4f),
                                text = mtData.mtdata.mtEnd,
                                fontSize = 23.sp
                            )
                            DefaultText(
                                modifier = Modifier.weight(1f),
                                text = "까지",
                                fontSize = 23.sp
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .padding(vertical = 5.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, match2, RoundedCornerShape(10.dp))
                            .padding(vertical = 10.dp),

                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        DefaultText(
                            modifier = Modifier.weight(2f),
                            text = "1인당 회비",
                            fontSize = 23.sp
                        )
                        DefaultText(
                            modifier = Modifier.weight(2f),
                            text = mtData.mtdata.fee.toPriceString(""),
                            fontSize = 23.sp
                        )
                        DefaultText(
                            modifier = Modifier.weight(1f),
                            text = "원",
                            fontSize = 23.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .padding(vertical = 5.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, match2, RoundedCornerShape(10.dp))
                            .padding(vertical = 10.dp),

                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        DefaultText(
                            modifier = Modifier.weight(2f),
                            text = "참여인원",
                            fontSize = 23.sp
                        )
                        DefaultText(
                            modifier = Modifier.weight(2f),
                            text = mtData.personList.size.toString(),
                            fontSize = 23.sp
                        )
                        DefaultText(
                            modifier = Modifier.weight(1f),
                            text = "명",
                            fontSize = 23.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                            .padding(vertical = 5.dp)
                    )
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showEditDialog = true },
                        border = BorderStroke(2.dp, match2),
                    ) {
                        DefaultText(text = "수정")
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showMTList() },
                        border = BorderStroke(2.dp, match2),
                    ) {
                        DefaultText(text = "MT 목록")
                    }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showAddDialog = true },
                        border = BorderStroke(2.dp, match2),
                    ) {
                        DefaultText(text = "다른 여행 떠나기")
                    }
                    if (showEditDialog) {
                        MTDialog(
                            mtData.mtdata,
                            onDismiss = {
                                showEditDialog = false
                            },
                            onAdd = { data ->
                                mtViewModel.insertMtData(data)
                                mtViewModel.showSnackBarMsg("정보를 수정했습니다.")
                                showEditDialog = false
                            }
                        )
                    }

                    if (showAddDialog) {
                        MTDialog(
                            null,
                            onDismiss = {
                                showAddDialog = false
                            },
                            onAdd = { data ->
                                mtViewModel.insertMtData(data)
                                mtViewModel.showSnackBarMsg("${data.mtTitle}로 변경했어요")
                                showAddDialog = false
                            }
                        )
                    }
                }
            }
        }
    }


}