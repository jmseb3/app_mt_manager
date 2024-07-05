package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.room.entity.categoryList
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import com.wonddak.mtmanger.ui.view.dialog.CategoryDialog
import com.wonddak.mtmanger.ui.view.dialog.DeleteDialog
import kotlinx.coroutines.launch

@Composable
fun CategoryView(
    modifier: Modifier,
    list: List<categoryList>,
    update: (Int, String) -> Unit = { _, _ -> },
    delete: (Int) -> Unit = {},
    insert: (String) -> Unit = {},
) {
    var inputText by remember {
        mutableStateOf("")
    }

    var prevSize by remember { mutableStateOf(list.size) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    LaunchedEffect(list) {
        if (list.size > prevSize) {
            listState.scrollToItem(list.size - 1)
        }
        prevSize = list.size
    }
    Box(modifier.fillMaxSize().imePadding()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = OutlinedTextFieldDefaults.MinHeight + 10.dp),
            state = listState
        ) {
            items(list) { category ->
                var showDeleteDialog by remember {
                    mutableStateOf(false)
                }
                var showEditDialog by remember {
                    mutableStateOf(false)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, match1, RoundedCornerShape(3.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefaultText(
                        text = category.name,
                        color = match2,
                    )
                    Row {
                        IconButton(
                            onClick = {
                                focusManager.clearFocus()
                                showEditDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                null,
                                tint = match2
                            )
                        }
                        IconButton(
                            onClick = {
                                focusManager.clearFocus()
                                showDeleteDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                null,
                                tint = match2
                            )
                        }
                    }

                    if (showDeleteDialog) {
                        DeleteDialog(
                            onDismiss = { showDeleteDialog = false },
                            onDelete = {
                                delete(category.id!!)
                                showDeleteDialog = false
                            }
                        )
                    }
                    if (showEditDialog) {
                        CategoryDialog(
                            category = category,
                            onDismiss = { showEditDialog = false },
                            onAdd = { name ->
                                update(category.id!!, name)
                                showEditDialog = false
                            }
                        )
                    }
                }
            }
        }
        DialogTextField(
            value = inputText,
            color = match2,
            placeHolder = "카테고리 입력",
            label = "입력",
            change = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (inputText.isNotEmpty()) {
                        scope.launch {
                            insert(inputText)
                            listState.scrollToItem(list.size - 1)
                            inputText = ""
                        }
                    }
                }
            )
        )
    }
}