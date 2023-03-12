package com.chinshry.base.adapter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chinshry.base.R
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.holder.ImageHolder
import com.chinshry.base.util.FloorUtil
import com.chinshry.base.view.buryPoint
import com.chinshry.base.view.clickWithTrigger
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by chinshry on 2022/03/21.
 * Describe：楼层Banner Adapter
 */
class FloorBannerAdapter(
    itemData: List<ElementAttribute>,
    private val banner: Banner<ElementAttribute, FloorBannerAdapter>,
    private val buryPointInfo: BuryPointInfo? = null,
) : BannerAdapter<ElementAttribute, ImageHolder>(itemData) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent?.context)
        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        // imageView.scaleType = ImageView.ScaleType.FIT_XY
        return ImageHolder(imageView)
    }

    override fun onBindView(
        holder: ImageHolder,
        itemData: ElementAttribute,
        position: Int,
        size: Int
    ) {
        val requestListener = object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // 多张Banner不固定比例 按第一张图拉伸
                if (position == 0 && resource != null && resource.intrinsicWidth > 0) {
                    banner.layoutParams?.let {
                        it.height = banner.width * resource.intrinsicHeight / resource.intrinsicWidth
                    }
                }
                return false
            }
        }

        Glide.with(holder.imageView.context)
            .load(FloorUtil.getImageUrl(itemData.elementPicture).ifBlank { R.color.empty })
            .listener(requestListener)
            .into(holder.imageView)

        holder.imageView.buryPoint = buryPointInfo
        holder.imageView.clickWithTrigger(FloorUtil.getElementBury(itemData.elementTitle, itemData.buryPoint)) {
            // TODO CLICK ITEM
        }
    }
}
