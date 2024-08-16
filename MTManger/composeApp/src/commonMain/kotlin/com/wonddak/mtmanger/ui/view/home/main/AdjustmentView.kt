package com.wonddak.mtmanger.ui.view.home.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.room.entity.Person
import com.wonddak.mtmanger.toPriceString
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.viewModel.MTViewModel
import kotlin.math.abs

@Composable
fun AdjustmentView(mtViewModel: MTViewModel) {
    val resource: Resource<MtDataList> by mtViewModel.nowMtDataList.collectAsState()

    val scroll = rememberScrollState()
    var showMtTitle by remember { mutableStateOf(true) }
    var showMtPerson by remember { mutableStateOf(true) }
    var equalDistribution by remember { mutableStateOf(true) }
    var personPerDistribution: Map<Person, Int> by remember { mutableStateOf(mapOf()) }

    var editMessage by remember { mutableStateOf(false) }
    var addMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().imePadding()
    ) {
        if (resource is Resource.Success) {
            (resource as Resource.Success<MtDataList>).data?.let { mtData ->
                val availableAmount = mtData.availableAmount
                if(if (!equalDistribution) {personPerDistribution.values.sum() == abs(mtData.availableAmount)} else true) {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {  },
                        border = BorderStroke(2.dp, match2),
                    ) {
                        DefaultText(text = "이미지 만들기")
                    }
                }
                Column(modifier = Modifier.fillMaxSize().verticalScroll(scroll)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .wrapContentSize()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            if (showMtTitle) {
                                Text(
                                    mtData.mtdata.mtTitle + " 정산 보고",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(20.dp))
                            }
                            Column {
                                Text(
                                    text = "참여 인원",
                                    fontWeight = FontWeight.Bold
                                )
                                if (mtData.isEmptyPerson) {
                                    Text("참여 인원 정보가 없습니다.")
                                } else {
                                    if (showMtPerson) {
                                        Text("--- ${mtData.totalPersonCount}명 ---")
                                        mtData.personList.forEachIndexed { index, person ->
                                            Text("[${index + 1}] ${person.name}")
                                        }
                                    } else {
                                        Text("${mtData.personList.first().name} 외 ${mtData.totalPersonCount - 1}명")
                                    }
                                }
                                Spacer(Modifier.height(20.dp))
                            }
                            Column {
                                Text(
                                    text = if (availableAmount >= 0) "남은 금액" else "초과 금액",
                                    fontWeight = FontWeight.Bold
                                )
                                if (availableAmount >= 0) {
                                    Text(availableAmount.toPriceString())
                                } else {
                                    Text(
                                        (-availableAmount).toPriceString(),
                                        color = Color.Red
                                    )
                                }
                                if (availableAmount != 0) {
                                    Text(
                                        text = if (availableAmount >= 0) "분배 금액" else "추가 지불 금액",
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (equalDistribution) {
                                        val distributionPrice = mtData.getDistributionPrice
                                        Text("1인당 ${distributionPrice.first.toPriceString()}")
                                        if (distributionPrice.second > 0) {
                                            Text("남은 금액 ${distributionPrice.second.toPriceString()}")
                                        }
                                    } else {
                                        mtData.personList.forEach { person ->
                                            Row {
                                                Text(
                                                    person.name,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    (personPerDistribution[person]
                                                        ?: 0).toPriceString(),
                                                    modifier = Modifier.weight(1f),
                                                    color = if (availableAmount >= 0) Color.Black else Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (editMessage && addMessage.isNotEmpty()) {
                                Column {
                                    Spacer(Modifier.height(20.dp))
                                    Text(addMessage)
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        OptionButton(showMtTitle, "Title 숨기기 / 보이기") {
                            showMtTitle = it
                        }
                        OptionButton(showMtPerson, "참여 인원 전체 숨기기 / 보이기") {
                            showMtPerson = it
                        }
                        OptionButton(equalDistribution, "초과/남은 금액 균등 분배") {
                            equalDistribution = it
                            if (equalDistribution) {
                                personPerDistribution = mapOf()
                            }
                        }
                        if (!equalDistribution) {
                            val pattern = remember { Regex("^\\d+\$") }
                            Card(
                                modifier = Modifier.padding(10.dp).fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp).fillMaxWidth()
                                ) {
                                    if (personPerDistribution.values.sum() == abs(mtData.availableAmount)) {
                                        Text("남은 금액이 모두 정상적으로 분배 되었습니다.")
                                    } else {
                                        Text("금액이 초과되었거나 부족합니다.")
                                        Text("초과/부족 금액 : ${abs(mtData.availableAmount) - personPerDistribution.values.sum()}")
                                    }
                                    mtData.personList.forEach { person ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            var payFee by remember { mutableStateOf("") }
                                            Text(
                                                person.name,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center
                                            )
                                            TextField(
                                                value = payFee,
                                                onValueChange = {
                                                    if (it.isEmpty()) {
                                                        payFee = it
                                                        val temp =
                                                            personPerDistribution.toMutableMap()
                                                        if (temp.containsKey(person)) {
                                                            temp.remove(person)
                                                            personPerDistribution = temp
                                                        }
                                                    } else if (it.matches(pattern)) {
                                                        payFee = it
                                                        val temp =
                                                            personPerDistribution.toMutableMap()
                                                        temp[person] = payFee.toInt()
                                                        personPerDistribution = temp
                                                    }
                                                },
                                                modifier = Modifier.weight(1f),
                                                maxLines = 1,
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Number,
                                                    imeAction = ImeAction.Next
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        OptionButton(editMessage, "추가 메세지 작성") {
                            editMessage = it
                        }
                        if (editMessage) {
                            TextField(
                                addMessage,
                                {
                                    addMessage = it
                                },
                                trailingIcon = {
                                    if (addMessage.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                addMessage = ""
                                            }
                                        ) {
                                            Icon(Icons.Default.Clear, null)
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionButton(
    value: Boolean,
    text: String,
    onChange: (value: Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Switch(
            checked = value,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = match2.copy(alpha = 0.8f),
                uncheckedThumbColor = match2.copy(alpha = 0.5f),
                checkedTrackColor = match1.copy(alpha = 0.9f),
                uncheckedTrackColor = match1.copy(alpha = 0.9f),
                checkedBorderColor = match2,
                uncheckedBorderColor = match2
            )
        )
        Text(
            text,
            color = match2,
            fontFamily = maple()
        )
    }
}

