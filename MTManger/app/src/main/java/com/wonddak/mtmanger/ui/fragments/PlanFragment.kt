package com.wonddak.mtmanger.ui.fragments

import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentPlanBinding
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.dialog.DatePicker
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.dialog.PlanDialog
import com.wonddak.mtmanger.ui.view.plan.PlanListView
import java.text.SimpleDateFormat


class PlanFragment : BaseDataBindingFragment<FragmentPlanBinding>(R.layout.fragment_plan) {
    override fun initBinding() {

        lifecycleScope.launchWhenCreated {
            mtViewModel.mainMtId.collect { mainmtid ->
                if (mainmtid == 0) {
                    binding.mtdatastart.visibility = View.VISIBLE
                } else {
                    binding.mtdatastart.visibility = View.INVISIBLE
                }
            }
        }

        binding.planAddBtn.setOnClickListener {
            mtViewModel.addEmptyPlan()
        }

        binding.planRecycler.setContent {
            PlanListView(
                mtViewModel = mtViewModel,
                itemClick = {item ->
                    PlanDialog.newInstance(
                        object : PlanDialog.PlanDialogCallback {
                            override fun onClick(title: String, day: String, text: String) {
                                mtViewModel.updatePlanById(
                                    item.planId!!,
                                    day,
                                    title,
                                    text
                                )
                            }

                            override fun dateBtnClick(editText: EditText) {
                                DatePicker.show(requireContext()) { _, year, month, day ->
                                    val transFormat = SimpleDateFormat("yyyy.MM.dd")

                                    mtViewModel.nowMtDataList.value.let {
                                        if (it is Resource.Success) {
                                            it.data?.mtdata?.let { mtData ->
                                                val tempDate = "${year}.${month + 1}.${day}"
                                                val startDate = transFormat.parse(mtData.mtStart)
                                                val endDate = transFormat.parse(mtData.mtEnd)
                                                val nowDate = transFormat.parse(tempDate)

                                                if (nowDate in startDate..endDate) {
                                                    editText.setText(tempDate)
                                                } else {
                                                    showToast(
                                                        String.format(
                                                            getString(R.string.data_dialog_error),
                                                            startDate,
                                                            endDate
                                                        )
                                                    )
                                                }

                                            }

                                        }
                                    }

                                }
                            }
                        },
                        item.nowplantitle,
                        item.nowday,
                        item.simpletext
                    ).show(
                        parentFragmentManager, null
                    )
                },
                itemLongClick = {item ->
                    DeleteDialog.newInstance(
                        object : DeleteDialog.DeleteDialogCallback {
                            override fun onclick() {
                                mtViewModel.deletePlanById(item.planId!!)
                            }
                        }
                    ).show(
                        parentFragmentManager, null
                    )
                },
                imgLongClick = {item ->
                    DeleteDialog.newInstance(
                        object : DeleteDialog.DeleteDialogCallback {
                            override fun onclick() {
                                mtViewModel.updatePlanImgSrc(item.planId!!, "")
                            }
                        },
                        getString(R.string.dialog_delete_image)
                    ).show(
                        parentFragmentManager, null
                    )
                }
            )
        }

    }

}