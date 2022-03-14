package com.chinshry.base.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.util.FloorUtil
import com.chinshry.base.view.ViewPagerGridView
import com.chinshry.base.view.clickWithTrigger
import com.example.base.R

/**
 * Created by chinshry on 2022/01/23.
 * Describe：楼层adapter
 */
class GridAdapter(
    data: MutableList<MutableList<ElementAttribute>>,
    private val view: ViewPagerGridView,
    private val itemLayout: Int,
    private val buryPointInfo: BuryPointInfo?,
) : BaseQuickAdapter<MutableList<ElementAttribute>, BaseViewHolder>(0, data) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val gridView = GridLayout(context)
        gridView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        if (view.scrollScreen) {
            gridView.let {
               it.setPadding(
                   view.viewPadding - SizeUtils.dp2px(view.itemDividerVerticalHeight) / 2,
                   it.top,
                   view.viewPadding - SizeUtils.dp2px(view.itemDividerVerticalHeight) / 2,
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
        var itemColumnSpanSum = 0

        item.forEach { element ->
            val itemView = View.inflate(context, itemLayout, null)
            itemView.findViewById<TextView>(R.id.tv_title_text)?.text = element.elementTitle
            itemView.findViewById<ImageView>(R.id.iv_element)?.let {
                // Glide.with(context).load(pictureUrl).into(it)
                // Glide.with(context).load(R.mipmap.img_2).into(it)
            }
            itemView.clickWithTrigger {
                ToastUtils.showShort("click " + element.elementTitle)
            }
            if (!element.elementVisible) {
                itemView.visibility = View.INVISIBLE
            }

            itemColumnSpanSum += element.occupiesColumns ?: 1

            val isFirstColumnItem = itemColumnSpanSum <= view.pagerColumnCount

            FloorUtil.addGridItem(
                holder.itemView as GridLayout,
                itemView,
                isFirstColumnItem,
                element.occupiesColumns,
                element.occupiesRows,
                view.itemDividerVerticalHeight,
                view.itemDividerHorizontalHeight
            )

        }
    }

}
