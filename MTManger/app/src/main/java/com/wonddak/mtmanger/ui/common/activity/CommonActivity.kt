package com.wonddak.foodrecipe.ui.common.activity

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class CommonActivity :AppCompatActivity() {

    protected val mMainScope = CoroutineScope(Dispatchers.Main)
    protected val mBgDispatcher: CoroutineDispatcher = Dispatchers.IO

}