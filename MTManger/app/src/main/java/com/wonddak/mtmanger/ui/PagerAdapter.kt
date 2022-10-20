package com.wonddak.mtmanger.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wonddak.mtmanger.ui.fragments.BuyFragment
import com.wonddak.mtmanger.ui.fragments.MainFragment
import com.wonddak.mtmanger.ui.fragments.PersonFragment
import com.wonddak.mtmanger.ui.fragments.PlanFragment

class PagerAdapter(
    fm: FragmentManager
): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return  when(position){
            0 -> MainFragment()
            1 -> PersonFragment()
            2 -> BuyFragment()
            3 -> PlanFragment()
            else -> MainFragment()
        }
    }
}