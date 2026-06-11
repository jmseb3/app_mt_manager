package com.wonddak.mtmanger.repository

import com.wonddak.mtmanger.core.Const
import com.wonddak.mtmanger.room.entity.BuyGood
import com.wonddak.mtmanger.room.entity.MtData
import com.wonddak.mtmanger.room.entity.MtDataList
import com.wonddak.mtmanger.room.entity.Person
import com.wonddak.mtmanger.room.entity.Plan
import com.wonddak.mtmanger.room.entity.categoryList

object ScreenshotMockData {
    private const val MAIN_MT_ID = Const.SCREENSHOT_MOCK_MT_ID
    private const val SUB_MT_ID = 2

    val mtData = MtData(
        mtDataId = MAIN_MT_ID,
        mtTitle = "가평 여름 MT",
        fee = 80000,
        mtStart = "2026.07.18",
        mtEnd = "2026.07.20"
    )

    val mtDataList = MtDataList(
        mtData = mtData,
        personList = listOf(
            Person(1, MAIN_MT_ID, "김민준", "010-1234-5678", 80000),
            Person(2, MAIN_MT_ID, "이서연", "010-2345-6789", 80000),
            Person(3, MAIN_MT_ID, "박지호", "010-3456-7890", 80000),
            Person(4, MAIN_MT_ID, "최하은", "010-4567-8901", 80000),
            Person(5, MAIN_MT_ID, "정도윤", "010-5678-9012", 80000)
        ),
        buyGoodList = listOf(
            BuyGood(1, MAIN_MT_ID, "식재료", "바비큐 세트", 1, 145000),
            BuyGood(2, MAIN_MT_ID, "주류", "맥주/음료", 1, 68000),
            BuyGood(3, MAIN_MT_ID, "차비", "렌터카", 1, 120000),
            BuyGood(4, MAIN_MT_ID, "마트", "간식", 1, 42000)
        ),
        planList = listOf(
            Plan(
                planId = 1,
                mtId = MAIN_MT_ID,
                nowDay = "2026.07.18",
                nowPlanTitle = "마트 장보기",
                simpleText = "숙소 입실 전 바비큐 재료와 음료 구매",
                link = "https://maps.google.com"
            ),
            Plan(
                planId = 2,
                mtId = MAIN_MT_ID,
                nowDay = "2026.07.19",
                nowPlanTitle = "계곡 물놀이",
                simpleText = "점심 식사 후 단체 사진 촬영",
                link = ""
            ),
            Plan(
                planId = 3,
                mtId = MAIN_MT_ID,
                nowDay = "2026.07.20",
                nowPlanTitle = "정산 및 체크아웃",
                simpleText = "숙소 정리 후 최종 정산 공유",
                link = ""
            )
        )
    )

    val mtTotalList = listOf(
        mtData,
        MtData(
            mtDataId = SUB_MT_ID,
            mtTitle = "강릉 겨울 MT",
            fee = 90000,
            mtStart = "2026.12.12",
            mtEnd = "2026.12.14"
        )
    )

    val categoryList = listOf(
        categoryList(1, "식재료"),
        categoryList(2, "주류"),
        categoryList(3, "차비"),
        categoryList(4, "식사"),
        categoryList(5, "마트")
    )
}
