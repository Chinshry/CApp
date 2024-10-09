package com.chinshry.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getNameByModelName
import com.chinshry.base.bean.PageParamsBean
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.util.CommonUtils.getModuleName
import com.chinshry.base.util.getPageBuryPointByClass
import com.chinshry.base.util.logBuryPoint
import com.chinshry.base.view.CustomHeaderBar.Companion.initCustomHeaderBar
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by chinshry on 2021/12/23.
 */
open class BaseActivity : AppCompatActivity() {
    open var pageSource: String? = null
    open var pageBuryPoint: BuryPointInfo = BuryPointInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 注解埋点
        getPageBuryPointByClass(this::class.java)?.let {
            pageBuryPoint = BuryPointInfo(
                pageName = it.pageName,
                pageChannel = getNameByModelName(getModuleName(this.javaClass.name))
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

    override fun setContentView(view: View?) {
        super.setContentView(view)
        // 初始化header点击事件
        initCustomHeaderBar(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        if (!isInMultiWindowMode) {
            ImmersionBar
                .with(this)
                .fitsSystemWindows(true)
                .barColor(R.color.white)
                .autoDarkModeEnable(true)
                .init()
        }
    }
}