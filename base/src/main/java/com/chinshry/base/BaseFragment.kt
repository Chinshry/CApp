package com.chinshry.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getNameByModelName
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.util.getPageBuryPointByClass
import com.chinshry.base.util.logBuryPoint
import com.chinshry.base.view.CustomHeaderBar.Companion.initCustomHeaderBar

/**
 * Created by chinshry on 2021/12/23.
 */
abstract class BaseFragment: Fragment() {
    open var pageBuryPoint: BuryPointInfo = BuryPointInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 注解埋点
        getPageBuryPointByClass(this::class.java)?.let {
            pageBuryPoint = BuryPointInfo(
                pageName = it.pageName,
                pageChannel = getNameByModelName(CommonUtils.getModuleName(this.javaClass.name))
            )
        }

        logBuryPoint(pageBuryPoint)

        return inflater.inflate(setLayout(), container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            logBuryPoint(pageBuryPoint)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化header点击事件
        initCustomHeaderBar(this)
    }

    abstract fun setLayout(): Int
}