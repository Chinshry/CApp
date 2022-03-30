package com.chinshry.base.adapter

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.bean.FloorUIData.Companion.getFloorType
import com.chinshry.base.holder.FloorBannerViewHolder
import com.chinshry.base.holder.FloorGridViewHolder
import com.chinshry.base.util.CommonUtils
import com.chinshry.base.util.FloorUtil
import com.chinshry.base.util.FloorUtil.getItemLayout
import com.chinshry.base.R
import com.chinshry.base.bean.*

/**
 * Created by chinshry on 2022/03/21.
 * Describe：楼层Adapter
 */
open class FloorAdapter(val buryPointInfo: BuryPointInfo? = null) :
    BaseMultiItemQuickAdapter<FloorData, BaseViewHolder>() {

    init {
        addSupportViewType(-1, R.layout.layout_empty)
        addSupportViewType(FloorUIData.FloorType.GRID.value, R.layout.common_floor_grid)
        addSupportViewType(FloorUIData.FloorType.CARD.value, R.layout.common_floor_grid)
        addSupportViewType(FloorUIData.FloorType.BANNER.value, R.layout.common_floor_banner)

        this.setDiffCallback(DiffFloorDataCallback())
    }

    // 根据displayType styleType 获取楼层布局
    override fun getMultiItemType(data: List<FloorData>, position: Int): Int? {
        data.getOrNull(position)?.styleType?.let {
            return getFloorType(it)
        }
        return null
    }

    override fun convertHolder(holder: BaseViewHolder, item: FloorData, payloads: List<Any>) {
        if (CommonUtils.compareAppVersion(item.appVersion)) {
            return
        }

        FloorUtil.initFloorAddHeader(
            item,
            holder.itemView,
            buryPointInfo
        )

        when (item.itemType) {
            FloorUIData.FloorType.GRID.value -> {
                val itemLayout = getItemLayout(item.gridPlateType)
                FloorGridViewHolder.holder(holder.itemView, item, itemLayout, buryPointInfo, payloads = payloads)
            }
            FloorUIData.FloorType.CARD.value -> {
                FloorGridViewHolder.holder(holder.itemView, item, R.layout.common_floor_grid_img_item, buryPointInfo = buryPointInfo)
            }
            FloorUIData.FloorType.BANNER.value -> {
                FloorBannerViewHolder.holder(holder.itemView, item, buryPointInfo = buryPointInfo)
            }
        }
    }

    class DiffFloorDataCallback : DiffUtil.ItemCallback<FloorData>() {
        /**
         * 判断是否是同一个item
         *
         * @param oldItem New data
         * @param newItem old Data
         * @return
         */
        override fun areItemsTheSame(
            @NonNull oldItem: FloorData,
            @NonNull newItem: FloorData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * 当是同一个item时，再判断内容是否发生改变
         *
         * @param oldItem New data
         * @param newItem old Data
         * @return
         */
        override fun areContentsTheSame(
            @NonNull oldItem: FloorData,
            @NonNull newItem: FloorData
        ): Boolean {
            if (shouldRefreshAll(oldItem, newItem)) {
                return false
            }

            return getBadgeLimitFlagItem(oldItem).isEmpty()
        }

        /**
         * 可选实现
         * 如果需要精确修改某一个view中的内容，请实现此方法。
         * 如果不实现此方法，或者返回null，将会直接刷新整个item。
         *
         * @param oldItem Old data
         * @param newItem New data
         * @return Payload info. if return null, the entire item will be refreshed.
         */
        override fun getChangePayload(
            @NonNull oldItem: FloorData,
            @NonNull newItem: FloorData
        ): List<*>? {
            if (shouldRefreshAll(oldItem, newItem)) {
                return null
            }

            return getBadgeLimitFlagItem(oldItem)
        }

        /**
         * 是否是例外 即始终需要刷新的楼层
         * @param oldItemType Int
         * @return Boolean
         */
        private fun isExtraFloor(oldItemType: String?):Boolean {
            when(oldItemType) {
                FloorUIData.FloorType.INSURANCE_PRODUCE.type,
                FloorUIData.FloorType.RECEPTION_INFO.type -> {
                    return true
                }
            }
            return false
        }

        /**
         * 是否为全量刷新
         * @param oldItem FloorData
         * @param newItem FloorData
         * @return Boolean
         */
        private fun shouldRefreshAll(
            @NonNull oldItem: FloorData,
            @NonNull newItem: FloorData
        ): Boolean {
            // 添加例外 始终需要刷新的楼层
            if (isExtraFloor(oldItem.styleType)) {
                return true
            }

            if (oldItem.elementAttributes.isNullOrEmpty() || newItem.elementAttributes.isNullOrEmpty()) {
                return true
            }

            if (oldItem.elementAttributes != newItem.elementAttributes) {
                return true
            }

            return false
        }

         /**
         * 子元素中是否有badgeFlag为1的红点
         */
        private fun getBadgeLimitFlagItem(floorData: FloorData?): List<Int> {
            val itemPositionList = mutableListOf<Int>()

             // 只检查宫格楼层
            if (
                floorData?.displayType != FloorUIData.FLOOR_DISPLAY_TYPE_GRID &&
                floorData?.styleType != FloorUIData.FLOOR_FLOOR_TYPE_RECEPTION_CUSTOM
            ) return itemPositionList

            floorData.elementAttributes?.forEachIndexed { index, element ->
                // 只刷新有配置时间值的item
                if (element.eliminationLogic ?: 0L > 0L) {
                    itemPositionList.add(index)
                }
            }
            return itemPositionList
        }
    }

    open fun setList(list: List<FloorData>?) {
        this.setDiffNewData(list?.toMutableList())
    }

}