package com.chinshry.application.tabView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter2(private val fragmentList: List<Fragment>, activity: TabActivity)
    : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return if (fragmentList.isNotEmpty()) fragmentList.size else 0
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }

}