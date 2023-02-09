package com.chinshry.base.view

import android.graphics.Paint
import android.text.style.LineHeightSpan
import kotlin.math.roundToInt

/**
 * Created by chinshry on 2023/02/09.
 * Describe：固定 TextView 行高度的 Span
 * 参考：https://segmentfault.com/a/1190000021947194
 */
class FixedLineHeightSpan(private val fixedLineHeight: Int) : LineHeightSpan {
    override fun chooseHeight(text: CharSequence?, start: Int, end: Int, spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt) {
        // 原始行高
        val originHeight = fm.descent - fm.ascent
        if (originHeight <= 0) {
            return
        }
        // 计算比例值
        val ratio = fixedLineHeight * 1.0f / originHeight
        // 根据最新行高，修改descent
        fm.descent = (fm.descent * ratio).roundToInt()
        // 根据最新行高，修改ascent
        fm.ascent = fm.descent - fixedLineHeight
    }
}