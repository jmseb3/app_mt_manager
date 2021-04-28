package com.wonddak.mtmanger

import android.Manifest
import android.R.attr.name
import android.R.id
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.wonddak.mtmanger.databinding.ActivityMainBinding
import com.wonddak.mtmanger.room.AppDatabase
import com.wonddak.mtmanger.room.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

    private lateinit var bm: BillingModule


    private var isPurchasedRemoveAds = false
        set(value) {
            field = value
            updateRemoveAdsView()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs: SharedPreferences = applicationContext.getSharedPreferences("mainMT", 0)

        bm = BillingModule(this, lifecycleScope, object : BillingModule.Callback {
            override fun onBillingModulesIsReady() {
                bm.checkPurchased(SettingFragment.Sku.REMOVE_ADS) {
                    isPurchasedRemoveAds = it
                    Log.d("datas", "isPurchasedRemoveAds:" + it)
                }
            }

            override fun onSuccess(purchase: Purchase) {

            }

            override fun onFailure(errorCode: Int) {

            }
        })

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

    private fun updateRemoveAdsView() {
        if (isPurchasedRemoveAds) {
            mAdView.visibility = View.GONE
        }
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
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
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
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    binding.mainactivitytitle.text = "설정"
                }
            }
            R.id.action_hide_bottom -> {
                val prefs: SharedPreferences = applicationContext.getSharedPreferences("mainMT", 0)
                val editor = prefs.edit()
                val hide = prefs.getBoolean("hide", false)
                if (!hide) {
                    editor.putBoolean("hide", true)
                    editor.commit()
                    mBottomNavigationView.visibility = View.GONE
                    invalidateOptionsMenu()

                } else {
                    editor.putBoolean("hide", false)
                    editor.commit()
                    mBottomNavigationView.visibility = View.VISIBLE
                    invalidateOptionsMenu()
                }

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
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
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


    fun changeFragment(fragment: Fragment) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val db = AppDatabase.getInstance(this)
        val prefs: SharedPreferences = this.getSharedPreferences("mainMT", 0)
        val editor = prefs.edit()
        val mainmtid: Int = prefs.getInt("id", 0)

        if (requestCode == 105 && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            val iddatas = prefs.getInt("key", 0)
            GlobalScope.launch(Dispatchers.IO) {
                db.MtDataDao().updatePlanbyid(iddatas, "" + uri)
            }
        }


        if (requestCode == 1010 && resultCode == RESULT_OK && data != null) {
            val uri = data.data!!
            Log.d("datas", "" + uri)

            val cursor = contentResolver.query(
                uri,
                arrayOf<String>(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ), null, null, null
            )

            cursor!!.moveToFirst()
            val name = cursor.getString(0) //0은 이름을 얻어옵니다.
            val number = cursor.getString(1) //1은 번호를 받아옵니다.
            Log.d("datas", name + "." + number)
            cursor.close()

            AddDialog(this, db, editor, this@MainActivity).personDialog(
                mainmtid,
                0,
                1,
                name,
                number
            )

        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "연락처 권한을 승인했습니다..", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                    this, "연락처 권한이 필요합니다.\n" +
                            " 현재 거부상태입니다.", Toast.LENGTH_LONG
                ).show();
            }
        }
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "저장소 권한을 승인했습니다..", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                    this, "저장소 권한이 필요합니다.\n" +
                            " 현재 거부상태입니다.", Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}



