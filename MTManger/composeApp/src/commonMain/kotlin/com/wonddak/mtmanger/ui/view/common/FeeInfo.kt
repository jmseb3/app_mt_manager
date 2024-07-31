package com.wonddak.mtmanger.ui.view.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.wonddak.mtmanger.toPriceString
import com.wonddak.mtmanger.ui.view.home.buy.BuyGoodItemText

@Composable
fun FeeInfo(
    modifier: Modifier = Modifier,
    text: String,
    fee: Int,
    feeIndex: String = "원"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        BuyGoodItemText(
            text = text,
            fontSize = 18.sp
        )
        BuyGoodItemText(
            text = fee.toPriceString(feeIndex)
        )
    }
}