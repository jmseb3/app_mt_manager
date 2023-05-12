package com.wonddak.mtmanger.ui.view.dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyDialog(
    buyGood: BuyGood? = null,
    categoryList: List<categoryList>,
    onDismiss: () -> Unit = {},
    onAdd: (category: String, name: String, count: String, price: String) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(buyGood?.category ?: categoryList[0].name) }
    var name by remember { mutableStateOf(buyGood?.name ?: "") }
    var count by remember { mutableStateOf(buyGood?.count?.toString() ?: "") }
    var price by remember { mutableStateOf(buyGood?.price?.toString() ?: "") }

    DialogBase(
        titleText = if (buyGood != null) "내역 수정" else "내역 추가",
        confirmText = if (buyGood != null) "수정" else "추가",
        onConfirm = {
            if (category.isEmpty() || name.isEmpty() || price.isEmpty() || count.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.dialog_error_field),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onAdd(category, name, price, count)
            }
        },
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
                    value = category,
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
                                Text(
                                    text = item.name,
                                    fontWeight = if (category == item.name) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                category = item.name
                                expanded = false
                            }
                        )
                    }
                }
            }
            DialogTextField(
                value = name,
                placeHolder = "이름 입력해 주세요",
                label = "이름",
                change = { name = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            DialogTextField(
                value = count,
                placeHolder = "수량을 입력해 주세요",
                label = "수량",
                change = {
                    count = it.replace("\\D".toRegex(), "")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            val focusManager = LocalFocusManager.current
            DialogTextField(
                value = price,
                placeHolder = "가격을 입력해 주세요",
                label = "가격",
                change = {
                    price = it.replace("\\D".toRegex(), "")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}