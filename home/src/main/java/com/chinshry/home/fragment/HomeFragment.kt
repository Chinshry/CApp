package com.chinshry.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chinshry.base.BaseFragment
import com.chinshry.base.adapter.FloorAdapter
import com.chinshry.base.bean.*
import com.chinshry.base.util.CommonUtils.dp2px
import com.chinshry.base.util.getScrollYHeight
import com.chinshry.base.view.FloorDivider
import com.chinshry.base.view.CRefreshHeader
import com.chinshry.home.R
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 主页Fragment
 */
@BuryPoint(pageName = "主页Fragment")
class HomeFragment: BaseFragment() {

    private val floorAdapter: FloorAdapter by lazy { FloorAdapter(pageBuryPoint) }

    override fun setLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.itemAnimator?.changeDuration = 0
        recyclerView.setItemViewCacheSize(Constants.VIEW_CACHE_SIZE)
        recyclerView.addItemDecoration(FloorDivider())
        (recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false

        recyclerView.adapter = floorAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                initToolbar(recyclerView.getScrollYHeight())
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        refreshLayout.setOnRefreshListener { getFloorData() }
        refreshLayout.autoRefresh()
        // 适配刘海屏
        refreshLayout.refreshHeader?.let {
            refreshLayout.setRefreshHeader((it as CRefreshHeader).marginStatusBar())
        }
    }

    private fun initToolbar(scrollYHeight: Int) {
        val baseHeight = dp2px(context ?: requireContext(), 100f)

        val toolBarAlpha = when {
            scrollYHeight >= baseHeight -> {
                toolbar.visibility = View.VISIBLE
                1f
            }
            scrollYHeight == 0 -> {
                toolbar.visibility = View.GONE
                0f
            }
            else -> {
                toolbar.visibility = View.VISIBLE
                scrollYHeight / (baseHeight * 1f)
            }
        }

        // changeStatusBarColor()

        toolbar.alpha = toolBarAlpha
        tv_toolbar.alpha = toolBarAlpha
    }

    private fun getFloorData() {
        val dataSpanGrid = FloorData(
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_CARD,
            gridColumnsNum = 3F,
            gridRowNum = 4F,
            itemDividerH = 4f,
            itemDividerW = 10f,
            elementAttributes = listOf(
                ElementAttribute(occupiesColumns = 2, occupiesRows = 2, elementTitle = "1"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "2"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "3"),
                ElementAttribute(occupiesColumns = 2, occupiesRows = 1, elementTitle = "4"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 2, elementTitle = "5"),
                ElementAttribute(occupiesColumns = 2, occupiesRows = 1, elementTitle = "6"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "7"),
                ElementAttribute(occupiesColumns = 2, occupiesRows = 2, elementTitle = "8"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "9"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 2, elementTitle = "10"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "11"),
            )
        )

        val dataGrid = FloorData(
            titleFlag = true,
            title = "宫格-多行",
            hasMore = true,
            hasMoreText = "查看更多",
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_GRID,
            gridColumnsNum = 4F,
            gridRowNum = 2F,
            itemDividerW = 10f,
            elementAttributes = listOf(
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "1"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "2"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "3"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "4"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "5"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "6"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "7"),
            )
        )

        val dataGridScroll = FloorData(
            titleFlag = true,
            title = "宫格-滑动",
            hasMore = true,
            hasMoreText = "查看更多",
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_GRID,
            gridColumnsNum = 5F,
            gridRowNum = 1F,
            itemDividerW = 10f,
            elementAttributes = listOf(
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "1"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "2"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "3"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "4"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "5"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "6"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "7"),
            )
        )

        val dataBanner = FloorData(
            titleFlag = true,
            title = "banner",
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_BANNER,
            elementAttributes = listOf(
                ElementAttribute(elementTitle = "1"),
                ElementAttribute(elementTitle = "2"),
                ElementAttribute(elementTitle = "3")
            )
        )

        val dataCardScroll = FloorData(
            titleFlag = true,
            title = "卡片-单行滑动",
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_CARD,
            gridColumnsNum = 3.5F,
            gridRowNum = 1F,
            itemDividerW = 10f,
            elementAttributes = listOf(
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "1"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "2"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "3"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "4"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "5"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "6"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "7"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "8"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "9"),
            )
        )

        val dataCardScrollTwo = FloorData(
            titleFlag = true,
            title = "卡片-双行滑动",
            displayType = FloorUIData.FLOOR_DISPLAY_TYPE_CARD,
            gridColumnsNum = 2.5F,
            gridRowNum = 2F,
            itemDividerH = 4f,
            itemDividerW = 10f,
            elementAttributes = listOf(
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "1"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "2"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "3"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "4"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "5"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "6"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "7"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "8"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "9"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "10"),
                ElementAttribute(occupiesColumns = 1, occupiesRows = 1, elementTitle = "11"),
            )
        )

        floorAdapter.setList(
            listOf(
                // dataSpanGrid,
                dataGrid,
                dataGridScroll,
                dataBanner,
                dataCardScroll,
                dataCardScrollTwo,
            )
        )

        refreshLayout.finishRefresh(1000)


        // tab浮窗展示
        val floatData = FloatingWindowData(
            id = "0",
            buryPoint = "浮窗",
            delayShowTime = 3
        )

        floatView.initFloat(recyclerView, floatData)
    }

}