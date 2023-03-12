package com.chinshry.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.R
import com.chinshry.base.adapter.Scroll3DAdapter
import com.chinshry.base.databinding.LayoutScroll3dBinding
import com.chinshry.base.transformer.Scroll3DTransformer
import com.chinshry.base.util.getBooleanById
import com.chinshry.base.util.getDimensionById
import com.chinshry.base.util.getIntegerById

/**
 * Created by chinshry on 2023/03/11.
 * Describe：3D滑动组件
 */
class Scroll3DView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val viewBinding: LayoutScroll3dBinding

    private val scroll3DAdapter by lazy { Scroll3DAdapter() }

    private var scrollLoop = false
    private var scrollOffsetCount = 0

    private val positions = listOf(0, 1, 2)
    private val scales = listOf(1f, 0.6f, 0.3f)
    private val alphas = listOf(1f, 0.6f, 0f)
    private val rotationsY = listOf(0f, 20f, 20f)
    private val translationsX = listOf(0f, 0.4f, 1.2f)
    private val translationsY = listOf(0f, 0.13f, 0.13f)

    init {
        viewBinding = LayoutScroll3dBinding.inflate(LayoutInflater.from(context), this, true)
        initView(context, attrs)
        initPager()
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Scroll3DView)

        val itemWidth = typedArray.getDimensionPixelSize(
            R.styleable.Scroll3DView_scroll_3d_item_width,
            getDimensionById(R.dimen.default_scroll_3d_view_item_width).toInt()
        )
        setItemWidth(itemWidth)

        val scrollOffsetCount = typedArray.getInteger(
            R.styleable.Scroll3DView_scroll_3d_offset_count,
            getIntegerById(R.integer.default_scroll_3d_view_offset_count)
        )
        setScrollOffsetCount(scrollOffsetCount)

        val scrollLoop = typedArray.getBoolean(
            R.styleable.Scroll3DView_scroll_3d_loop,
            getBooleanById(R.bool.default_scroll_3d_loop)
        )
        setScrollLoop(scrollLoop)

        typedArray.recycle()
    }

    private fun initPager() {
        viewBinding.viewpager2.adapter = scroll3DAdapter
        setPageTransformer(Scroll3DTransformer(positions, scales, alphas, rotationsY, translationsX, translationsY))
    }

    fun setData(data: List<Int>) {
        if (scrollLoop && data.size >= scrollOffsetCount) {
            val startData = data.takeLast(scrollOffsetCount)
            val endData = data.take(scrollOffsetCount)
            scroll3DAdapter.data = startData + data + endData
            viewBinding.viewpager2.currentItem = scrollOffsetCount
            viewBinding.viewpager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        // 5 6 1 2 3 4 5 6 1 2
                        // 0 1 2 3 4 5 6 7 8 9
                        val position = viewBinding.viewpager2.currentItem
                        val newPosition = if (position < scrollOffsetCount) {
                            position + data.size
                        } else if (position >= scrollOffsetCount + data.size) {
                            position - data.size
                        } else {
                            position
                        }
                        viewBinding.viewpager2.setCurrentItem(newPosition, false)
                    }
                }
            })
        } else {
            scroll3DAdapter.data = data
        }
    }

    private fun setItemWidth(itemWidth: Int) {
        val recyclerView = (viewBinding.viewpager2.getChildAt(0) as? RecyclerView) ?: return
        recyclerView.clipChildren = false
        recyclerView.clipToPadding = false

        val paddingHorizontal = (ScreenUtils.getAppScreenWidth() - itemWidth) / 2
        recyclerView.setPadding(
            paddingHorizontal,
            recyclerView.paddingTop,
            paddingHorizontal,
            recyclerView.paddingBottom
        )
    }
    private fun setScrollOffsetCount(scrollOffsetCount: Int) {
        this.scrollOffsetCount = scrollOffsetCount
        viewBinding.viewpager2.offscreenPageLimit = scrollOffsetCount
    }

    private fun setScrollLoop(scrollLoop: Boolean) {
        this.scrollLoop = scrollLoop
    }

    private fun setPageTransformer(transformer: ViewPager2.PageTransformer) {
        viewBinding.viewpager2.setPageTransformer(transformer)
    }
}