package com.wonddak.mtmanger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    fm: FragmentManager
):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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