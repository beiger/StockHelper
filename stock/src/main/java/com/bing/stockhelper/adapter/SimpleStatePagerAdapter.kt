package com.bing.stockhelper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class SimplePagerAdapter(
        fm: FragmentManager,
        private val mFragments: ArrayList<Fragment>,
        private val mTitles: List<String>? = null
): FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
                return mFragments[position]
        }

        override fun getCount(): Int {
                return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
                return mTitles?.get(position)
        }
}

class SimpleStatePagerAdapter(
        fm: FragmentManager,
        private val builder: (Int) -> Fragment,
        private val size: Int,
        private val mTitles: List<String>? = null
): FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
                return builder(position)
        }

        override fun getCount(): Int {
                return size
        }

        override fun getPageTitle(position: Int): CharSequence? {
                return mTitles?.get(position)
        }
}