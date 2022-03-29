package com.chinshry.base.holder

import android.view.View
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.FloorData
import com.chinshry.base.view.ScrollGridView
import com.chinshry.base.R


/**
 * Created by chinshry on 2022/03/21.
 */
class FloorGridViewHolder {
    companion object {
        /**
         *
         * @param view View 楼层子View
         * @param floorData FloorData 楼层数据
         * @param itemLayout Int 子Item布局
         * @param buryPointInfo BuryPointInfo? 埋点
         * @param payloads List<*>? 局部刷新数据
         */
        fun holder(
            view: View,
            floorData: FloorData,
            itemLayout: Int,
            buryPointInfo: BuryPointInfo? = null,
            payloads: List<*>? = null,
        ) {
            val parentView = view.findViewById<ScrollGridView>(R.id.layout_floor_item)
            if (floorData.elementAttributes.isNullOrEmpty() || parentView == null) {
                return
            }

            // 获取局部刷新数据
            val payload: List<*>? = payloads?.getOrNull(0) as? List<*>
            val badgePositionList = mutableListOf<Int>()
            payload?.forEach { badgePositionList.add(it as Int) }

            if (badgePositionList.isNotEmpty()) {
                // 若局部刷新数据不为空 表明其他元素相同 仅刷新红点
                floorData.elementAttributes.let {
                    if (it.isNotEmpty()) {
                        parentView.refreshBadgeStatus()
                    }
                }
            } else {
                parentView.initData(
                    floorData,
                    itemLayout,
                    buryPointInfo,
                )
            }
        }
    }
}