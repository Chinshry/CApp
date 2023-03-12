package com.chinshry.base.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.chinshry.base.R
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.Constants
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.chinshry.base.bean.FloorUIData
import com.chinshry.base.view.buryPoint
import com.chinshry.base.view.clickWithTrigger

/**
 * Created by chinshry on 2022/01/23.
 * Describe：楼层工具类
 */
object FloorUtil {
    /**
     * 楼层头部数据设置
     * @param floorData 楼层数据
     * @param headerView View
     */
    fun initFloorAddHeader(
        floorData: FloorData?,
        headerView: View,
        buryPointInfo: BuryPointInfo?
    ) {
        floorData?.apply {
            val floorView = headerView.findViewById<LinearLayout>(R.id.common_floor_ll)
            val floorBgView = headerView.findViewById<ImageView>(R.id.common_floor_bg)
            val headerLayout = headerView.findViewById<RelativeLayout>(R.id.layout_header)
            val titleView = headerView.findViewById<TextView>(R.id.tv_title)
            val moreLayout = headerView.findViewById<RelativeLayout>(R.id.rl_more)
            val moreTextView = headerView.findViewById<TextView>(R.id.tv_more)
            val moreImageView = headerView.findViewById<ImageView>(R.id.iv_more)

            // 动态加载楼层背景图
            if (!imageUrl.isNullOrBlank()) {
                floorBgView?.apply {
                    visibility = View.VISIBLE

                    var url = ""
                    var errorPic = R.color.transparent

                    imageUrl?.let {
                        if (it.isDigitsOnly()) {
                            errorPic = it.toInt()
                        } else {
                            url = getImageUrl(it)
                        }
                    }

                    Glide.with(context)
                        .load(url)
                        .placeholder(drawable)
                        .error(errorPic)
                        .into(this)
                }
            } else {
                floorBgView?.visibility = View.GONE
            }

            // 楼层上下间距
            floorView?.let {
                floorView.setPadding(
                    floorView.paddingStart,
                    marginTop?.dp(floorView.context) ?: floorView.paddingTop,
                    floorView.paddingEnd,
                    marginBottom?.dp(floorView.context) ?: floorView.paddingBottom
                )
            }

            // 是否展示头部
            if (titleFlag == true || hasMore == true) {
                headerLayout?.visibility = View.VISIBLE

                // 头部标题
                if (titleFlag == true) {
                    titleView?.text = title
                    if (!titleColor.isNullOrBlank()) {
                        titleView?.setTextColor(Color.parseColor(titleColor))
                    }
                }

                // 头部更多
                if (hasMore == true) {
                    moreLayout?.visibility = View.VISIBLE
                    moreTextView?.text = hasMoreText
                    if (!hasMoreTextColor.isNullOrBlank()) {
                        moreTextView?.setTextColor(Color.parseColor(hasMoreTextColor))
                        moreImageView?.imageTintList =
                            ColorStateList.valueOf(Color.parseColor(hasMoreTextColor))
                    }
                    moreLayout?.buryPoint = buryPointInfo
                    moreLayout?.clickWithTrigger(getElementBury(hasMoreText)) {
                        // TODO CLICK ITEM
                    }

                } else {
                    moreLayout?.visibility = View.GONE
                }
            } else {
                headerLayout?.visibility = View.GONE
            }

        }
    }

    /**
     * 楼层item数据设置
     * @param itemData item数据
     * @param itemView 子布局
     */
    fun initFloorItem(
        itemData: ElementAttribute?,
        itemView: View,
        buryPointInfo: BuryPointInfo?,
    ) {
        val context: Context = ActivityUtils.getTopActivity().applicationContext

        itemData?.apply {
            val elementView = itemView.findViewById<ConstraintLayout>(R.id.iv_element_bg_color)
            val titleView = itemView.findViewById<TextView>(R.id.tv_title)
            val imageView = itemView.findViewById<ImageView>(R.id.iv_element)
            val badgeView = itemView.findViewById<ImageView>(R.id.element_badge)

            if (elementVisible == false) {
                itemView.visibility = View.INVISIBLE
            } else {
                itemView.visibility = View.VISIBLE
            }

            // 子元素背景色
            if (!elementBackgroundColor.isNullOrBlank()) {
                elementView?.setBackgroundColor(Color.parseColor(elementBackgroundColor))
            }

            // 子元素标题
            if (elementTitle.isNullOrBlank()) {
                titleView?.visibility = View.GONE
            } else {
                titleView?.visibility = View.VISIBLE
                titleView?.text = elementTitle
                if (!elementTitleColor.isNullOrBlank()) {
                    titleView?.setTextColor(Color.parseColor(elementTitleColor))
                }
            }

            // 子元素图片
            Glide.with(context)
                .load(getImageUrl(elementPicture).ifBlank { R.color.empty })
                .into(imageView)

            // 子元素红点
            if (BadgeUtils.showBadge(BadgeType.FLOOR, this)) {
                badgeView?.visibility = View.VISIBLE
                Glide.with(context).load(getImageUrl(angleMark)).into(badgeView)
            } else {
                badgeView?.visibility = View.GONE
            }

            itemView.buryPoint = buryPointInfo
            itemView.clickWithTrigger(getElementBury(elementTitle, buryPoint)) {
                // 红点逻辑
                BadgeUtils.handleBadgeClick(BadgeType.FLOOR, badgeView, this)
                // TODO CLICK ITEM
            }

        }

    }

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
        layoutParams.leftMargin = ((itemDividerVerticalHeight ?: 0f) / 2).dp(parent.context).toInt()
        layoutParams.rightMargin = ((itemDividerVerticalHeight ?: 0f) / 2).dp(parent.context).toInt()
        // 非第一行item 添加上边距
        if (isFirstColumnItem == false) {
            layoutParams.topMargin = (itemDividerHorizontalHeight ?: 0f).dp(parent.context).toInt()
        }
    }

    /**
     * 根据gridPlateType获取子元素布局 默认R.layout.common_floor_grid_item
     * @param gridPlateType Int?
     * @return Int
     */
    fun getItemLayout(gridPlateType: String?): Int {
        return when (gridPlateType) {
            FloorUIData.GRID_PLATE_TYPE_BIG -> R.layout.big_floor_grid_item
            else -> R.layout.common_floor_grid_item
        }
    }

    /**
     * 获取图片地址 拼接CONFIG_CENTER_BASE_URL 为空返回空字符串
     */
    fun getImageUrl(url: String?): String {
        return Constants.TEST_IMG_URL

        return when {
            url.isNullOrBlank() -> {
                ""
            }
            url.startsWith("http") -> {
                url
            }
            else -> {
                Constants.CONFIG_CENTER_BASE_URL + url
            }
        }
    }

    /**
     * 获取楼层元素埋点ButtonName
     * 优先级 buryPoint > elementTitle
     * @return String
     */
    fun getElementBury(title: String?, buryPoint: String? = null): String {
        return if (!buryPoint.isNullOrBlank())
            buryPoint
        else
            title ?: ""
    }

}