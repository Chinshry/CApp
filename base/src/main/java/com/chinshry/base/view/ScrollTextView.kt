package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.chinshry.base.R
import com.chinshry.base.databinding.LayoutScrollTextBinding

/**
 * Created by chinshry on 2023/02/10.
 * Describe：可以滑动的TextView
 */
class ScrollTextView (
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val viewBinding: LayoutScrollTextBinding

    init {
        viewBinding = LayoutScrollTextBinding.inflate(LayoutInflater.from(context), this, true)
        initView(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextView)

        val textLineHeight = typedArray.getDimensionPixelSize(
            R.styleable.ScrollTextView_scroll_text_line_height,
            0
        )
        setTextLineHeight(textLineHeight)

        val text = typedArray.getString(R.styleable.ScrollTextView_scroll_text) ?: ""
        setText(text)

        val textSize = typedArray.getDimension(
            R.styleable.ScrollTextView_scroll_text_size,
            0f
        )
        setTextSize(textSize)

        val textColor = typedArray.getColor(
            R.styleable.ScrollTextView_scroll_text_color,
            Color.WHITE
        )
        setTextColor(textColor)

        typedArray.recycle()
    }

    fun setText(text: String) {
        viewBinding.tvContent.setTextWithFixedHeight(text)
    }

    fun setTextSize(textSize: Float) {
        viewBinding.tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setTextLineHeight(lineHeight: Int) {
        viewBinding.tvContent.setFixedLineHeight(lineHeight)
    }

    fun setTextColor(textColor: Int) {
        viewBinding.tvContent.setTextColor(textColor)
    }
}