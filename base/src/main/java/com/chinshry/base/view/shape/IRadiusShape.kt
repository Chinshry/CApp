package com.chinshry.base.view.shape

import android.content.res.ColorStateList


interface IRadiusShape {
    /**
     * 设置背景颜色状态列表
     *
     * @param bgColorStateList 背景颜色状态列表
     */
    fun setBackgroundColor(bgColorStateList: ColorStateList?)

    /**
     * 设置边框线颜色
     *
     * @param color 边框线颜色
     */
    fun setBorderColor(color: Int)

    /**
     * 设置边框颜色状态列表
     *
     * @param borderColorStateList 边框颜色状态列表
     */
    fun setBorderColor(borderColorStateList: ColorStateList?)

    /**
     * 设置圆角，四个圆角大小一样
     *
     * @param radius 圆角大小
     */
    fun setRadius(radius: Int)

    /**
     * 设置背景渐变信息
     *
     * @param colors             渐变颜色数组
     * @param positions     渐变位置
     */
    fun setBackgroundColors(
        colors: IntArray,
        positions: FloatArray,
    )

    /**
     * 设置边框渐变信息
     *
     * @param colors             渐变颜色数组
     * @param positions     渐变位置
     */
    fun setBorderColors(
        colors: IntArray,
        positions: FloatArray,
    )
}