package com.wonddak.mtmanger.model

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const

sealed class BottomNavItem(
    val screenRoute: String,
    val icon: Int,
    val title: String
) {
    data object Main : BottomNavItem(
        Const.MAIN,
        R.drawable.ic_baseline_home_24,
        "메인"
    )

    data object Person : BottomNavItem(
        Const.PERSON,
        R.drawable.ic_baseline_person_outline_24,
        "참가자 명단"
    )

    data object Buy : BottomNavItem(
        Const.BUY,
        R.drawable.ic_baseline_attach_money_24,
        "구매 내역"
    )

    data object Plan : BottomNavItem(
        Const.PLAN,
        R.drawable.ic_baseline_book_24,
        "계획"
    )
}