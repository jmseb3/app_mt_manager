package com.wonddak.mtmanger.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wonddak.mtmanger.databinding.FragmentPlanBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.Plan
import com.wonddak.mtmanger.ui.MainActivity
import com.wonddak.mtmanger.ui.adapter.PlanRecyclerAdaptar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PlanFragment : Fragment() {

    internal var mainActivity: MainActivity? = null
    private var adapter: PlanRecyclerAdaptar? = null
    private lateinit var binding: FragmentPlanBinding
    var imgid: Int? =null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()

        val mainmtid: Int = prefs.getInt("id", 0)

        binding.planAddBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.MtDataDao().insertPlan(Plan(null, mainmtid))
            }.isCompleted
        }

        if (mainmtid == 0) {
            binding.mtdatastart.visibility = View.VISIBLE
        } else {
            binding.mtdatastart.visibility = View.INVISIBLE

            db.MtDataDao().getPlan(mainmtid)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    adapter = PlanRecyclerAdaptar(it, requireContext(), db, editor, mainActivity!!)
                    binding.planRecycler.adapter = adapter
                    val size = it.size
                    binding.planRecycler.layoutManager!!.scrollToPosition(size)

                })
        }

        return binding.root
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