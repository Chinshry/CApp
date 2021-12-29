package com.chinshry.base.util

import com.blankj.utilcode.util.LogUtils
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Module.Companion.getBuryNameByModelName

/**
 * Created by chinshry on 2021/12/23.
 * Describe：
 */
fun getPageBuryPoint(mClass: Class<*>?): BuryPointInfo? {
    return mClass?.getAnnotation(BuryPoint::class.java)?.run {
        BuryPointInfo(
            pageName = pageName,
            pageChannel = getBuryNameByModelName(CommonUtils.getModuleName(mClass.name))
        )
    }
}

fun getClickBuryPoint(text: String?, offset: Int = 3): BuryPointInfo? {
    text?.let {
        // return getPageBuryPoint(CommonUtils.getTrackClass(offset))?.apply { buttonName = it }
        return CommonUtils.getTrackBuryPoint(offset)?.apply { buttonName = it }
    }
    return null
}

fun logBuryPoint(buryPoint: BuryPointInfo?) {
    buryPoint?.apply {
        if (pageName.isNotEmpty() && pageChannel.isNotEmpty()) {
            if (buttonName.isEmpty()) {
                LogUtils.d("PAGE | $this")
            } else {
                LogUtils.d("BUTTON | $this")
            }
        }
    }
}

