package com.chinshry.base.view

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.chinshry.base.R

/**
 * Created by chinshry on 2023/02/09.
 * Describe：固定行高度的 TextView
 * 参考：https://segmentfault.com/a/1190000021947194
 */
class FixedLineHeightTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var fixedLineHeight: Int = 0
    
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FixedLineHeightTextView, defStyleAttr, 0)
        setFixedLineHeight(typedArray.getDimensionPixelSize(R.styleable.FixedLineHeightTextView_fixedLineHeight, 0))
        typedArray.recycle()
    }
    
    /**
     * 设置行高度
     * @param dimension Int
     */
    fun setFixedLineHeight(dimension: Int) {
        fixedLineHeight = dimension
    }

    /**
     * 获取行高度
     */
    fun getFixedLineHeight(): Int {
        return fixedLineHeight
    }

    /**
     * 设置 text 并且固定行高度
     */
    fun setTextWithFixedHeight(text: CharSequence?) {
        if (!text.isNullOrEmpty() && fixedLineHeight > 0) {
            val spannableString = SpannableString(text)
            spannableString.setSpan(FixedLineHeightSpan(fixedLineHeight), 0, spannableString.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            super.setText(spannableString)
        } else {
            super.setText(text)
        }
    }
}