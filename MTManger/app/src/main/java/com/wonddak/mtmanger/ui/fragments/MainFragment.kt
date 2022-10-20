package com.wonddak.mtmanger.ui.fragments

import android.content.SharedPreferences
import android.view.View
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentMainBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.MtData
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.dialog.MtDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseDataBindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    @Inject
    lateinit var prefs: SharedPreferences

    override fun initBinding() {
        val editor = prefs.edit()
        val dec = DecimalFormat("#,###")

        mtViewModel.mainMtId.observe(viewLifecycleOwner) { mainmtid ->
            if (mainmtid == 0) {
                binding.mtdatastart.visibility = View.VISIBLE
                binding.mtdatafield.visibility = View.INVISIBLE
            } else {
                binding.mtdatastart.visibility = View.INVISIBLE
                binding.mtdatafield.visibility = View.VISIBLE

                mMainScope.launch {
                    withContext(mBgDispatcher) {
                        mtViewModel.getMtDataById().apply {
                            binding.mttitle.text = this.mtTitle + " MT"
                            binding.mttotoalfee.text = dec.format(this.fee).toString()
                            binding.mtstart.text = this.mtStart
                            binding.mtend.text = this.mtEnd
                        }
                    }
                }
            }
        }

        mtViewModel.getPerson().observe(viewLifecycleOwner) {
            binding.mttotalperson.text = it.size.toString()
        }

        binding.mtdataadd.setOnClickListener {
            val mtDialog = MtDialog.newInstance()

            mtDialog.setMtDialogCallback(object : MtDialog.MtDialogOkCallBack {
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

                        editor.putInt("id", newId.toInt())
                        editor.apply()
                        mtViewModel.setMtId(newId.toInt())
                    }
                }
            })
            mtDialog.show(
                parentFragmentManager, "mtdialog"
            )
        }

        binding.newmt.setOnClickListener {
            val mtDialog = MtDialog.newInstance()

            mtDialog.setMtDialogCallback(object : MtDialog.MtDialogOkCallBack {
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

                        editor.putInt("id", newId.toInt())
                        editor.apply()
                        mtViewModel.setMtId(newId.toInt())
                    }
                }
            })
            mtDialog.show(
                parentFragmentManager, "mtdialog"
            )
        }

        binding.mtedit.setOnClickListener {
            mMainScope.launch {
                val mtData: MtData
                withContext(mBgDispatcher) {
                    mtData = mtViewModel.getMtDataById()
                }
                val mtDialog = MtDialog.newInstance(
                    title = mtData.mtTitle,
                    fee = mtData.fee,
                    start = mtData.mtStart,
                    end = mtData.mtEnd
                )
                mtDialog.setMtDialogCallback(object : MtDialog.MtDialogOkCallBack {
                    override fun onClick(
                        title: String,
                        fee: Int,
                        startDate: String,
                        endDate: String
                    ) {
                        mMainScope.launch {
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
                            showToast("수정이 완료되었습니다.")

                        }
                    }
                })
                mtDialog.show(
                    parentFragmentManager, "mtdialog"
                )


            }

        }

        binding.mtlists.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.settingfrag, MtListFragment())
                .commit()

            mtViewModel.setBottomMenuStatus(false)
        }
    }
}
