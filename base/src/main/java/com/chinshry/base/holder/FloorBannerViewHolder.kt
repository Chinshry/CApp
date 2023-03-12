package com.chinshry.base.holder

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.chinshry.base.R
import com.chinshry.base.adapter.FloorBannerAdapter
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.chinshry.base.util.dp
import com.chinshry.base.util.getColorById
import com.youth.banner.Banner
import com.youth.banner.indicator.RectangleIndicator


/**
 * Created by chinshry on 2022/03/21.
 */
class FloorBannerViewHolder {
    companion object {
        fun holder(
            view: View,
            floorData: FloorData,
            buryPointInfo: BuryPointInfo? = null,
        ) {
            val banner = view.findViewById<Banner<ElementAttribute, FloorBannerAdapter>>(R.id.layout_floor_banner)
            if (floorData.elementAttributes.isNullOrEmpty() || banner == null) {
                return
            }

            bannerViewHolder(
                banner,
                floorData.elementAttributes,
                buryPointInfo
            )
        }

        private fun bannerViewHolder(
            banner: Banner<ElementAttribute, FloorBannerAdapter>,
            itemData: List<ElementAttribute?>,
            buryPointInfo: BuryPointInfo? = null,
        ) {
            banner.setAdapter(FloorBannerAdapter(itemData.filterNotNull(), banner, buryPointInfo))
            banner.indicator = RectangleIndicator(ActivityUtils.getTopActivity())
            banner.setIndicatorSelectedWidth(6.dp)
            banner.setIndicatorNormalWidth(6.dp)
            banner.setIndicatorHeight(6.dp)
            banner.setIndicatorRadius(6.dp)
            banner.setLoopTime(2500)
            banner.setIndicatorNormalColor(getColorById(R.color.half_white))
            banner.setIndicatorSelectedColor(getColorById(R.color.white))
        }
    }
}