package com.wonddak.mtmanger.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentPlanBinding
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.adapter.PlanRecyclerAdapter
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.dialog.DatePicker
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.dialog.PlanDialog
import java.text.SimpleDateFormat


class PlanFragment : BaseDataBindingFragment<FragmentPlanBinding>(R.layout.fragment_plan) {

    private lateinit var adapter: PlanRecyclerAdapter

    private var focusId :Int? = null
    override fun initBinding() {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                requireContext().contentResolver.takePersistableUriPermission(uri, flag)
                focusId?.let {
                    mtViewModel.updatePlanImgSrc(
                        it,
                        uri.toString()
                    )
                    focusId = null
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
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
        val listener = object : PlanRecyclerAdapter.PlanListItemCallback {
            override fun itemClick(item: Plan) {
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
            }

            override fun itemLongClick(item: Plan) {
                DeleteDialog.newInstance(
                    object : DeleteDialog.DeleteDialogCallback {
                        override fun onclick() {
                            mtViewModel.deletePlanById(item.planId!!)
                        }
                    }
                ).show(
                    parentFragmentManager, null
                )
            }

            override fun itemAddPhoto(item: Plan) {
                focusId = item.planId
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            override fun itemImgLongClick(item: Plan) {
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
        }
        adapter = PlanRecyclerAdapter(listener)
        binding.planRecycler.adapter = adapter

        lifecycleScope.launchWhenCreated {
            mtViewModel.nowMtDataList.collect {
                if (it is Resource.Success) {
                    it.data?.planList?.let { planList ->
                        adapter.insertItems(planList)
                        binding.planRecycler.scrollToPosition(planList.size)
                    }

                }
            }
        }
    }

}