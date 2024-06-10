package com.wonddak.mtmanger.model

import com.wonddak.mtmanger.core.Const
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_baseline_attach_money_24
import mtmanger.composeapp.generated.resources.ic_baseline_book_24
import mtmanger.composeapp.generated.resources.ic_baseline_home_24
import mtmanger.composeapp.generated.resources.ic_baseline_person_outline_24
import org.jetbrains.compose.resources.DrawableResource

sealed class BottomNavItem(
    val screenRoute: String,
    val icon: DrawableResource,
    val title: String
) {
    data object Main : BottomNavItem(
        Const.MAIN,
        Res.drawable.ic_baseline_home_24,
        "메인"
    )

    data object Person : BottomNavItem(
        Const.PERSON,
        Res.drawable.ic_baseline_person_outline_24,
        "참가자 명단"
    )

    data object Buy : BottomNavItem(
        Const.BUY,
        Res.drawable.ic_baseline_attach_money_24,
        "구매 내역"
    )

    data object Plan : BottomNavItem(
        Const.PLAN,
        Res.drawable.ic_baseline_book_24,
        "계획"
    )
}