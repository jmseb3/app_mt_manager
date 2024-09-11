package com.wonddak.mtmanger.core

object Const {

    //depth 1
    const val HOME = "host_home"
    const val MAIN = "nav_main"
    const val PERSON = "nav_person"
    const val BUY = "nav_buy"
    const val PLAN = "nav_plan"

    //depth2 from plan
    const val NEW_PLAN = "${PLAN}_new"
    const val EDIT_PLAN = "${PLAN}_edit"

    const val PLAN_ARG_START = "start"
    const val PLAN_ARG_END = "end"
    const val PLAN_ARG_ID = "id"

    fun navigationNewPlan(start:String,end:String) :String {
        return "$NEW_PLAN?start=$start&end=$end"
    }
    fun navigationNewPlanRout() = "$NEW_PLAN?$PLAN_ARG_START={$PLAN_ARG_START}&$PLAN_ARG_END={$PLAN_ARG_END}"
    fun navigationEditPlan(start:String,end:String,id:Int) :String {
        return "$EDIT_PLAN?start=$start&end=$end&id=$id"
    }
    fun navigationEditPlanRout() = "$EDIT_PLAN?$PLAN_ARG_START={$PLAN_ARG_START}&$PLAN_ARG_END={$PLAN_ARG_END}&$PLAN_ARG_ID={$PLAN_ARG_ID}"


    //depth2 from main
    const val SETTING = "host_setting"
    const val SETTING_HOME = "nav_setting"

    //depth3 form setting
    const val CATEGORY = "nav_category"

    //depth2 from main
    const val MT_LIST = "nav_mt_list"
    const val MT_ADJUSTMENT = "nav_mt_adjustment"

    //depth1 if start
    const val ATT = "nav_Att"

}