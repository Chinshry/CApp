package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.chinshry.base.R
import com.chinshry.base.util.dp
import com.chinshry.base.util.getShape

/**
 * Created by chinshry on 2023/02/10.
 * Describe：标签View
 */
class LabelTextView (
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : AppCompatTextView(context, attrs, defStyleAttr) {
    companion object {
        private const val LAYOUT_PADDING_HORIZONTAL = 6
        private const val LAYOUT_PADDING_VERTICAL = 2
    }
    
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    
    init {
        setPadding(
            LAYOUT_PADDING_HORIZONTAL.dp,
            LAYOUT_PADDING_VERTICAL.dp,
            LAYOUT_PADDING_HORIZONTAL.dp,
            LAYOUT_PADDING_VERTICAL.dp
        )
        initView(context, attrs)
    }
    
    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        isVisible = text != null
    }
    
    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView)
    
        val labelTextSize = typedArray.getDimension(
            R.styleable.LabelTextView_label_text_size,
            0f
        )
        setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)
        
        val labelColor = typedArray.getColor(
            R.styleable.LabelTextView_label_color,
            Color.TRANSPARENT
        )
        
        setTextColor(labelColor)
        background = getShape(0.5F.dp, labelColor, null, 4.dp.toFloat())
        
        typedArray.recycle()
    }
}