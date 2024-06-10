package com.wonddak.mtmanger.ui.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.*
import com.wonddak.mtmanger.toPriceString
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.FeeInfo
import com.wonddak.mtmanger.ui.view.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.view.dialog.PersonDialog
import com.wonddak.mtmanger.viewModel.MTViewModel
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.dialog_delete_reset
import mtmanger.composeapp.generated.resources.ic_baseline_phone_24
import mtmanger.composeapp.generated.resources.info_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PersonView(
    mtViewModel: MTViewModel,
) {
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
            Text(
                text = "참여자 명단",
                color = match2,
                fontSize = 30.sp,
                fontFamily = maple(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp)),
                thickness = 4.dp,
                color = match2
            )
            Spacer(modifier = Modifier.height(3.dp))
            BuyGoodItemText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(resource = Res.string.info_text),
                color = match2
            )
            Spacer(modifier = Modifier.height(3.dp))
            PersonItemList(
                Modifier.fillMaxHeight(), mtViewModel
            )
        }
        Row {
            PersonPanel(mtViewModel)
        }
    }
}

@Composable
fun PersonPanel(
    mtViewModel: MTViewModel,
) {
//    val context = LocalContext.current
    val resource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState()
    var showItemReset by remember {
        mutableStateOf(false)
    }
    var showAddDialog by remember {
        mutableStateOf(false)
    }
    if (resource is Resource.Success) {
        (resource as Resource.Success<MtDataList>).data?.let { mtData ->


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    thickness = 4.dp,
                    color = match2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.weight(3f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        val modifier = Modifier.padding(3.dp)
                        FeeInfo(
                            modifier, text = "총 참여자", fee = mtData.personList.size, feeIndex = "명"
                        )
                        FeeInfo(modifier,
                            text = "받은 금액",
                            fee = mtData.personList.sumOf { it.paymentFee })
                    }
                    Column(
                        Modifier.weight(2f), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showAddDialog = true },
                            border = BorderStroke(2.dp, match2)
                        ) {
                            BuyGoodItemText(text = "참여자 추가")
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(), onClick = {
                                showItemReset = true
                            }, border = BorderStroke(2.dp, match2)
                        ) {
                            BuyGoodItemText(text = "초기화")
                        }
                    }
                }
            }
        }
    }

    if (showItemReset) {
        DeleteDialog(
            msg = stringResource(resource = Res.string.dialog_delete_reset),
            onDelete = {
                mtViewModel.clearPersonData()
                showItemReset = false
            },
            onDismiss = {
                showItemReset = false
            },
        )
    }
    if (showAddDialog) {
        PersonDialog(
            null,
            onDismiss = {
                showAddDialog = false
            },
            onAdd = { data ->
                mtViewModel.insertPerson(data)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PersonItemList(
    modifier: Modifier = Modifier,
    mtViewModel: MTViewModel,
) {
    val resource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState()
    Card(
        modifier = modifier, shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(
            containerColor = match2
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)

            ) {
                val weight1 = Modifier.weight(1f)
                BuyGoodItemText(
                    weight1, color = match1, text = "이름"
                )
                BuyGoodItemText(
                    weight1, color = match1, text = "납부금액"
                )
                BuyGoodItemText(
                    weight1, color = match1, text = "폰"
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = match1
                )
            ) {
                if (resource is Resource.Success) {
                    (resource as Resource.Success<MtDataList>).data?.let {
                        val personList = it.personList
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        ) {
                            itemsIndexed(personList) { index, person ->
                                PersonItemView(person, mtViewModel)
                                if (index != personList.size - 1) {
                                    HorizontalDivider(
                                        color = match2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonItemView(
    person: Person,
    mtViewModel: MTViewModel,
) {
    var showItemDelete by remember {
        mutableStateOf(false)
    }
    var showEditDialog by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .combinedClickable(
                onClick = {
                    showEditDialog = true
                },
                onLongClick = {
                    showItemDelete = true
                },
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        val weight1 = Modifier.weight(1f)
        BuyGoodItemText(
            weight1, text = person.name
        )
        BuyGoodItemText(
            weight1, text = person.paymentFee.toPriceString()
        )
        IconButton(
            modifier = weight1,
            onClick = {
//                context.startActivity(
//                    Intent(
//                        Intent.ACTION_DIAL, Uri.parse("tel:" + person.phoneNumber)
//                    )
//                )
            }
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.ic_baseline_phone_24),
                contentDescription = null,
                tint = match2
            )
        }
    }

    if (showItemDelete) {
        DeleteDialog(
            onDelete = {
                mtViewModel.deletePerson(person.personId!!)
                showItemDelete = false
            },
            onDismiss = {
                showItemDelete = false
            },
        )
    }
    if (showEditDialog) {
        PersonDialog(
            person,
            onDismiss = {
                showEditDialog = false
            },
            onAdd = { data ->
                mtViewModel.updatePerson(person.personId!!, data)
                showEditDialog = false
            }
        )
    }
}