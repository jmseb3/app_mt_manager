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
import com.wonddak.mtmanger.databinding.FragmentPersonBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.adapter.PersonRecyclerAdaptar
import java.text.DecimalFormat

class PersonFragment : Fragment() {
    internal var mainActivity: MainActivity? = null
    private var adapter: PersonRecyclerAdaptar? = null
    private lateinit var binding: FragmentPersonBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPersonBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val dec = DecimalFormat("#,###")

        val mainmtid: Int = prefs.getInt("id", 0)

        if (mainmtid == 0) {
            binding.mtdatastart.visibility = View.VISIBLE
        } else {
            binding.mtdatastart.visibility = View.INVISIBLE

            var sum: Int
            db.MtDataDao().getPerson(mainmtid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter = PersonRecyclerAdaptar(it, requireContext(), db, editor, mainActivity!!)
                binding.personrecycler.adapter = adapter
                sum = 0
                for (element in it) {
                    sum += element.paymentFee
                }

                binding.totalfee.text = dec.format(sum).toString() +"원"
                binding.totalcount.text =it.size.toString() +"명"

            })
        }



        binding.personrecycler.addItemDecoration(
                DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        )

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


        binding.btnaddperson.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).personDialog(mainmtid,0,1,null,null)
        }

        binding.btnclearperson.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).DeleteData(mainmtid,0,2)

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