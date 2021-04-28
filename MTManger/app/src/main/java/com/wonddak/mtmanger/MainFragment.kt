package com.wonddak.mtmanger

import android.content.Context

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.wonddak.mtmanger.databinding.FragmentMainBinding
import com.wonddak.mtmanger.room.AppDatabase
import kotlinx.coroutines.*
import java.text.DecimalFormat


class MainFragment : Fragment() {
    internal var mainActivity: MainActivity? = null
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val prefs: SharedPreferences = requireContext().getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()

        val mainmtid: Int = prefs.getInt("id", 0)

        val db = AppDatabase.getInstance(requireContext())
        val dec = DecimalFormat("#,###")

        GlobalScope.launch(Dispatchers.IO) {
            var data =db.MtDataDao().getMtDataAndPerson()
            Log.d("datas:",""+data)
        }




        if (mainmtid == 0) {
            binding.mtdatastart.visibility = View.VISIBLE
            binding.mtdatafield.visibility = View.INVISIBLE
        } else {

            binding.mtdatastart.visibility = View.INVISIBLE
            binding.mtdatafield.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                var nowMtData = db.MtDataDao().getMtDataById(mainmtid)

                GlobalScope.launch(Dispatchers.Default) {
                    binding.mttitle.text = nowMtData.mtTitle + " MT"
                    binding.mttotoalfee.text = dec.format(nowMtData.fee).toString()
                    binding.mtstart.text = nowMtData.mtStart
                    binding.mtend.text = nowMtData.mtEnd
                }
            }.isCompleted


        }

        db.MtDataDao().getPerson(mainmtid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.mttotalperson.text = it.size.toString()
        })


        binding.mtdataadd.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(1, 1)
        }

        binding.newmt.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(null, 1)
        }

        binding.mtedit.setOnClickListener {
            AddDialog(requireContext(), db, editor, mainActivity!!).mtDialog(mainmtid, 2)
        }

        binding.mtlists.setOnClickListener {

            requireFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .add(R.id.settingfrag, MtListFragment())
                .commit()

            mainActivity!!.mBottomNavigationView.visibility = View.GONE
            mainActivity!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
