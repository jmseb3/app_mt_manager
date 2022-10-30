package com.wonddak.mtmanger.ui.fragments

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentMainBinding
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.dialog.MtDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseDataBindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun initBinding() {
        val dec = DecimalFormat("#,###")
        binding.viemodel = mtViewModel

        lifecycleScope.launchWhenCreated {
            mtViewModel.mainMtId.collect { mainmtid ->
                if (mainmtid == 0) {
                    binding.mtdatastart.visibility = View.VISIBLE
                    binding.mtdatafield.visibility = View.INVISIBLE
                } else {
                    binding.mtdatastart.visibility = View.INVISIBLE
                    binding.mtdatafield.visibility = View.VISIBLE

                }
            }
        }

        lifecycleScope.launchWhenCreated {
            mtViewModel.nowMtDataList.collect {
                if (it is Resource.Success) {
                    if (it.data == null) {
                        return@collect
                    }
                    it.data.mtdata.let { mtData ->
                        binding.mttitle.text = mtData.mtTitle + " MT"
                        binding.mttotoalfee.text = dec.format(mtData.fee).toString()
                        binding.mtstart.text = mtData.mtStart
                        binding.mtend.text = mtData.mtEnd
                    }

                    it.data.personList.let {
                        binding.mttotalperson.text = it.size.toString()
                    }
                }
            }
        }

        val mtAddCallBack = object : MtDialog.MtDialogOkCallBack {
            override fun onClick(
                title: String,
                fee: Int,
                startDate: String,
                endDate: String
            ) {
                mMainScope.launch {
                    val newId =
                        withContext(mBgDispatcher) {
                            mtViewModel.insertMtData(
                                MtData(
                                    null,
                                    title,
                                    fee,
                                    startDate,
                                    endDate

                                )
                            )
                        }

                    mtViewModel.setMtId(newId.toInt())
                }
            }
        }
        val newMtCallback = object : MtDialog.MtDialogOkCallBack {
            override fun onClick(
                title: String,
                fee: Int,
                startDate: String,
                endDate: String
            ) {
                mMainScope.launch {
                    val newId =
                        withContext(mBgDispatcher) {
                            mtViewModel.insertMtData(
                                MtData(
                                    null,
                                    title,
                                    fee,
                                    startDate,
                                    endDate

                                )
                            )
                        }

                    mtViewModel.setMtId(newId.toInt())
                }
            }
        }
        val mtEditCallBack = object : MtDialog.MtDialogOkCallBack {
            override fun onClick(
                title: String,
                fee: Int,
                startDate: String,
                endDate: String
            ) {
                mMainScope.launch {
                    withContext(mBgDispatcher) {
                        mtViewModel.updateMtData(
                            title,
                            fee,
                            startDate,
                            endDate
                        )
                    }
                    showToast("수정이 완료되었습니다.")

                }
            }
        }

        binding.mtdataadd.setOnClickListener {
            MtDialog.newInstance(mtAddCallBack).show(
                parentFragmentManager, "mtdialog"
            )
        }

        binding.newmt.setOnClickListener {
            MtDialog.newInstance(newMtCallback).show(
                parentFragmentManager, "mtdialog"
            )
        }

        binding.mtedit.setOnClickListener {
            mMainScope.launch {
                mtViewModel.nowMtDataList.value.let {
                    if (it is Resource.Success) {
                        it.data?.mtdata?.let { mtData ->

                            MtDialog.newInstance(
                                mtEditCallBack,
                                title = mtData.mtTitle,
                                fee = mtData.fee,
                                start = mtData.mtStart,
                                end = mtData.mtEnd
                            ).show(
                                parentFragmentManager, "mtdialog"
                            )
                        }
                    }
                }
            }
        }

        binding.mtlists.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.settingfrag, MtListFragment())
                .commit()

            mtViewModel.setBottomMenuStatus(false)
            mtViewModel.setTopButtonStatus(false)
        }
    }
}
