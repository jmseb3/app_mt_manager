package com.wonddak.mtmanger.ui.fragments

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentComposeBinding
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.view.BuyView
import com.wonddak.mtmanger.ui.view.PersonView
import com.wonddak.mtmanger.ui.view.PlanView
import com.wonddak.mtmanger.ui.view.SettingView
import com.wonddak.mtmanger.ui.view.common.NoDataBase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BuyFragment : BaseDataBindingFragment<FragmentComposeBinding>(R.layout.fragment_compose) {
    override fun initBinding() {
        binding.composeView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                BuyView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }
}

@AndroidEntryPoint
class PersonFragment : BaseDataBindingFragment<FragmentComposeBinding>(R.layout.fragment_compose) {
    override fun initBinding() {
        binding.composeView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                PersonView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }
}

@AndroidEntryPoint
class PlanFragment : BaseDataBindingFragment<FragmentComposeBinding>(R.layout.fragment_compose) {
    override fun initBinding() {
        binding.composeView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                PlanView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }

}

@AndroidEntryPoint
class SettingFragment : BaseDataBindingFragment<FragmentComposeBinding>(R.layout.fragment_compose) {
    @Inject
    lateinit var billingModule: BillingModule
    override fun initBinding() {
        binding.composeView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                SettingView(
                    mtViewModel = mtViewModel,
                    billingModule = billingModule
                )
            }
        }
    }

}
