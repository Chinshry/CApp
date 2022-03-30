package com.chinshry.base.util

import com.blankj.utilcode.util.LogUtils
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module
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

fun getTrackBuryPoint(offset: Int = 0): BuryPointInfo? {
    Thread.currentThread().stackTrace.forEachIndexed { index, stackTraceElement ->
        if (index >= offset) {
            stackTraceElement?.let {
                if (!it.isNativeMethod && isModuleClass(it.className)) {
                    getPageBuryPoint(it.className, it.methodName) ?.let { classBuryPoint ->
                        return classBuryPoint
                    }
                }
            }
        }
    }
    return null
}

fun getTrackClass(offset: Int = 0): Class<*>? {
    return Thread.currentThread().stackTrace.getOrNull(offset)?.className?.let {
        Class.forName(it)
    }
}

fun isModuleClass(className: String?): Boolean {
    if (className.isNullOrBlank()) return false
    if (className.contains("$")) return false
    if (className.contains("PermissionHelper")) return true

    val modelName = getNameByModelName(CommonUtils.getModuleName(className))
    return modelName != Module.Base.moduleName
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

fun getClickBuryPoint(offset: Int = 3): BuryPointInfo? {
    // return getPageBuryPoint(CommonUtils.getTrackClass(offset))?.apply { buttonName = it }
    return getTrackBuryPoint(offset)
}

fun logBuryPoint(buryPoint: BuryPointInfo?) {
    buryPoint?.apply {
        if (pageName.isNotEmpty() && pageChannel.isNotEmpty()) {
            LogUtils.d(this.toLogString())
        }
    }
}

