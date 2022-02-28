package com.chinshry.base.util

import android.view.View
import android.widget.GridLayout
import com.blankj.utilcode.util.SizeUtils

/**
 * Created by chinshry on 2022/01/23.
 * Describe：楼层工具类
 */
object FloorUtil {
    fun addGridItem(
        parent: GridLayout,
        itemView: View,
        isFirstColumnItem: Boolean? = true,
        occupiesColumns: Int? = null,
        occupiesRows: Int? = null,
        itemDividerVerticalHeight: Float?,
        itemDividerHorizontalHeight: Float?
    ) {
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.width = 0
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, occupiesColumns ?: 1, (occupiesColumns ?: 1).toFloat())
        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, occupiesRows ?: 1, (occupiesRows ?: 1).toFloat())

        parent.addView(itemView, layoutParams)

        // 添加左右边距
        layoutParams.leftMargin = SizeUtils.dp2px(itemDividerVerticalHeight ?: 0f) / 2
        layoutParams.rightMargin = SizeUtils.dp2px(itemDividerVerticalHeight ?: 0f) / 2
        // 非第一行item 添加上边距
        if (isFirstColumnItem == false) {
            layoutParams.topMargin = SizeUtils.dp2px(itemDividerHorizontalHeight ?: 0f)
        }
    }

}