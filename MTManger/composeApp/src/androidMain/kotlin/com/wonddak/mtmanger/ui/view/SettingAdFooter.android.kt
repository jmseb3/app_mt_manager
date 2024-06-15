package com.wonddak.mtmanger.ui.view

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wonddak.mtmanger.ui.theme.match2
import com.wonddak.mtmanger.ui.view.home.buy.BuyGoodItemText
import com.wonddak.mtmanger.util.BillingModule
import org.koin.compose.koinInject

@Composable
internal actual fun SettingAdFooter(removeAd:Boolean) {
    val billingModule  : BillingModule = koinInject()
    val activity = (LocalContext.current as Activity)
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            billingModule.purchaseRemoveAdRequest(activity)
        },
        border = BorderStroke(2.dp, match2),
        enabled = !removeAd
    ) {
        BuyGoodItemText(text = if (removeAd) "광고 제거 여부:O" else "광고 제거")
    }
}