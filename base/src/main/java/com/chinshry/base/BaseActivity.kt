package com.chinshry.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.BuryPointInfo.Companion.getPageBuryPoint
import com.chinshry.base.bean.BuryPointInfo.Companion.logBuryPoint
import com.chinshry.base.bean.PageParamsBean
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.view.clickWithTrigger
import com.example.base.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.custom_bar.*

/**
 * Created by chinshry on 2021/12/23.
 * File Name: BaseActivity.kt
 */
open class BaseActivity : AppCompatActivity() {
    var pageSource: String? = null
    var pageBuryPoint: BuryPointInfo = BuryPointInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 注解埋点
        getPageBuryPoint(this::class.java)?.let {
            pageBuryPoint = it
        }

        // intent携带params
        intent.getStringExtra("params")?.let { params ->
            CommonUtils.string2object(params, PageParamsBean::class.java) ?.apply {
                source?.let { pageSource = it }
                trackInfo?.let { pageBuryPoint = it }
            }
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setBackButton()
        setCloseButton()
    }

    fun setPageTitle(title: String) {
        header_text_title?.apply {
            text = title
        }
    }

    private fun setCloseButton(
        function: () -> Unit = { finish() }
    ) {
        header_btn_close?.apply {
            clickWithTrigger {
                function()
            }
        }
    }

    private fun setBackButton(
        function: () -> Unit = { finish() }
    ) {
        header_btn_back?.apply {
            clickWithTrigger {
                function()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        logBuryPoint(pageBuryPoint)

        ImmersionBar
            .with(this)
            .fitsSystemWindows(true)
            .barColor(R.color.white)
            .autoDarkModeEnable(true)
            .init()
    }
}