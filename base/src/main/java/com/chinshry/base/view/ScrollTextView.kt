package com.chinshry.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.chinshry.base.R
import com.chinshry.base.databinding.LayoutScrollTextBinding
import com.chinshry.base.util.getBooleanById
import com.chinshry.base.util.getDimensionById
import com.chinshry.base.util.getIntegerById
import com.chinshry.base.util.setTextSizeById

/**
 * Created by chinshry on 2023/02/10.
 * Describe：可以滑动的TextView
 */
class ScrollTextView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : EdgeTransparentView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val viewBinding: LayoutScrollTextBinding
    private var viewMaxLines = 0
    private var viewMaxHeight = 0
    private var viewFadingEdgeEnable = true

    init {
        viewBinding = LayoutScrollTextBinding.inflate(LayoutInflater.from(context), this, true)
        initView(context, attrs)
    }

    companion object {
        const val SCROLL_DIRECTION_UP = -1
        const val SCROLL_DIRECTION_DOWN = 1
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(viewMaxHeight, MeasureSpec.AT_MOST)
        )
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        fadeEdgePosition =
            if (viewFadingEdgeEnable) {
                if (canScroll(SCROLL_DIRECTION_UP) && canScroll(SCROLL_DIRECTION_DOWN)) {
                    POSITION_TOP or POSITION_BOTTOM
                } else if (canScroll(SCROLL_DIRECTION_UP)) {
                    POSITION_TOP
                } else if (canScroll(SCROLL_DIRECTION_DOWN)) {
                    POSITION_BOTTOM
                } else {
                    POSITION_NONE
                }
            } else {
                POSITION_NONE
            }
        return super.drawChild(canvas, child, drawingTime)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextView)

        val textLineHeight = typedArray.getDimensionPixelSize(
            R.styleable.ScrollTextView_scroll_text_line_height,
            getDimensionById(R.dimen.default_scroll_text_view_text_line_height).toInt()
        )
        setTextLineHeight(textLineHeight)

        val text = typedArray.getString(R.styleable.ScrollTextView_scroll_text) ?: ""
        setScrollText(text)

        val textSize = typedArray.getDimension(
            R.styleable.ScrollTextView_scroll_text_size,
            getDimensionById(R.dimen.default_scroll_text_view_text_size)
        )
        setScrollTextSize(textSize)

        val textColor = typedArray.getColor(
            R.styleable.ScrollTextView_scroll_text_color,
            Color.WHITE
        )
        setTextColor(textColor)

        val textMaxLines = typedArray.getInteger(
            R.styleable.ScrollTextView_scroll_text_max_lines,
            getIntegerById(R.integer.default_scroll_text_view_text_max_lines)
        )
        setMaxLines(textMaxLines)

        val scrollBarEnable = typedArray.getBoolean(
            R.styleable.ScrollTextView_scroll_bar_enable,
            getBooleanById(R.bool.default_scroll_bar_enable)
        )
        setScrollBarEnable(scrollBarEnable)

        val fadingEdgeEnable = typedArray.getBoolean(
            R.styleable.ScrollTextView_scroll_fading_edge_enable,
            getBooleanById(R.bool.default_scroll_fading_edge_enable)
        )
        setFadingEdgeEnable(fadingEdgeEnable)

        typedArray.recycle()
    }

    /**
     * 设置是否显示边缘渐变
     * @param enable Boolean
     */
    fun setFadingEdgeEnable(enable: Boolean) {
        viewFadingEdgeEnable = enable
        invalidate()
    }

    /**
     * 或许是否显示边缘渐变
     * @return enable Boolean
     */
    fun getFadingEdgeEnable(): Boolean {
        return viewFadingEdgeEnable
    }

    /**
     * 滚动对齐文字顶部
     * @return Boolean
     */
    fun adjustScrollOffset() {
        val scrollOffset = viewBinding.slContainer.scrollY.mod(viewBinding.tvContent.getFixedLineHeight())
        if (scrollOffset != 0) {
            viewBinding.slContainer.smoothScrollBy(0, -scrollOffset)
        }
    }

    /**
     * 是否可以滚动(向上或向下)
     * @return Boolean
     */
    fun canScroll(): Boolean {
        return canScroll(SCROLL_DIRECTION_UP) || canScroll(SCROLL_DIRECTION_DOWN)
    }

    /**
     * 是否可以在某个方向滚动
     * @param direction Int
     * @return Boolean
     */
    fun canScroll(direction: Int): Boolean {
        return viewBinding.slContainer.canScrollVertically(direction)
    }

    /**
     * 滚动到顶部/底部
     * @param direction Int
     * @return Boolean
     */
    fun fullScroll(direction: Int): Boolean {
        return viewBinding.slContainer.fullScroll(direction)
    }

    /**
     * 按页滚动
     * @param direction Int
     * @return Boolean
     */
    fun pageScroll(direction: Int): Boolean {
        return viewBinding.slContainer.pageScroll(direction)
    }

    /**
     * 设置不超过一行则Gravity.START
     * @param text String
     */
    fun setGravityCenterExceedOneLine(text: String) {
        viewBinding.tvContent.apply {
            post {
                paint.textSize = textSize
                val exceedOneLine = paint.measureText(text) > width
                this.gravity = if (exceedOneLine) Gravity.START else Gravity.CENTER
            }
        }
    }

    /**
     * 设置滚动条是否打开
     * @param enable Boolean
     */
    fun setScrollBarEnable(enable: Boolean) {
        viewBinding.slContainer.isVerticalScrollBarEnabled = enable
    }

    /**
     * 获取滚动条是否打开
     * @return Boolean
     */
    fun getScrollBarEnable(): Boolean {
        return viewBinding.slContainer.isVerticalScrollBarEnabled
    }

    /**
     * 设置文本
     * @param text String
     */
    fun setScrollText(text: String) {
        viewBinding.tvContent.setTextWithFixedHeight(text)
    }

    /**
     * 设置文本大小
     * @param resId Int
     */
    fun setScrollTextSize(resId: Int) {
        viewBinding.tvContent.setTextSizeById(resId)
    }

    /**
     * 设置文本大小
     * @param textSize Float
     */
    fun setScrollTextSize(textSize: Float) {
        viewBinding.tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    private fun updateViewMaxHeight() {
        viewMaxHeight = viewMaxLines * getTextLineHeight()
        requestLayout()
    }

    /**
     * 设置text最大行数
     * @param lines Int
     */
    fun setMaxLines(lines: Int) {
        // viewBinding.tvContent.maxLines = lines
        viewMaxLines = lines
        updateViewMaxHeight()
    }

    /**
     * 设置文本行高
     * @param lineHeight Int
     */
    fun setTextLineHeight(lineHeight: Int) {
        viewBinding.tvContent.setFixedLineHeight(lineHeight)
        updateViewMaxHeight()
    }

    /**
     * 获取文本行高
     * @return Int
     */
    fun getTextLineHeight(): Int {
        return viewBinding.tvContent.getFixedLineHeight()
    }

    /**
     * 设置文本颜色
     * @param textColor Int
     */
    fun setTextColor(textColor: Int) {
        viewBinding.tvContent.setTextColor(textColor)
    }

    /**
     * 添加滚动监听
     * @param listener OnScrollChangeListener
     */
    fun addScrollListener(listener: OnScrollChangeListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.slContainer.setOnScrollChangeListener(listener)
        }
    }

    /**
     * 添加滚动监听
     * @param listener OnScrollChangeListener
     */
    fun setTextClickListener(listener: OnClickListener?) {
        viewBinding.tvContent.setOnClickListener(listener)
    }
}