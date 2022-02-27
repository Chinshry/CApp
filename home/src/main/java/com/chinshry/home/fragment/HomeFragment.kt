package com.chinshry.home.fragment

import android.os.Bundle
import android.view.View
import com.chinshry.base.BaseFragment
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.chinshry.base.view.ViewPagerGridView
import com.chinshry.home.R
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 主页Fragment
 */
@BuryPoint(pageName = "主页Fragment")
class HomeFragment: BaseFragment() {

    override fun setLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val data0 = FloorData(
            gridColumnsNum = 3F,
            gridRowNum = 4F,
            scrollOffset = 1,
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

        val data1 = FloorData(
            gridColumnsNum = 3F,
            gridRowNum = 1F,
            scrollOffset = 1,
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

        val data2 = FloorData(
            gridColumnsNum = 3.5F,
            gridRowNum = 1F,
            scrollOffset = 1,
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

        val data3 = FloorData(
            gridColumnsNum = 3.5F,
            gridRowNum = 1F,
            scrollOffset = 2,
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

        val data4 = FloorData(
            gridColumnsNum = 2.5F,
            gridRowNum = 2F,
            scrollOffset = 1,
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


        addGridView(data1)
        addGridView(data2)
        addGridView(data3)
        addGridView(data4)
        addGridView(data0)
    }

    private fun addGridView(testDate: FloorData) {
        val view = ViewPagerGridView(requireContext())
        // view.initData(testDate, R.layout.common_floor_grid_item)
        view.initData(testDate, R.layout.common_floor_grid_img_item, null)
        view.clipChildren = false
        view.clipToPadding = false
        view.setPadding(view.paddingLeft, 50 , view.paddingRight, view.paddingBottom)
        ll.addView(view)
    }
}