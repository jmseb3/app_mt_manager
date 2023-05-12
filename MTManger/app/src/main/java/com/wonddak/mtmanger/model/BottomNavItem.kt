package com.wonddak.mtmanger.model

import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.core.Const

sealed class BottomNavItem(
    val screenRoute: String,
    val icon: Int
) {
    object Main : BottomNavItem(
        Const.Main,
        R.drawable.ic_baseline_home_24
    )

    object Person : BottomNavItem(
        Const.Person,
        R.drawable.ic_baseline_person_outline_24
    )

    object Buy : BottomNavItem(
        Const.Buy,
        R.drawable.ic_baseline_attach_money_24
    )

    object Plan : BottomNavItem(
        Const.Plan,
        R.drawable.ic_baseline_book_24
    )
}