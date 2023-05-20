package com.chinshry.base.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Choreographer
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils

/**
 * Created by chinshry on 2023/05/11.
 * Describe：永久跑马灯Textview
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): FrameLayout(context, attrs, defStyleAttr)  {
    companion object {
        private const val TAG = "MarqueeTextView"
        private const val DEFAULT_SPEED = 0.5f
    }

    private val mTextView by lazy { FixedLineHeightTextView(context, attrs, defStyleAttr) }
    private var mBitmap: Bitmap? = null
    private val mFrameCallback: Choreographer.FrameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            mLeftX -= mSpeed
            invalidate()
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
    private var mLeftX = 0f

    /**
     * 文字滚动时，头尾的最小间隔距离
     */
    private val mSpace get() = mTextView.textSize

    /**
     * 文字滚动速度
     */
    private var mSpeed = DEFAULT_SPEED * 2

    init {
        mTextView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateBitmap()
            checkView()
        }
        setWillNotDraw(false)
    }

    fun setText(str: CharSequence?) {
        mTextView.text = str
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mTextView.measure(MeasureSpec.UNSPECIFIED, heightMeasureSpec)
        val mHeight = MeasureSpec.getSize(mTextView.measuredHeight)
        val newHeightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mTextView.layout(left, top, left + mTextView.measuredWidth, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        // "dispatchDraw:textView.measuredWidth=${mTextView.measuredWidth} width=$width mLeftX=$mLeftX canvas=$canvas".logd(TAG)
        mBitmap?.let { bitmap ->
            if (canScroll()) {
                if (mLeftX < -mTextView.measuredWidth - mSpace) {
                    mLeftX += (mTextView.measuredWidth + mSpace)
                }
                canvas.drawBitmap(bitmap, mLeftX, 0f, mTextView.paint)
                if (mLeftX + (mTextView.measuredWidth - width) < 0) {
                    canvas.drawBitmap(
                        bitmap, mTextView.measuredWidth + mLeftX + mSpace, 0f, mTextView.paint
                    )
                }
            } else {
                canvas.drawBitmap(bitmap, 0f, 0f, mTextView.paint)
            }
        }
    }

    private fun updateBitmap() {
        mBitmap = Bitmap.createBitmap(
            mTextView.measuredWidth,
            measuredHeight,
            Bitmap.Config.ARGB_8888
        )?.apply {
            mTextView.draw(Canvas(this))
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(mFrameCallback)
    }

    private fun checkView() {
        val exceedOneLine = canScroll()
        LogUtils.dTag(TAG, "checkView exceedOneLine=$exceedOneLine")
        if (exceedOneLine) {
            restartScroll()
        } else {
            stopScroll()
        }
    }

    fun canScroll(): Boolean {
        return mTextView.measuredWidth > this.width
    }

    fun startScroll() {
        if (canScroll()) {
            Choreographer.getInstance().postFrameCallback(mFrameCallback)
        }
    }

    fun stopScroll() {
        mLeftX = 0f
        Choreographer.getInstance().removeFrameCallback(mFrameCallback)
    }

    fun pauseScroll() {
        Choreographer.getInstance().removeFrameCallback(mFrameCallback)
    }

    fun restartScroll() {
        stopScroll()
        startScroll()
    }
}