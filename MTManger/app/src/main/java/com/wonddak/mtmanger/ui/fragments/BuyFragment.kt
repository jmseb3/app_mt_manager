package com.wonddak.mtmanger.ui.fragments

import com.wonddak.mtmanger.room.BuyGood
import com.wonddak.mtmanger.ui.dialog.BuyGoodDialog
import com.wonddak.mtmanger.ui.dialog.DeleteDialog
import java.text.DecimalFormat
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentBuyBinding
import com.wonddak.mtmanger.model.Resource
import com.wonddak.mtmanger.ui.adapter.BuyRecyclerAdapter
import com.wonddak.mtmanger.ui.common.fragment.BaseDataBindingFragment

class BuyFragment : BaseDataBindingFragment<FragmentBuyBinding>(R.layout.fragment_buy) {
    private lateinit var adapter: BuyRecyclerAdapter


    override fun initBinding() {
        binding.mtViewModel = mtViewModel

        val dec = DecimalFormat("#,###")

        lifecycleScope.launchWhenCreated {
            mtViewModel.mainMtId.collect { mainmtid ->
                if (mainmtid == 0) {
                    binding.mtdatastart.visibility = View.VISIBLE
                } else {
                    binding.mtdatastart.visibility = View.INVISIBLE
                }
            }
        }

        adapter = BuyRecyclerAdapter(object : BuyRecyclerAdapter.BuyListItemCallback {
            override fun itemClick(item: BuyGood) {
                DeleteDialog.newInstance(
                    object : DeleteDialog.DeleteDialogCallback {
                        override fun onclick() {
                            mtViewModel.deleteBuyGood(item.buyGoodId!!)
                        }
                    }
                ).show(
                    parentFragmentManager, null
                )

            }

            override fun itemLongClick(item: BuyGood) {
                BuyGoodDialog.newInstance(
                    object : BuyGoodDialog.BuyGoodDialogCallback {
                        override fun onClick(
                            category: String,
                            name: String,
                            count: String,
                            price: String
                        ) {
                            mtViewModel.insertBuyGood(
                                category,
                                name,
                                count.toInt(),
                                price.toInt(),
                                item.buyGoodId
                            )

                        }
                    },
                    mtViewModel.categoryList.value,
                    item.count.toString(),
                    item.price.toString(),
                    item.name
                ).show(parentFragmentManager, null)

            }
        })
        binding.buyrecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        binding.buyrecycler.adapter = adapter

        var fee: Int = 0
        var sum: Int
        var sum2: Int
        lifecycleScope.launchWhenCreated {
            mtViewModel.nowMtDataList.collect {
                if (it is Resource.Success) {
                    if (it.data == null) {
                        return@collect
                    }
                    fee = it.data.mtdata.fee

                    sum = 0
                    adapter.insertItems(it.data.buyGoodList)
                    it.data.buyGoodList.forEach { buyGood ->
                        sum += (buyGood.price * buyGood.count)
                    }
                    sum2 = 0
                    it.data.personList.forEach { person ->
                        sum2 += person.paymentFee
                    }
                    binding.buyTotalfee.text = dec.format(sum2).toString() + "원"
                    binding.totalmoney.text = dec.format(sum2 - sum).toString() + "원"
                    binding.totalbuyprice.text = dec.format(sum).toString() + "원"
                }
            }
        }
        binding.maintitle.setOnClickListener {
            mtViewModel.toggleBuyGoodFoldStatus()
        }
        binding.btnaddbuy.setOnClickListener {
            BuyGoodDialog.newInstance(
                object : BuyGoodDialog.BuyGoodDialogCallback {
                    override fun onClick(
                        category: String,
                        name: String,
                        count: String,
                        price: String
                    ) {
                        mtViewModel.insertBuyGood(
                            category,
                            name,
                            count.toInt(),
                            price.toInt()
                        )
                    }
                },
                mtViewModel.categoryList.value
            ).show(parentFragmentManager, null)
        }
        binding.btnclearbuy.setOnClickListener {
            DeleteDialog.newInstance(
                object : DeleteDialog.DeleteDialogCallback {
                    override fun onclick() {
                        mtViewModel.clearBuyGoodData()
                    }
                },
                getString(R.string.dialog_delete_reset)
            ).show(
                parentFragmentManager, null
            )

        }
    }

}