package com.wonddak.mtmanger.ui.fragments

import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Insert
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentMtListBinding
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.adapter.MtListRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MtListFragment : BaseDataBindingFragment<FragmentMtListBinding>(R.layout.fragment_mt_list) {
    private lateinit var adapter: MtListRecyclerAdapter

    override fun initBinding() {
        lifecycleScope.launchWhenCreated {
            mtViewModel.getMtTotalLIst().observe(this@MtListFragment) {
                adapter.insertItems(it)
            }
        }

        adapter = MtListRecyclerAdapter(object : MtListRecyclerAdapter.MtListItemCallback {
            override fun itemClick(mtData: MtData) {
                showToast(mtData.mtTitle + "MT로 변경했어요")

                mtViewModel.setMtId(mtData.mtDataId!!)
                parentFragmentManager.beginTransaction()
                    .remove(this@MtListFragment)
                    .commitNow()

                mtViewModel.setBottomMenuStatus(true)
                mtViewModel.setTopButtonStatus(true)

            }
        })

        binding.apply {
            mtlistrecycler.apply {
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    )
                )
                adapter = this@MtListFragment.adapter
            }
        }
    }
}