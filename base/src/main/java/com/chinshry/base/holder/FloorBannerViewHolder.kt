package com.chinshry.base.holder

import android.R.attr.banner
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chinshry.base.R
import com.chinshry.base.adapter.FloorBannerAdapter
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.bean.FloorData
import com.chinshry.base.util.CommonUtils.dp2px
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
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
                buryPointInfo = buryPointInfo
            )
        }

        private fun bannerViewHolder(
            banner: Banner<ElementAttribute, FloorBannerAdapter>,
            itemData: List<ElementAttribute?>,
            buryPointInfo: BuryPointInfo? = null,
        ) {
            val context: Context = ActivityUtils.getTopActivity().applicationContext
            banner.setAdapter(FloorBannerAdapter(itemData.filterNotNull(), banner, buryPointInfo))
            banner.indicator = RectangleIndicator(context)
            banner.setIndicatorSelectedWidth(dp2px(context, 6F))
            banner.setIndicatorNormalWidth(dp2px(context, 6F))
            banner.setIndicatorHeight(dp2px(context, 6F))
            banner.setIndicatorRadius(dp2px(context, 6F))
            banner.setLoopTime(2500)
            banner.setIndicatorNormalColor(ContextCompat.getColor(context, R.color.half_white))
            banner.setIndicatorSelectedColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}