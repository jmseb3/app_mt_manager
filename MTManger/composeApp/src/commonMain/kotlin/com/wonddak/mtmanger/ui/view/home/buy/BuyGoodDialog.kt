package com.wonddak.mtmanger.ui.view.home.buy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.room.entity.BuyGood
import com.wonddak.mtmanger.room.entity.SimpleBuyGood
import com.wonddak.mtmanger.room.entity.categoryList
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_won
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyDialog(
    buyGood: BuyGood? = null,
    categoryList: List<categoryList>,
    onDismiss: () -> Unit = {},
    onAdd: (SimpleBuyGood) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var simpleBuyGood by remember {
        mutableStateOf(buyGood?.toSimple() ?: SimpleBuyGood(categoryList[0].name))
    }
    DialogBase(
        titleText = if (buyGood != null) "내역 수정" else "내역 추가",
        confirmText = if (buyGood != null) "수정" else "추가",
        onConfirm = {
            onAdd(simpleBuyGood)
        },
        confirmEnabled = simpleBuyGood.isConfirm(),
        onDismiss = onDismiss
    )
    {
        Column() {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                DialogTextField(
                    modifier = Modifier.menuAnchor(),
                    value = simpleBuyGood.category,
                    placeHolder = "",
                    label = "카테고리",
                    change = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoryList.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.background(Color.White),
                            text = {
                                DefaultText(
                                    text = item.name,
                                    fontWeight = if (simpleBuyGood.category == item.name) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                simpleBuyGood = simpleBuyGood.copy(category = item.name)
                                expanded = false
                            }
                        )
                    }
                }
            }
            DialogTextField(
                value = simpleBuyGood.name,
                placeHolder = "이름 입력해 주세요",
                label = "이름",
                change = { simpleBuyGood = simpleBuyGood.copy(name = it) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = simpleBuyGood.count,
                placeHolder = "수량을 입력해 주세요",
                label = "수량",
                change = {
                    simpleBuyGood = simpleBuyGood.copy(count = it.replace("\\D".toRegex(), ""))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            val focusManager = LocalFocusManager.current
            DialogTextField(
                value = simpleBuyGood.price,
                placeHolder = "가격을 입력해 주세요",
                label = "가격",
                change = {
                    simpleBuyGood = simpleBuyGood.copy(price = it.replace("\\D".toRegex(), ""))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_won),
                        contentDescription = null,
                        tint = match1,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}