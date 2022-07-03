package com.wonddak.mtmanger.ui.main

import android.content.Context

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonddak.mtmanger.AddDialog
import com.wonddak.mtmanger.R
import com.wonddak.mtmanger.databinding.FragmentMainBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.ui.BaseFragment
import com.wonddak.mtmanger.ui.MainActivity
import kotlinx.coroutines.*
import java.text.DecimalFormat


class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val db = AppDatabase.getInstance(requireContext())
        val dec = DecimalFormat("#,###")

        viewModel.mainMtId.observe(viewLifecycleOwner){ mainmtid ->
            if (mainmtid == 0) {
                binding.mtdatastart.visibility = View.VISIBLE
                binding.mtdatafield.visibility = View.INVISIBLE
            } else {
                binding.mtdatastart.visibility = View.INVISIBLE
                binding.mtdatafield.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch{
                    CoroutineScope(Dispatchers.IO).launch{
                        db.MtDataDao().getMtDataById(mainmtid).apply {
                            binding.mttitle.text = this.mtTitle + " MT"
                            binding.mttotoalfee.text = dec.format(this.fee).toString()
                            binding.mtstart.text = this.mtStart
                            binding.mtend.text = this.mtEnd
                        }
                }


            }
        }}

        viewModel.getPerson().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.mttotalperson.text = it.size.toString()
        })


        binding.mtdataadd.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(1, 1)
        }

        binding.newmt.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(null, 1)
        }

        binding.mtedit.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(viewModel.mainMtId.value, 2)
        }

        binding.mtlists.setOnClickListener {

            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.settingfrag, MtListFragment())
                .commit()

            mainActivity!!.mBottomNavigationView.visibility = View.GONE
            mainActivity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        }

    }
}
