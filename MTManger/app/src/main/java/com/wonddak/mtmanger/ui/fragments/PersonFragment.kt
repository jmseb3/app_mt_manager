package com.wonddak.mtmanger.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentPersonBinding
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.room.Person
import com.wonddak.mtmanger.ui.adapter.PersonRecyclerAdapter
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import com.wonddak.mtmanger.ui.dialog.PersonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonFragment : BaseDataBindingFragment<FragmentPersonBinding>(R.layout.fragment_person) {
    private lateinit var adapter: PersonRecyclerAdapter
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var contentLauncher: ActivityResultLauncher<Intent>

    private fun getContentInfo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        contentLauncher.launch(intent)
    }

    override fun initBinding() {
        val personAddCallback = object : PersonDialog.PersonDialogOkCallBack {
            override fun addPersonFromPhoneData() {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    getContentInfo()
                }else {
                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }

            override fun onClick(name: String, fee: String, number: String) {
                mtViewModel.insertPerson(
                    name,
                    fee.toInt(),
                    number
                )
                showToast("${name}님을 추가했습니다.")

            }
        }
        val personDialogEditCallback = object : PersonDialog.PersonDialogOkCallBack {
            override fun onClick(name: String, fee: String, number: String) {
                mtViewModel.insertPerson(
                    name,
                    fee.toInt(),
                    number
                )
                showToast("정보를 수정했습니다.")

            }
        }

        contentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
                val uriString = data.data!!.dataString

                val uri = Uri.parse(uriString)

                val cursor = requireContext().contentResolver.query(
                    uri,
                    arrayOf<String>(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ), null, null, null
                )

                cursor!!.moveToFirst()
                val name = cursor.getString(0) //0은 이름을 얻어옵니다.
                val number = cursor.getString(1) //1은 번호를 받아옵니다.
                cursor.close()
                PersonDialog.newInstance(
                    personAddCallback,
                    name, number
                ).show(
                    parentFragmentManager, null
                )
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Toast.makeText(requireContext(), "연락처 권한을 승인했습니다..", Toast.LENGTH_LONG).show();
                    getContentInfo()
                } else {
                    Toast.makeText(
                        requireContext(), "연락처 권한이 필요합니다.\n" +
                                " 현재 거부상태입니다.", Toast.LENGTH_LONG
                    ).show();
                }
            }

        binding.mtViewModel = mtViewModel
        lifecycleScope.launchWhenCreated {
            mtViewModel.mainMtId.collect { mainmtid ->
                if (mainmtid == 0) {
                    binding.mtdatastart.visibility = View.VISIBLE
                } else {
                    binding.mtdatastart.visibility = View.INVISIBLE
                }
            }
        }


        val listener = object : PersonRecyclerAdapter.PersonListItemCallback {
            override fun itemClick(item: Person) {
                PersonDialog.newInstance(personDialogEditCallback).show(
                    parentFragmentManager, null
                )

            }

            override fun itemLongClick(item: Person) {
                DeleteDialog.newInstance(object : DeleteDialog.DeleteDialogCallback {
                    override fun onclick() {
                        mtViewModel.deletePerson(item.personId!!)
                    }
                }).show(
                    parentFragmentManager,null
                )

            }

            override fun phoneNumberClick(item: Person) {
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phoneNumber)))
            }
        }
        adapter = PersonRecyclerAdapter(listener)

        binding.personrecycler.apply {
            adapter = this@PersonFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        lifecycleScope.launchWhenCreated {
            var sum: Int
            mtViewModel.nowMtDataList.collect {
                if (it is Resource.Success) {
                    if (it.data == null) {
                        return@collect
                    }
                    it.data.personList.let { personList ->
                        sum = 0
                        personList.forEach { person ->
                            sum += person.paymentFee
                        }
                        binding.totalfee.text = sum.toString() + "원"
                        binding.totalcount.text = personList.size.toString() + "명"
                        adapter.insertItems(personList)
                    }

                }
            }
        }

        binding.maintitle.setOnClickListener {
            mtViewModel.togglePersonFoldStatus()
        }

        binding.btnaddperson.setOnClickListener {
            PersonDialog.newInstance(personAddCallback).show(
                parentFragmentManager, null
            )
        }

        binding.btnclearperson.setOnClickListener {
            DeleteDialog.newInstance(object : DeleteDialog.DeleteDialogCallback {
                override fun onclick() {
                    mtViewModel.clearPersonData()
                }
            }, "정말 초기화 하시겠습니까?").show(
                parentFragmentManager, null
            )

        }
    }
}