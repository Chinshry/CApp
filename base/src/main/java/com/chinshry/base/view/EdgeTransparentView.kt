package com.chinshry.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.chinshry.base.R
import com.chinshry.base.util.getDimensionById

/**
 * Created by chinshry on 2023/02/10.
 * Describe：边缘模糊的view
 */
open class EdgeTransparentView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
) : FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /** 标记边沿透明 **/
    var fadeEdgePosition = 0

    /** 边沿透明的宽度 **/
    var fadeEdgeWidth = 0f

    /** 当前view的宽度 **/
    private var mWidth = 0

    /** 当前view的高度 **/
    private var mHeight = 0

    /** 渐变颜色 **/
    private val mGradientColors = intArrayOf(Color.WHITE, Color.TRANSPARENT)

    /** 渐变位置 **/
    private val mGradientPosition = floatArrayOf(0f, 1f)

    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
    }

    companion object {
        const val POSITION_NONE = 0
        const val POSITION_TOP = 0x01
        const val POSITION_BOTTOM = POSITION_TOP shl 1
        const val POSITION_LEFT = POSITION_TOP shl 2
        const val POSITION_RIGHT = POSITION_TOP shl 3

        private const val DEGREES_TOP = 0f
        private const val DEGREES_RIGHT = 90f
        private const val DEGREES_BOTTOM = 180f
        private const val DEGREES_LEFT = 270f
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EdgeTransparentView)
        fadeEdgePosition = typedArray.getInt(R.styleable.EdgeTransparentView_edge_position, POSITION_NONE)
        fadeEdgeWidth = typedArray.getDimension(
            R.styleable.EdgeTransparentView_edge_width,
            getDimensionById(R.dimen.default_edge_transparent_width)
        )
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initShader()
        mWidth = width
        mHeight = height
    }

    private fun initShader() {
        mPaint.shader = LinearGradient(
            0f,
            0f,
            0f,
            fadeEdgeWidth,
            mGradientColors,
            mGradientPosition,
            Shader.TileMode.CLAMP
        )
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val layerSave = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        val drawChild = super.drawChild(canvas, child, drawingTime)
        if (fadeEdgePosition and POSITION_TOP != 0) {
            canvas.drawRect(DEGREES_TOP, 0f, mWidth.toFloat(), fadeEdgeWidth, mPaint)
        }
        if (fadeEdgePosition and POSITION_BOTTOM != 0) {
            val save = canvas.save()
            canvas.rotate(DEGREES_BOTTOM, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
            canvas.drawRect(0f, 0f, mWidth.toFloat(), fadeEdgeWidth, mPaint)
            canvas.restoreToCount(save)
        }
        val offset = (mHeight - mWidth) / 2
        if (fadeEdgePosition and POSITION_LEFT != 0) {
            val saveCount = canvas.save()
            canvas.rotate(DEGREES_LEFT, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
            canvas.translate(0f, offset.toFloat())
            canvas.drawRect(
                (0 - offset).toFloat(), 0f, (mWidth + offset).toFloat(), fadeEdgeWidth,
                mPaint
            )
            canvas.restoreToCount(saveCount)
        }
        if (fadeEdgePosition and POSITION_RIGHT != 0) {
            val saveCount = canvas.save()
            canvas.rotate(DEGREES_RIGHT, (mWidth / 2).toFloat(), (mHeight / 2).toFloat())
            canvas.translate(0f, offset.toFloat())
            canvas.drawRect(
                (0 - offset).toFloat(), 0f, (mWidth + offset).toFloat(), fadeEdgeWidth,
                mPaint
            )
            canvas.restoreToCount(saveCount)
        }
        canvas.restoreToCount(layerSave)
        return drawChild
    }
}