package com.chinshry.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base.R
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by chinshry on 2021/12/23.
 * File Name: BaseFragment.kt
 */
abstract class BaseFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar
            .with(this)
            .fitsSystemWindows(true)
            .barColor(R.color.white)
            .autoDarkModeEnable(true)
            .init()
    }

    abstract fun setLayout(): Int
}