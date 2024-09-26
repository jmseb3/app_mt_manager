package com.wonddak.mtmanger.model

import com.wonddak.mtmanger.core.Const
import mtmanger.composeapp.generated.resources.Res
import mtmanger.composeapp.generated.resources.ic_baseline_attach_money_24
import mtmanger.composeapp.generated.resources.ic_baseline_book_24
import mtmanger.composeapp.generated.resources.ic_baseline_home_24
import mtmanger.composeapp.generated.resources.ic_baseline_person_outline_24
import mtmanger.composeapp.generated.resources.ic_baseline_settings_24
import org.jetbrains.compose.resources.DrawableResource

sealed class NavItem(
    val screenRoute: String,
    val icon: DrawableResource,
    val title: String
) {
    data object Main : NavItem(
        Const.MAIN,
        Res.drawable.ic_baseline_home_24,
        "메인"
    )

    data object Person : NavItem(
        Const.PERSON,
        Res.drawable.ic_baseline_person_outline_24,
        "참가자 명단"
    )

    data object Buy : NavItem(
        Const.BUY,
        Res.drawable.ic_baseline_attach_money_24,
        "구매 내역"
    )

    data object Plan : NavItem(
        Const.PLAN,
        Res.drawable.ic_baseline_book_24,
        "계획"
    )

    data object Setting : NavItem(
        Const.SETTING_HOME,
        Res.drawable.ic_baseline_settings_24,
        "설정"
    )
}