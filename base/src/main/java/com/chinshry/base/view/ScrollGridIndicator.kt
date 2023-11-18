package com.chinshry.base.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.chinshry.base.R

/**
 * Created by chinshry on 2022/01/23.
 * Describe：滚动组件指示器
 */
class ScrollGridIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mTrackPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTrackRect: RectF = RectF()
    private var mRadius: Float = 0f
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRect: RectF = RectF()
    private var viewWidth: Int = 0
    private var mTrackColor = Color.parseColor("#e5e5e5")
    private var mIndicatorColor = Color.parseColor("#ff4646")

    // 可滑动次数
    var count = 1
        set(value) {
            field = value
            ratio = 1f / count
        }

    // 滑动进度比例
    var progress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    // thumb长度比例
    private var ratio = 1f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollGridIndicator)
        mTrackColor = typedArray.getColor(R.styleable.ScrollGridIndicator_trackColor, mTrackColor)
        mIndicatorColor = typedArray.getColor(R.styleable.ScrollGridIndicator_indicatorColor, mIndicatorColor)
        typedArray.recycle()

        mTrackPaint.color = mTrackColor
        mTrackPaint.style = Paint.Style.FILL
        mPaint.color = mIndicatorColor
        mPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        mTrackRect.set(0f, 0f, w * 1f, h * 1f)
        mRadius = h / 2f
    }

    /**
     * 设置指示器背景进度条的颜色
     * @param color 背景色
     */
    fun setTrackColor(@ColorInt color: Int) {
        mTrackPaint.color = color
        invalidate()
    }

    /**
     * 设置指示器的颜色
     * @param color 指示器颜色
     */
    fun setIndicatorColor(@ColorInt color: Int) {
        mPaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        isVisible = (ratio != 1F)

        //绘制背景
        canvas.drawRoundRect(mTrackRect, mRadius, mRadius, mTrackPaint)

        //计算指示器的长度和位置
        val leftOffset = viewWidth * (1f - ratio) * progress
        val left = mTrackRect.left + leftOffset
        val right = left + viewWidth * ratio
        mRect.set(left, mTrackRect.top, right, mTrackRect.bottom)

        //绘制指示器
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint)
    }

}