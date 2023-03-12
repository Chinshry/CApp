package com.chinshry.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chinshry.base.BaseFragment
import com.chinshry.base.adapter.FloorAdapter
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Constants
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloatingWindowData
import com.chinshry.base.bean.FloorData
import com.chinshry.base.bean.FloorUIData
import com.chinshry.base.util.dp
import com.chinshry.base.util.getScrollYHeight
import com.chinshry.base.view.CRefreshHeader
import com.chinshry.base.view.FloorDivider
import com.chinshry.home.databinding.FragmentHomeBinding

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 主页Fragment
 */
@BuryPoint(pageName = "主页Fragment")
class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val floorAdapter: FloorAdapter by lazy { FloorAdapter(pageBuryPoint) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.isNestedScrollingEnabled = false
        viewBinding.recyclerView.itemAnimator?.changeDuration = 0
        viewBinding.recyclerView.setItemViewCacheSize(Constants.VIEW_CACHE_SIZE)
        viewBinding.recyclerView.addItemDecoration(FloorDivider())
        (viewBinding.recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false

        viewBinding.recyclerView.adapter = floorAdapter

        viewBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                initToolbar(recyclerView.getScrollYHeight())
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewBinding.refreshLayout.setOnRefreshListener { getFloorData() }
        viewBinding.refreshLayout.autoRefresh()
        // 适配刘海屏
        viewBinding.refreshLayout.refreshHeader?.let {
            viewBinding.refreshLayout.setRefreshHeader((it as CRefreshHeader).marginStatusBar())
        }
    }

    private fun initToolbar(scrollYHeight: Int) {
        val baseHeight = 100f.dp(context)

        val toolBarAlpha = when {
            scrollYHeight >= baseHeight -> {
                viewBinding.toolbar.visibility = View.VISIBLE
                1f
            }
            scrollYHeight == 0 -> {
                viewBinding.toolbar.visibility = View.GONE
                0f
            }
            else -> {
                viewBinding.toolbar.visibility = View.VISIBLE
                scrollYHeight / (baseHeight * 1f)
            }
        }

        // changeStatusBarColor()

        viewBinding.toolbar.alpha = toolBarAlpha
        viewBinding.tvToolbar.alpha = toolBarAlpha
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

        viewBinding.refreshLayout.finishRefresh(1000)


        // tab浮窗展示
        val floatData = FloatingWindowData(
            id = "0",
            buryPoint = "浮窗",
            delayShowTime = 3
        )

        viewBinding.floatView.initFloat(viewBinding.recyclerView, floatData)
    }

}