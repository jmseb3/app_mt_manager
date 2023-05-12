package com.wonddak.mtmanger.ui.fragments

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentPersonBinding
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.view.PersonView
import com.wonddak.mtmanger.ui.view.common.NoDataBase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : BaseDataBindingFragment<FragmentPersonBinding>(R.layout.fragment_person) {

    override fun initBinding() {
        binding.personView.setContent {
            val id by mtViewModel.mainMtId.collectAsState(0)
            NoDataBase(mainId = id) {
                PersonView(
                    mtViewModel = mtViewModel
                )
            }
        }
    }
}