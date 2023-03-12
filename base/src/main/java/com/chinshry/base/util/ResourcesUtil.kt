package com.chinshry.base.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.StringUtils

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

fun getStringById(@StringRes id: Int?, defValue: String = "") = id?.let { StringUtils.getString(it) } ?: defValue
fun getColorById(@ColorRes id: Int) = ColorUtils.getColor(id)
fun getDrawableById(@DrawableRes id: Int): Drawable = ResourceUtils.getDrawable(id)

inline val Int.dp: Int get() = toFloat().dp.toInt()
inline val Float.dp: Float get() = CommonUtils.dp2px(ActivityUtils.getTopActivity(), this)

fun Int.dp(context: Context?): Int = run { return toFloat().dp(context).toInt() }
fun Float.dp(context: Context?): Float = CommonUtils.dp2px(context, this)