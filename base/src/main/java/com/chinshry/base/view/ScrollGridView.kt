package com.chinshry.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.chinshry.base.util.CommonUtils.dp2px
import com.chinshry.base.adapter.FloorGridAdapter
import com.chinshry.base.databinding.ScrollGridviewBinding
import kotlin.math.abs
import kotlin.math.ceil


/**
 * Created by chinshry on 2022/02/14.
 * Describe：宫格滚动组件
 */
class ScrollGridView(context: Context, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {
    private var adapter: BaseQuickAdapter<MutableList<ElementAttribute>, BaseViewHolder>? = null
    private val viewBinding: ScrollGridviewBinding

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
    /** 一屏展示行个数 **/
    private var gridRowNum: Int = 1
    /** 一屏展示列个数 **/
    private var gridColumnsNum: Float = 0f
        set(value) {
            field = value
            scrollScreen = value.toInt().toFloat() == value
            pagerColumnCount = if (scrollScreen) gridColumnsNum.toInt() else 1
        }

    private var startX: Int = 0
    private var startY: Int = 0

    constructor(context: Context) : this(context, null)

    init {
        viewBinding = ScrollGridviewBinding.inflate(LayoutInflater.from(context), this)
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

        gridColumnsNum = floorData.gridColumnsNum ?: itemDataList.size.toFloat()
        gridRowNum = floorData.gridRowNum?.toInt() ?: 1

        initPage(itemDataList, itemLayout, buryPointInfo)
        initIndicator()
    }

    /**
     * 红点刷新 重设数据
     */
    fun refreshBadgeStatus() {
        adapter?.let{ it.setList(it.data) }
    }

    private fun initPage(
        itemDataList: List<ElementAttribute>,
        itemLayout: Int,
        buryPointInfo: BuryPointInfo?
    ) {
        adapter = FloorGridAdapter(
            data = dealData(itemDataList),
            view = this,
            viewWidth = getItemViewWidth(),
            itemLayout = itemLayout,
            buryPointInfo = buryPointInfo
        )

        val snapHelper = CustomSnapHelper()
        snapHelper.attachToRecyclerView(viewBinding.recyclerView)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        viewBinding.recyclerView.adapter = adapter
    }

    /**
     * 设置item宽度
     * @return Int
     */
    private fun getItemViewWidth(): Int {
        return if (scrollScreen) {
            LayoutParams.MATCH_PARENT
        } else {
            // 头尾padding = 控件左边距 - (item间距/2)
            val pagePaddingHorizontal = viewPadding - dp2px(context, itemDividerVerticalHeight) / 2

            viewBinding.recyclerView.let { view ->
                view.setPadding(
                    pagePaddingHorizontal,
                    view.paddingTop,
                    pagePaddingHorizontal,
                    view.paddingBottom
                )
            }

            val percent = if (gridColumnsNum < 1f) 1f else (1 / gridColumnsNum)
            val screenAppWidth = ScreenUtils.getAppScreenWidth()

            // 屏幕上显示的子元素横向间距总宽度
            val dividerWidth = dp2px(context, itemDividerVerticalHeight) * gridColumnsNum.toInt()

            // 一个item的宽度 = (屏幕宽度 - 控件边距 - 屏幕上显示的子元素横向间距总宽度) * item显示占比 + 子元素横向间距
            ((screenAppWidth - viewPadding - dividerWidth) * percent).toInt() + dp2px(context, itemDividerVerticalHeight)
        }
    }

    private fun initIndicator() {
        // 半页滚动 不显示指示器
        viewBinding.indicator.isVisible = scrollScreen

        viewBinding.indicator.count =
            if (scrollScreen) {
                adapter?.itemCount ?: 1
            } else {
                // 暂未用到 因目前半屏滚动不显示指示器
                val columnCountSum = adapter?.itemCount ?: (1 * pagerColumnCount)
                // 滑动page = ceil((总column个数 - floor(一屏显示个数)) / 一次滚动个数) + 1
                ceil((columnCountSum - gridColumnsNum.toInt()) / pagerColumnCount.toFloat()).toInt() + 1
            }

        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offsetX = recyclerView.computeHorizontalScrollOffset()
                val range = recyclerView.computeHorizontalScrollRange()
                val extend = recyclerView.computeHorizontalScrollExtent()
                val progress: Float = offsetX * 1.0f / (range - extend)
                viewBinding.indicator.progress = progress
            }
        })
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val moveX = event.x.toInt() - startX
        val moveY = event.y.toInt() - startY

        startX = event.x.toInt()
        startY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                // 横向滑动距离 > 竖向滑动距离
                if (abs(moveX) > abs(moveY)) {
                    // parent拦截 在RecycleView内消费事件
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }

        return super.onInterceptTouchEvent(event)
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
                            pageItemColumn - itemColumnSpanSum
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
