package com.chinshry.base

import androidx.appcompat.app.AppCompatActivity
import com.example.base.R
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by chinshry on 2021/12/23.
 * File Name: BaseActivity.kt
 */
open class BaseActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        ImmersionBar
            .with(this)
            .fitsSystemWindows(true)
            .barColor(R.color.white)
            .autoDarkModeEnable(true)
            .init()
    }
}