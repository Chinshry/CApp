package com.chinshry.base.util

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils

/**
 * Created by chinshry on 2023/3/10.
 * Describe：资源工具类
 */

/**
 * 根据resId设置文字大小
 * @receiver AppCompatTextView
 * @param dimenId Int
 */
fun TextView.setTextSizeById(dimenId: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimenById(dimenId).toFloat())
}

fun View.getDimensionById(dimenId: Int): Float {
    return context.resources.getDimension(dimenId)
}

fun View.getIntegerById(integerId: Int): Int {
    return context.resources.getInteger(integerId)
}

fun View.getBooleanById(booleanId: Int): Boolean {
    return context.resources.getBoolean(booleanId)
}

/**
 * 根据resId获取文字大小
 * @receiver View
 * @param dimenId Int
 * @return Int
 */
fun View.getDimenById(dimenId: Int): Int {
    return resources.getDimensionPixelSize(dimenId)
}

inline val Double.dp: Int
    get() = run {
        return toFloat().dp
    }
inline val Int.dp: Int
    get() = run {
        return toFloat().dp
    }
inline val Float.dp: Int
    get() = run {
        val context = ActivityUtils.getTopActivity()
        return CommonUtils.dp2px(context, this)
    }