package com.wonddak.mtmanger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonddak.mtmanger.databinding.FragmentMtListBinding
import com.wonddak.mtmanger.room.AppDatabase

class MtListFragment : Fragment() {
    internal var mainActivity: MainActivity? = null
    private var adapter: MtListRecyclerAdaptar? = null
    private lateinit var binding: FragmentMtListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMtListBinding.inflate(inflater, container, false)

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val db = AppDatabase.getInstance(requireContext())

        binding.mtlistrecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        db.MtDataDao().getMtData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter = MtListRecyclerAdaptar(requireContext(),it,editor,mainActivity!!)
            binding.mtlistrecycler.adapter = adapter

        })

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