package com.wonddak.foodrecipe.ui.common.fragment

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class CommonFragment :Fragment() {

    protected val mMainScope = CoroutineScope(Dispatchers.Main)
    protected val mBgDispatcher: CoroutineDispatcher = Dispatchers.IO

}