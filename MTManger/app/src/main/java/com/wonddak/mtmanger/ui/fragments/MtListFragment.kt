package com.wonddak.mtmanger.ui.fragments

import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
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
    private var adapter: MtListRecyclerAdapter = MtListRecyclerAdapter()

    @Inject
    lateinit var preferences: SharedPreferences

    override fun initBinding() {
        val editor = preferences.edit()
        mtViewModel.getMtTotalLIst().observe(this@MtListFragment) {
            adapter.insertItems(it)
        }

        binding.apply {
            mtlistrecycler.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            binding.mtlistrecycler.adapter = adapter
            adapter.setCallBack(object : MtListRecyclerAdapter.MtListItemCallback {
                override fun itemClick(mtData: MtData) {
                    showToast(mtData.mtTitle +"MT로 변경했어요")

                    mtViewModel.setMtId(mtData.mtDataId!!)
                    editor.putInt("id", mtData.mtDataId!!)
                    editor.commit()

                    parentFragmentManager.beginTransaction()
                        .remove(this@MtListFragment)
                        .commitNow()

                    mtViewModel.setBottomMenuStatus(true)

                }
            })
        }
    }
}