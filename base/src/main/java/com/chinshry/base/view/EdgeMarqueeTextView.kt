package com.chinshry.base.view

import android.content.Context
import android.util.AttributeSet

/**
 * Created by chinshry on 2023/05/11.
 * Describe：永久跑马灯Textview 右边缘模糊
 */
open class EdgeMarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EdgeTransparentView(context, attrs, defStyleAttr) {
    private val textView by lazy { MarqueeTextView(context, attrs, defStyleAttr) }

    init {
        initView()
    }

    private fun initView() {
        textView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            fadeEdgePosition = if (textView.canScroll()) POSITION_RIGHT else POSITION_NONE
        }
        addView(textView)
    }

    /**
     * 设置文本
     * @param str CharSequence?
     */
    fun setText(str: CharSequence?) {
        textView.setText(str)
    }
}