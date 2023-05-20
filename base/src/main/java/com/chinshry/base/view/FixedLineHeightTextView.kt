package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import kotlin.math.absoluteValue

/**
 * Created by chinshry on 2023/02/09.
 * Describe：固定行高度的 TextView
 * 参考：https://segmentfault.com/a/1190000021947194
 */
@SuppressLint("AppCompatCustomView")
class FixedLineHeightTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : TextView(context, attrs, defStyleAttr) {
    init {
        includeFontPadding = false
        isFallbackLineSpacing = false

        initView()
    }

    private fun initView() {
        val originHeight = paint.fontMetricsInt.run { descent - ascent }
        val ratio = lineHeight.toFloat() / originHeight

        val fixTop = (paint.fontMetricsInt.ascent.absoluteValue * ratio).toInt()
        val fixBottom = lineHeight - firstBaselineToTopHeight

        setPadding(
            paddingLeft, fixTop + paint.fontMetricsInt.ascent,
            paddingRight, fixBottom - paint.fontMetricsInt.descent
        )
    }
}
