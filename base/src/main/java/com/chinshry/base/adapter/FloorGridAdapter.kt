package com.chinshry.base.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.util.FloorUtil
import com.chinshry.base.view.ScrollGridView
import com.chinshry.base.util.CommonUtils.dp2px

/**
 * Created by chinshry on 2022/01/23.
 * Describe：楼层宫格 Adapter
 */
class FloorGridAdapter(
    data: MutableList<MutableList<ElementAttribute>>,
    private val view: ScrollGridView,
    private val viewWidth: Int,
    private val itemLayout: Int,
    private val buryPointInfo: BuryPointInfo?,
) : BaseQuickAdapter<MutableList<ElementAttribute>, BaseViewHolder>(0, data) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val gridView = GridLayout(context)
        gridView.layoutParams = RelativeLayout.LayoutParams(
            viewWidth,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        if (view.scrollScreen) {
            gridView.let {
               it.setPadding(
                   view.viewPadding - dp2px(context, view.itemDividerVerticalHeight / 2),
                   it.top,
                   view.viewPadding - dp2px(context, view.itemDividerVerticalHeight / 2),
                   it.bottom
               )
            }
        }
        gridView.columnCount = view.pagerColumnCount

        gridView.orientation = GridLayout.HORIZONTAL
        gridView.clipChildren = false
        gridView.clipToPadding = false

        return createBaseViewHolder(gridView)
    }

    override fun convert(holder: BaseViewHolder, item: MutableList<ElementAttribute>) {
        val gridView = holder.itemView as? GridLayout ?: return
        gridView.removeAllViews()

        var itemColumnSpanSum = 0

        item.forEach { element ->
            val elementItemView = View.inflate(context, itemLayout, null)
            FloorUtil.initFloorItem(
                element,
                elementItemView,
            )

            itemColumnSpanSum += element.occupiesColumns ?: 1
            val isFirstColumnItem = itemColumnSpanSum <= view.pagerColumnCount

            FloorUtil.addGridItem(
                gridView,
                elementItemView,
                isFirstColumnItem,
                element.occupiesColumns,
                element.occupiesRows,
                view.itemDividerVerticalHeight,
                view.itemDividerHorizontalHeight
            )

        }
    }

}
