package com.chinshry.base.util

import com.blankj.utilcode.util.LogUtils
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getNameByModelName
import java.lang.Exception
import java.lang.reflect.Method

/**
 * Created by chinshry on 2021/12/23.
 * Describe：埋点工具类
 */
fun getPageBuryPointByClass(mClass: Class<*>?): BuryPointInfo? {
    return mClass?.getAnnotation(BuryPoint::class.java)?.run {
        BuryPointInfo(
            pageName = pageName,
            pageChannel = getNameByModelName(CommonUtils.getModuleName(mClass.name))
        )
    }
}

fun getPageBuryPointByMethod(method: Method?): BuryPointInfo? {
    return method?.getAnnotation(BuryPoint::class.java)?.run {
        BuryPointInfo(
            pageName = pageName,
            pageChannel = getNameByModelName(CommonUtils.getModuleName(method.toString()))
        )
    }
}

fun getPageBuryPoint(className: String, methodName: String?): BuryPointInfo? {
    if (!methodName.isNullOrBlank()) {
        try {
            (getPageBuryPointByMethod(Class.forName(className).getDeclaredMethod(methodName)))?.let { return it }
        } catch (e: Exception) {
            Class.forName(className).declaredMethods.forEach { method ->
                if (method.name == methodName) {
                    (getPageBuryPointByMethod(method))?.let { return it }
                }
            }
        }
    }

    return getPageBuryPointByClass(Class.forName(className))
}

fun getClickBuryPoint(text: String?, offset: Int = 3): BuryPointInfo? {
    text?.let {
        // return getPageBuryPoint(CommonUtils.getTrackClass(offset))?.apply { buttonName = it }
        return CommonUtils.getTrackBuryPoint(offset)?.apply { viewName = it }
    }
    return null
}

fun logBuryPoint(buryPoint: BuryPointInfo?) {
    buryPoint?.apply {
        if (pageName.isNotEmpty() && pageChannel.isNotEmpty()) {
            LogUtils.d(this.toLogString())
        }
    }
}

