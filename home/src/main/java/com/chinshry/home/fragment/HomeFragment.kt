package com.chinshry.home.fragment

import android.os.Bundle
import android.view.View
import com.chinshrry.home.R
import com.chinshry.base.BaseFragment

/**
 * Created by chinshry on 2021/12/23.
 * File Name: HomeFragment.kt
 * Describe： 主页Fragment
 */
class HomeFragment: BaseFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
    }

}