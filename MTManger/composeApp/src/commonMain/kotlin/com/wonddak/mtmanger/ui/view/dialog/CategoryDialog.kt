package com.wonddak.mtmanger.ui.view.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.view.common.DialogBase
import com.wonddak.mtmanger.ui.view.common.DialogTextField

@Composable
fun CategoryDialog(
    category: categoryList,
    onDismiss: () -> Unit = {},
    onAdd: (category: String) -> Unit
) {
//    val context = LocalContext.current
    var categoryInput by remember {
        mutableStateOf(category.name)
    }
    DialogBase(
        titleText = "카테고리 수정",
        confirmText = "수정",
        onConfirm = {
            if (categoryInput.isEmpty()) {
//                Toast.makeText(context, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                onAdd(categoryInput)
            }
        },
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