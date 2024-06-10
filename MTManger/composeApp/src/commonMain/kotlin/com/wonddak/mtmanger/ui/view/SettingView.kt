package com.wonddak.mtmanger.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.ui.theme.maple
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.common.DefaultText
import com.wonddak.mtmanger.ui.view.common.DialogTextField
import com.wonddak.mtmanger.ui.view.dialog.CategoryDialog
import com.wonddak.mtmanger.ui.view.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.view.home.BuyGoodItemText
import com.wonddak.mtmanger.util.AppUtil
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.koinInject

@Composable
fun SettingView(
    mtViewModel: MTViewModel = koinInject(),
    payViewModel: PayViewModel = koinInject()
) {
    var focusId by remember { mutableIntStateOf(-1) }
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .noRippleClickable(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "카테고리 관리",
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

            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = match2
                )
            ) {
                val categoryList by mtViewModel.settingCategoryList.collectAsState(emptyList())
                var inputText by remember {
                    mutableStateOf("")
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(200.dp)
                        .padding(10.dp),
                    contentPadding = PaddingValues(5.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(categoryList) { category ->
                        var showDeleteDialog by remember {
                            mutableStateOf(false)
                        }
                        var showEditDialog by remember {
                            mutableStateOf(false)
                        }
                        Row(
                            modifier = Modifier
                                .border(1.dp, match1, RoundedCornerShape(3.dp))
                                .height(25.dp)
                                .noRippleClickable(
                                    onClick = {
                                        focusId = category.id!!
                                        showEditDialog = true
                                    },
                                    onLongClick = {
                                        focusId = category.id!!
                                        showDeleteDialog = true
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            DefaultText(
                                text = category.name,
                                color = match1,
                            )
                            if (showDeleteDialog) {
                                DeleteDialog(
                                    onDismiss = { showDeleteDialog = false },
                                    onDelete = {
                                        if (focusId >= 0) {
                                            mtViewModel.deleteCategoryById(focusId)
                                            showDeleteDialog = false
                                        }

                                    }
                                )
                            }
                            if (showEditDialog) {
                                CategoryDialog(
                                    category = category,
                                    onDismiss = { showEditDialog = false },
                                    onAdd = { name ->
                                        mtViewModel.updateCategory(category.id!!, name)
                                        showEditDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
                DialogTextField(
                    value = inputText,
                    placeHolder = "카테고리 입력",
                    label = "입력",
                    change = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (inputText.isNotEmpty()) {
                                mtViewModel.insertCategory(inputText)
                                inputText = ""
                            }
                        }
                    )
                )
            }
        }
        Column {
            val removeAdStatus = payViewModel.removeAdStatus
            SettingAdFooter(removeAdStatus)
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    AppUtil.sendMail()
                },
                border = BorderStroke(2.dp, match2),
            ) {
                BuyGoodItemText(text = "문의하기")
            }
        }
    }
}

@Composable
internal expect fun SettingAdFooter(removeAd: Boolean)