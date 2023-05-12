package com.wonddak.mtmanger.ui.fragments

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentPlanBinding
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.view.PlanView
import com.wonddak.mtmanger.ui.view.common.NoDataBase


class PlanFragment : BaseDataBindingFragment<FragmentPlanBinding>(R.layout.fragment_plan) {
    override fun initBinding() {
        binding.planView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                PlanView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }

}