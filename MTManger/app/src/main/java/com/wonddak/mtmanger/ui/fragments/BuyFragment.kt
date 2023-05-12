package com.wonddak.mtmanger.ui.fragments

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentBuyBinding
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.view.BuyView
import com.wonddak.mtmanger.ui.view.common.NoDataBase

class BuyFragment : BaseDataBindingFragment<FragmentBuyBinding>(R.layout.fragment_buy) {
    override fun initBinding() {
        binding.buyView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                BuyView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }

}