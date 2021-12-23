package com.chinshry.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getBuryNameByModelName
import com.chinshry.base.bean.PageParamsBean
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.util.CommonUtils.getModuleName
import com.chinshry.base.util.getPageBuryPoint
import com.chinshry.base.util.logBuryPoint
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
            pageBuryPoint = BuryPointInfo(
                pageName = it.pageName,
                pageChannel = getBuryNameByModelName(getModuleName(this.javaClass.name))
            )
        }

        // intent携带params
        intent.getStringExtra("params")?.let { params ->
            CommonUtils.string2object(params, PageParamsBean::class.java) ?.apply {
                source?.let { pageSource = it }
                trackInfo?.let { pageBuryPoint = it }
            }
        }

        logBuryPoint(pageBuryPoint)

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setBackButton()
        setCloseButton()
    }

    fun setPageTitle(title: String) {
        header_text_title?.text = title
    }

    private fun setCloseButton(
        function: () -> Unit = { finish() }
    ) {
        header_btn_close?.clickWithTrigger { function() }

    }

    private fun setBackButton(
        function: () -> Unit = { finish() }
    ) {
        header_btn_back?.clickWithTrigger { function() }
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
}