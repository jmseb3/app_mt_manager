package com.wonddak.mtmanger.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wonddak.mtmanger.*
import com.wonddak.mtmanger.databinding.ActivityMainBinding
import com.wonddak.mtmanger.ui.fragments.*
import com.wonddak.mtmanger.viewModel.MTViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val fragmentOne by lazy { MainFragment() }
    private val fragmentTwo by lazy { PersonFragment() }
    private val fragmentThree by lazy { BuyFragment() }
    private val fragmentFour by lazy { PlanFragment() }
    private var backKeyPressedTime: Long = 0

    private lateinit var binding: ActivityMainBinding
    lateinit var mBottomNavigationView: BottomNavigationView
    lateinit var mViewPager: ViewPager
    lateinit var mAdView: AdView

    @Inject
    lateinit var billingModule: BillingModule


    @Inject
    lateinit var preferences: SharedPreferences

    private val mtViewModel: MTViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainmtid: Int = preferences.getInt("id", 0)

        mtViewModel.setMtId(mainmtid)
        lifecycleScope.launchWhenCreated {
            billingModule.removeAddStatus.collect {
                mtViewModel.setRemoveAddStatus(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            mtViewModel.removeAdStatus.collect {
                Log.i("Billing JWH", "Main status $it")
                if (it) {
                    mAdView.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            mtViewModel.bottomMenuStatus.collect {
                if (it) {
                    mBottomNavigationView.visibility = View.VISIBLE
                } else {
                    mBottomNavigationView.visibility = View.GONE
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            mtViewModel.topBackButtonStatus.collect {
                supportActionBar!!.setDisplayHomeAsUpEnabled(!it)
            }
        }

        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        MobileAds.initialize(baseContext) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        mBottomNavigationView = binding.bottomNavigation
        mViewPager = binding.ViewPager
        mViewPager.adapter = PagerAdapter(supportFragmentManager)

        binding.ViewPager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    // 네비게이션 메뉴 아이템 체크상태
                    mBottomNavigationView.menu.getItem(position).isChecked = true
                }
            })

        initNavigationBar()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }

        } else if (supportFragmentManager.backStackEntryCount == 1) {
            supportFragmentManager.popBackStack()
            binding.mainactivitytitle.text = "MT 매니저"
            mtViewModel.setTopButtonStatus(false)
        } else {
            supportFragmentManager.popBackStack()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("mainMT", 0)
        val hide = prefs.getBoolean("hide", false)
        if (!hide) {
            menuInflater.inflate(R.menu.menu_main, menu)
        } else {
            menuInflater.inflate(R.menu.menu_main2, menu)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                if (supportFragmentManager.backStackEntryCount == 0) {
                    supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.settingfrag, SettingFragment())
                        .commit()


                    mBottomNavigationView.visibility = View.GONE
                    mtViewModel.setTopButtonStatus(false)
                    binding.mainactivitytitle.text = "설정"
                }
            }
            R.id.action_hide_bottom -> {
                mtViewModel.toggleBottomMenuStatus()
                invalidateOptionsMenu()
            }
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                val prefs: SharedPreferences = applicationContext.getSharedPreferences("mainMT", 0)
                val hide = prefs.getBoolean("hide", false)
                if (!hide) {
                    mBottomNavigationView.visibility = View.VISIBLE
                } else {
                    mBottomNavigationView.visibility = View.GONE
                }
                binding.mainactivitytitle.text = "MT 매니저"
                mtViewModel.setTopButtonStatus(true)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initNavigationBar() {
        mBottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_main -> {
                        changeFragment(fragmentOne)
                        mViewPager.currentItem = 0
                    }
                    R.id.nav_person -> {
                        changeFragment(fragmentTwo)
                        mViewPager.currentItem = 1
                    }
                    R.id.nav_buy -> {
                        changeFragment(fragmentThree)
                        mViewPager.currentItem = 2
                    }
                    R.id.nav_plan -> {
                        changeFragment(fragmentFour)
                        mViewPager.currentItem = 3
                    }
                }
                true
            }
            selectedItemId = R.id.nav_main
            mViewPager.currentItem = 0
        }
    }


    private fun changeFragment(fragment: Fragment) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            for (i in 0..supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragcontainer, fragment)
            .commit()

    }
}



