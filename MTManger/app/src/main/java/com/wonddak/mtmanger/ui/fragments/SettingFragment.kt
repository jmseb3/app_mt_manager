package com.wonddak.mtmanger.ui.fragments

import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentSettingBinding
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.adapter.SettingRecyclerAdapter
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.dialog.CategoryDialog
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : BaseDataBindingFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private lateinit var adapter: SettingRecyclerAdapter

    @Inject
    lateinit var billingModule: BillingModule

    override fun initBinding() {
        binding.mtViewModel = mtViewModel
        lifecycleScope.launchWhenCreated {
            mtViewModel.removeAdStatus.collect {
                Log.i("Billing JWH", "setting status $it")

                if (it) {
                    binding.buyNoAdd.text = "광고 제거 여부:O"
                    binding.buyNoAdd.isEnabled = false
                } else {
                    binding.buyNoAdd.text = "광고 제거"
                }
            }
        }

        binding.buyNoAdd.setOnClickListener {
            billingModule.getPay(requireActivity())
        }

        val listener = object : SettingRecyclerAdapter.SettingCallback {
            override fun onClick(categoryList: categoryList) {
                CategoryDialog.newInstance(categoryList.name,
                    object : CategoryDialog.CategoryDialogCallback {
                        override fun onClick(item: String) {
                            mtViewModel.updateCategory(categoryList.id!!, item)
                        }
                    }).show(parentFragmentManager, null)
            }

            override fun onLongClick(item: categoryList) {
                DeleteDialog.newInstance(object : DeleteDialog.DeleteDialogCallback {
                    override fun onclick() {
                        mtViewModel.deleteCategoryById(item.id!!)
                    }
                }
                ).show(parentFragmentManager, null)
            }
        }
        adapter = SettingRecyclerAdapter(listener)
        binding.categoryrecycler.adapter = adapter
        lifecycleScope.launchWhenCreated {
            mtViewModel.settingCategoryList.collect {
                adapter.insertItems(it)
            }
        }

        binding.addCategory.setOnClickListener {
            binding.addCategoryText.text.toString().let { text ->
                if (text.isEmpty()) {
                    showToast("항목을 입력해주세요.")
                } else {
                    mtViewModel.insertCategory(text)
                    showToast("항목을 추가햇습니다.")
                    binding.addCategoryText.setText("")
                }

            }
        }

        binding.sendemail.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("jmseb2@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "<MT매니저 관련 문의입니다.>")
                putExtra(Intent.EXTRA_TEXT, "내용:")
            }.let { startActivity(it) }
        }

        binding.titleline.setOnClickListener {
            mtViewModel.toggleCategoryFoldStatus()
        }
    }
}
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val db = AppDatabase.getInstance(requireContext())
//
//        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
//        val editor = prefs.edit()
//
//
//        var check = false
//        binding.titleline.setOnClickListener {
//            if (check == false) {
//                check = true
//                binding.showOrNotshow.visibility = View.VISIBLE
//                Glide.with(requireContext())
//                    .load(R.drawable.ic_baseline_arrow_drop_down_24)
//                    .into(binding.arrow)
//            } else {
//                check = false
//                binding.showOrNotshow.visibility = View.GONE
//                Glide.with(requireContext())
//                    .load(R.drawable.ic_baseline_arrow_drop_up_24)
//                    .into(binding.arrow)
//            }
//
//
//        }
//
//        return (binding.root)
//    }