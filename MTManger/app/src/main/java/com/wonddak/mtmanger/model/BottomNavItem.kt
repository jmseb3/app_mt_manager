package com.wonddak.mtmanger.model

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const

sealed class BottomNavItem(
    val screenRoute: String,
    val icon: Int,
    val title: String
) {
    object Main : BottomNavItem(
        Const.Main,
        R.drawable.ic_baseline_home_24,
        "메인"
    )

    object Person : BottomNavItem(
        Const.Person,
        R.drawable.ic_baseline_person_outline_24,
        "참가자 명단"
    )

    object Buy : BottomNavItem(
        Const.Buy,
        R.drawable.ic_baseline_attach_money_24,
        "구매 내역"
    )

    object Plan : BottomNavItem(
        Const.Plan,
        R.drawable.ic_baseline_book_24,
        "계획"
    )
}