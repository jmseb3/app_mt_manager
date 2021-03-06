package com.wonddak.mtmanger.ui.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.bumptech.glide.Glide
import com.wonddak.mtmanger.BillingModule
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentSettingBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.categoryList
import com.wonddak.mtmanger.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SettingFragment : Fragment() {

    internal var mainActivity: MainActivity? = null
    private var adapter: SettingRecyclerAdaptar? = null
    private lateinit var binding: FragmentSettingBinding
    private lateinit var bm: BillingModule

    private var mSkuDetails = listOf<SkuDetails>()
        set(value) {
            field = value
        }

    private var isPurchasedRemoveAds = false
        set(value) {
            field = value
            updateRemoveAdsView()
        }


    object Sku {
        const val REMOVE_ADS = "remove_ad_inapp"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()



        db.MtDataDao().getCategory().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter = SettingRecyclerAdaptar(it, requireContext(), db, editor, mainActivity!!)
            binding.categoryrecycler.adapter = adapter

        })

        var check = false
        binding.titleline.setOnClickListener {
            if (check == false) {
                check = true
                binding.showOrNotshow.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(R.drawable.ic_baseline_arrow_drop_down_24)
                    .into(binding.arrow)
            } else {
                check = false
                binding.showOrNotshow.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.drawable.ic_baseline_arrow_drop_up_24)
                    .into(binding.arrow)
            }


        }

        binding.addCategory.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.MtDataDao()
                    .insertCategory(categoryList(null, binding.addCategoryText.text.toString()))
            }.isCompleted
            Toast.makeText(requireContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            binding.addCategoryText.setText("")


        }
        binding.sendemail.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf<String>("jmseb2@gmail.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, "<MT????????? ?????? ???????????????.>")
            email.putExtra(Intent.EXTRA_TEXT, "??????:")
            startActivity(email)

        }

        bm = BillingModule(requireActivity(), lifecycleScope, object : BillingModule.Callback {
            override fun onBillingModulesIsReady() {
                bm.querySkuDetail(
                    BillingClient.SkuType.INAPP,
                    Sku.REMOVE_ADS,
                ) { skuDetails ->
                    mSkuDetails = skuDetails
                }
                bm.checkPurchased(Sku.REMOVE_ADS) {
                    isPurchasedRemoveAds = it
                    Log.d("datas", "isPurchasedRemoveAds:" + it)
                }

            }

            override fun onSuccess(purchase: Purchase) {
                when (purchase.sku) {
                    Sku.REMOVE_ADS -> {
                        isPurchasedRemoveAds = true
                    }
                }
            }

            override fun onFailure(errorCode: Int) {
                when (errorCode) {
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                        Toast.makeText(requireContext(), "?????? ????????? ???????????????.", Toast.LENGTH_LONG)
                            .show()
                    }
                    BillingClient.BillingResponseCode.USER_CANCELED -> {
                        Toast.makeText(requireContext(), "????????? ?????????????????????.", Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "error: $errorCode", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })

        setClickListeners()

        return (binding.root)
    }


    private fun updateRemoveAdsView() {
        if (isPurchasedRemoveAds) {
            binding.buyNoAdd.text = "?????? ?????? ??????:O"
            binding.buyNoAdd.isEnabled = false
            mainActivity!!.mAdView.visibility = View.GONE
        } else {
            binding.buyNoAdd.text = "?????? ??????"
        }

    }

    private fun setClickListeners() {
        with(binding) { // ?????? ?????? ?????? ?????? ??????
            buyNoAdd.setOnClickListener {
                mSkuDetails.find { it.sku == Sku.REMOVE_ADS }
                    ?.let { skuDetail -> bm.purchase(skuDetail) } ?: also {
                    Toast.makeText(requireContext(), "????????? ?????? ??? ????????????.", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity

    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

}
