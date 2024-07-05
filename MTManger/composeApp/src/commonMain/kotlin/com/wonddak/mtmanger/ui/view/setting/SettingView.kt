package com.wonddak.mtmanger.ui.view.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.noRippleClickable
import com.wonddak.mtmanger.ui.theme.match1
import com.wonddak.mtmanger.viewModel.MTViewModel
import com.wonddak.mtmanger.viewModel.PayViewModel
import org.koin.compose.koinInject

@Composable
fun SettingView(
    mtViewModel: MTViewModel = koinInject(),
    payViewModel: PayViewModel = koinInject()
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(match1)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .noRippleClickable(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryView(
            Modifier
                .fillMaxSize(),
            mtViewModel.settingCategoryList,
            update = {id,input ->
                mtViewModel.updateCategory(id,input)
            },
            delete = {
                mtViewModel.deleteCategoryById(it)
            },
            insert = {
                mtViewModel.insertCategory(it)
            }
        )

//        Column {
//            val removeAdStatus = payViewModel.removeAdStatus
//            SettingAdFooter(removeAdStatus)
//            OutlinedButton(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {
//                    AppUtil.sendMail()
//                },
//                border = BorderStroke(2.dp, match2),
//            ) {
//                BuyGoodItemText(text = "문의하기")
//            }
//        }
    }
}
