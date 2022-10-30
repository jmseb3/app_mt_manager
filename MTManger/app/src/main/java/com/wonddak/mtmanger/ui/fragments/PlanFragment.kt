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

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var contentLauncher: ActivityResultLauncher<Intent>

    private var focusId :Int? = null
    override fun initBinding() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedList: List<String> = result.filter {
                !it.value
            }.map {
                it.key
            }

            when {
                deniedList.isNotEmpty() -> {
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
                    }
                    map["DENIED"]?.let {
                        // request denied , request again
                        // 거부 한 번 했을경우 재요청
                    }
                    map["EXPLAINED"]?.let {
                        // request denied ,send to settings
                        // 거부 두 번 했을경우 설정
                    }
                    Toast.makeText(
                        requireContext(), "저장소 권한이 필요합니다.\n" +
                                " 현재 거부상태입니다.", Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "저장소 권한을 승인했습니다..", Toast.LENGTH_LONG).show();
                }
            }

        }

        contentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
            Log.i("JWH",data.toString())
            val uri = data.data?.dataString
            Log.i("JWH",uri.toString())
            focusId?.let {
                mtViewModel.updatePlanImgSrc(
                    it,
                    uri ?: ""
                )
                focusId = null
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
                val permissions =
                    listOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                var checkPermission = true

                permissions.forEach { permission ->
                    val isGranted = ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    )
                    if (isGranted != PackageManager.PERMISSION_GRANTED) {
                        checkPermission = false
                    }
                }
                if (checkPermission) {
                    val intent = Intent(Intent.ACTION_PICK)
                    focusId = item.planId
                    intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*"
                    )

                    contentLauncher.launch(intent)
                } else {
                    permissionLauncher.launch(permissions.toTypedArray())
                }

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