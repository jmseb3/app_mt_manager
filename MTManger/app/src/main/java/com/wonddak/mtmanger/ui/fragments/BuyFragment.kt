package com.wonddak.mtmanger.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wonddak.mtmanger.AddDialog
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentBuyBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.adapter.BuyRecyclerAdaptar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class BuyFragment : Fragment() {
    internal var mainActivity: MainActivity? = null
    private var adapter: BuyRecyclerAdaptar? = null
    private  lateinit var binding : FragmentBuyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBuyBinding.inflate(inflater,container,false)

        val db = AppDatabase.getInstance(requireContext())

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val dec = DecimalFormat("#,###")

        val mainmtid: Int = prefs.getInt("id", 0)

        if (mainmtid ==0) {
            binding.mtdatastart.visibility = View.VISIBLE
        } else {
            binding.mtdatastart.visibility = View.INVISIBLE

            var fee:Int =0
            GlobalScope.launch(Dispatchers.IO) {
                var nowMtData = db.MtDataDao().getMtDataById(mainmtid)
                fee=nowMtData.fee
            }.isCompleted

            var sum: Int
            db.MtDataDao().getBuyGood(mainmtid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter = BuyRecyclerAdaptar(requireContext(), it,db,editor,mainActivity!!)
                binding.buyrecycler.adapter = adapter

                sum = 0
                for (element in it) {
                    sum += (element.price * element.count)
                }

                var sum2: Int
                db.MtDataDao().getPerson(mainmtid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    sum2 = 0
                    for (element in it) {
                        sum2 += element.paymentFee
                    }
                    binding.buyTotalfee.text =dec.format(sum2).toString() +"원"
                    binding.totalmoney.text = dec.format(sum2-sum).toString() +"원"

                })
                binding.totalbuyprice.text = dec.format(sum).toString() +"원"


            })

        }

        var check: Boolean = true
        binding.maintitle.setOnClickListener {
            if (check == true){
                check =false
                binding.underdetail.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.drawable.ic_baseline_arrow_drop_up_24)
                    .into(binding.showOrNotshow)
            } else {
                check =true
                binding.underdetail.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(R.drawable.ic_baseline_arrow_drop_down_24)
                    .into(binding.showOrNotshow)
            }

        }

        binding.buyrecycler.addItemDecoration(
                DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        )



        binding.btnaddbuy.setOnClickListener {
            AddDialog(requireContext(),db,editor,mainActivity!!).buyDialog(mainmtid,0,1)
        }

        binding.btnclearbuy.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).DeleteData(mainmtid,0,4)
        }






        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = getActivity() as MainActivity

    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }
}