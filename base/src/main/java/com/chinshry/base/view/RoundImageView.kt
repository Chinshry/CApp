package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.chinshry.base.R

/**
 * Created by chinshry on 2023/02/09.
 * Describe：圆角ImageView
 */
class RoundImageView(
    context: Context,
    attrs: AttributeSet
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private var radius = 0F
    
    init {
        initView(context, attrs)
    }
    
    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
    
        radius = typedArray.getDimension(
            R.styleable.RoundImageView_round_radius,
            context.resources.getDimension(R.dimen.default_round_imageview_radius)
        )
        typedArray.recycle()
    }
    
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val path = Path()
        path.addRoundRect(
            RectF(0F, 0F, width.toFloat(), height.toFloat()),
            radius,
            radius,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}