package com.wonddak.mtmanger.ui.common.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wonddak.mtmanger.viewModel.MTViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

open class CommonFragment :Fragment() {

    protected val mMainScope = CoroutineScope(Dispatchers.Main)
    protected val mBgDispatcher: CoroutineDispatcher = Dispatchers.IO
    val mtViewModel : MTViewModel by activityViewModels()

    fun showToast(string: String){
        Toast.makeText(requireContext(),string,Toast.LENGTH_SHORT).show()
    }


}