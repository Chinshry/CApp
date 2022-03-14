package com.chinshry.base.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.*
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.adapter.GridAdapter
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.example.base.R
import kotlinx.android.synthetic.main.viewpager_grid.view.*
import kotlin.math.ceil


/**
 * Created by chinshry on 2022/02/14.
 * Describe：宫格滚动组件
 */
class ViewPagerGridView(context: Context, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {
    private var adapter: BaseQuickAdapter<MutableList<ElementAttribute>, BaseViewHolder>? = null
    /** 楼层水平间距 **/
    var viewPadding: Int = SizeUtils.dp2px(15f)
        set(value) {
            field = value
            invalidate()
        }

    /** 是否整屏滚动 **/
    var scrollScreen: Boolean = true
    /** 每个pager可展示的列数 **/
    var pagerColumnCount = 0
    /** item横间距 **/
    var itemDividerHorizontalHeight: Float = 0f
    /** item竖间距 **/
    var itemDividerVerticalHeight: Float = 0f
    /** 每次滚动移动pager 当前默认每次滚动1个pager **/
    private var scrollOffset: Int = 1
    /** 一屏展示行个数 **/
    private var gridRowNum: Int = 1
    /** 一屏展示列个数 **/
    private var gridColumnsNum: Float = 0f
        set(value) {
            field = value
            scrollScreen = value.toInt().toFloat() == value
            pagerColumnCount = if (scrollScreen) gridColumnsNum.toInt() else scrollOffset
        }

    /** 最后一屏占位符个数 **/
    private var lastPageSpaceColumnCount = 0

    /******************** 非整屏滚动使用属性 ********************/
    /** 非整屏滚动item宽度 **/
    private var scrollItemWidth: Int = 0

    private var startX: Int = 0
    private var startY: Int = 0

    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.viewpager_grid, this)
    }

    /**
     * 初始化楼层数据
     * @param floorData FloorData? 楼层数据
     * @param itemLayout Int 子元素布局
     * @param buryPointInfo BuryPointInfo? 埋点
     */
    fun initData(
        floorData: FloorData?,
        itemLayout: Int,
        buryPointInfo: BuryPointInfo?,
    ) {
        val itemDataList = floorData?.elementAttributes ?: return

        itemDividerHorizontalHeight = floorData.itemDividerH ?: 0f
        itemDividerVerticalHeight = floorData.itemDividerW ?: 0f
        scrollOffset = floorData.scrollOffset ?: 1

        gridColumnsNum = floorData.gridColumnsNum ?: itemDataList.size.toFloat()
        gridRowNum = floorData.gridRowNum?.toInt() ?: 1

        initPage(itemDataList, itemLayout, buryPointInfo)
        initIndicator()
    }

    private fun initPage(
        itemDataList: List<ElementAttribute>,
        itemLayout: Int,
        buryPointInfo: BuryPointInfo?
    ) {
        setRecyclerViewWidth()

        adapter = GridAdapter(
            data = dealData(itemDataList),
            view = this,
            itemLayout = itemLayout,
            buryPointInfo = buryPointInfo
        )
        viewpager2.adapter = adapter
        viewpager2.offscreenPageLimit = adapter?.itemCount ?: 1
    }

    private fun setRecyclerViewWidth() {
        val recyclerView = (viewpager2.getChildAt(0) as? RecyclerView)

        recyclerView?.let { view ->
            view.clipChildren = false
            view.clipToPadding = false

            if (scrollScreen) {
                view.setPadding(
                    0,
                    view.paddingTop,
                    0,
                    view.paddingBottom
                )
            } else {
                val percent = 1 / gridColumnsNum
                val screenAppWidth = ScreenUtils.getAppScreenWidth()

                // 屏幕上显示的间距总宽度
                val dividerWidth = SizeUtils.dp2px(itemDividerVerticalHeight) * gridColumnsNum.toInt()

                // 一个item的宽度 = (屏幕宽度 - 控件左边距 - 屏幕上显示的间距总宽度) * item显示占比
                scrollItemWidth = ((screenAppWidth - viewPadding - dividerWidth) * percent).toInt()
                // pager宽度
                val pagerWidth = (scrollItemWidth + SizeUtils.dp2px(itemDividerVerticalHeight)) * scrollOffset
                // pager左边距 = 控件左边距 - (item间距/2)
                val pagePaddingLeft = viewPadding - SizeUtils.dp2px(itemDividerVerticalHeight) / 2
                // pager右边距 = 屏幕宽度 - pager宽度 - pager左边距
                val pagePaddingRight = screenAppWidth - pagerWidth - pagePaddingLeft

                view.setPadding(
                    pagePaddingLeft,
                    view.paddingTop,
                    pagePaddingRight,
                    view.paddingBottom
                )
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initIndicator() {
        val pageCount = adapter?.itemCount ?: return
        val columnCountSum = pageCount * pagerColumnCount - lastPageSpaceColumnCount
        // 滑动page = ceil((总column个数 - floor(一屏显示个数)) / 一次滚动个数) + 1
        val scrollableCount = ceil((columnCountSum - gridColumnsNum.toInt()) / pagerColumnCount.toFloat()).toInt() + 1

        indicator.count =
            if (scrollScreen) {
                pageCount
            } else {
                scrollableCount
            }

        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicator.currentIndex = if (pageCount == 1) {
                    0
                } else {
                    position
                }

                // 非整页滑动 && 超过一页 && 为最后一页
                if (!scrollScreen && indicator.count != 0 && indicator.progress == 1f ) {
                    // 快速滑动超页处理
                    viewpager2.currentItem = indicator.count - 2
                    scrollLast(true)
                }
            }

        }

        viewpager2.registerOnPageChangeCallback(pageChangeCallback)
    }

    /**
     * 最后一个page滑动通过动画实现
     * @param toLast Boolean
     */
    private fun scrollLast(toLast: Boolean) {
        val recyclerView = adapter?.recyclerView ?: return
        val pageCount = adapter?.itemCount ?: return
        val columnCountSum = pageCount * pagerColumnCount - lastPageSpaceColumnCount

        val invisibleItemCount = columnCountSum - (ceil(gridColumnsNum) + pagerColumnCount * (indicator.count - 2))
        val invisibleItemWidth = invisibleItemCount * (scrollItemWidth + SizeUtils.dp2px(itemDividerVerticalHeight))
        // 半item未显示宽度
        val lastViewInvisibleWidth = scrollItemWidth * (1 - gridColumnsNum % 1)
        // 移动距离 = 半item未显示宽度 + 未显示item宽度 + 控件横向padding
        val lastViewOffset = lastViewInvisibleWidth + invisibleItemWidth + viewPadding

        var animatorOffsetList = listOf(0f, - lastViewOffset )
        if (!toLast) {
            animatorOffsetList = animatorOffsetList.reversed()
        }
        val indicatorOffset = if (toLast) 1 else -1

        // 移动动画
        recyclerView.children.forEach { view ->
            val animator = ObjectAnimator.ofFloat(view, "translationX", animatorOffsetList[0] , animatorOffsetList[1])
            animator.duration = 200
            animator.start()
        }

        indicator.currentIndex = indicator.currentIndex + indicatorOffset
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // 整页滚动 不拦截
        if (scrollScreen) {
            return false
        }

        var intercepted = false
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (x - startX > RecyclerView.TOUCH_SLOP_PAGING) {
                    if (indicator.progress == 1f) {
                        scrollLast(toLast = false)
                        intercepted = true
                    }
                } else if (x - startX < -RecyclerView.TOUCH_SLOP_PAGING) {
                    if (indicator.currentIndex + 2 >= indicator.count) {
                        if (indicator.progress < 1f) {
                            scrollLast(toLast = true)
                        }
                        intercepted = true
                    }
                }
            }
        }
        startX = x
        startY = y
        return intercepted
    }

    /**
     * 数据分页处理
     * @param itemDataList List<ElementAttribute>?
     * @return MutableList<MutableList<ElementAttribute>>
     */
    private fun dealData(itemDataList: List<ElementAttribute>): MutableList<MutableList<ElementAttribute>> {
        val dataPageList = mutableListOf<MutableList<ElementAttribute>>()

        // 每页可展示的 GridColumnSpan和
        val pageItemColumn = pagerColumnCount
        // 每页可展示的 GridSpan乘积和
        val pageItemSpanSum = pagerColumnCount * gridRowNum

        // 子item的 GridColumnSpan和
        var itemColumnSpanSum = 0
        // 子item的 GridSpan乘积和
        var itemSpanSum = 0

        val dataList = mutableListOf<ElementAttribute>()

        itemDataList.forEachIndexed { index, it ->
            val itemOccupiesColumns = it.occupiesColumns ?: 1
            val itemOccupiesRows = it.occupiesRows ?: 1
            val itemOccupies = itemOccupiesColumns * itemOccupiesRows

            if (itemOccupiesColumns > 1 || itemOccupiesRows > 1) scrollScreen = true

            if (itemSpanSum  + itemOccupies > pageItemSpanSum) {
                itemSpanSum = 0
                itemColumnSpanSum = 0
                dataPageList.add(dataList.toMutableList())
                dataList.clear()
            }

            itemSpanSum += itemOccupiesColumns * itemOccupiesRows
            itemColumnSpanSum += itemOccupiesColumns

            dataList.add(it)

            // 占位
            if (index + 1 == itemDataList.size) {
                val spaceItemCount =
                    when {
                        itemColumnSpanSum < pageItemColumn -> {
                            lastPageSpaceColumnCount = pageItemColumn - itemColumnSpanSum
                            lastPageSpaceColumnCount
                        }
                        itemSpanSum < pageItemSpanSum -> {
                            pageItemSpanSum - itemSpanSum
                        }
                        else -> {
                            0
                        }
                    }

                for( i in 0 until spaceItemCount) {
                    val spaceElementData = ElementAttribute(elementVisible = false)
                    dataList.add(spaceElementData)
                }
            }
        }

        dataPageList.add(dataList.toMutableList())

        return dataPageList
    }

}