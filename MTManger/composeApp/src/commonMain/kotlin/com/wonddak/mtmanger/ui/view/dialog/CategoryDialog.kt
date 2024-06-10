package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.wonddak.mtmanger.room.entity.categoryList
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@Composable
fun CategoryDialog(
    category: categoryList,
    onDismiss: () -> Unit = {},
    onAdd: (category: String) -> Unit
) {
    var categoryInput by remember {
        mutableStateOf(category.name)
    }
    DialogBase(
        titleText = "카테고리 수정",
        confirmText = "수정",
        onConfirm = {
            onAdd(categoryInput)
        },
        confirmEnabled = categoryInput.isNotEmpty(),
        onDismiss = onDismiss
    )
    {
        DialogTextField(
            value = categoryInput,
            placeHolder = "카테고리를 입력해 주세요",
            label = "카테고리"
        ) {
            categoryInput = it
        }
    }
}