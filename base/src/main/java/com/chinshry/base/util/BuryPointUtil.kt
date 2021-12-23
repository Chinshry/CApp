package com.chinshry.base.util

import com.blankj.utilcode.util.LogUtils
import com.chinshry.base.bean.BuryPointInfo

object BuryPointUtil {
    fun logBuryPoint(buryPoint: BuryPointInfo) {
        if (buryPoint.pageName.isNotEmpty() && buryPoint.pageChannel.isNotEmpty()) {
            LogUtils.d(buryPoint.toString())
        }
    }
}