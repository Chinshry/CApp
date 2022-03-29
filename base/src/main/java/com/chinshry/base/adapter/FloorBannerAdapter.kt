package com.chinshry.base.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chinshry.base.R
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.util.FloorUtil
import com.chinshry.base.view.clickWithTrigger
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by chinshry on 2022/03/21.
 * Describe：楼层Banner Adapter
 */
class FloorBannerAdapter(
    itemData: List<ElementAttribute>,
    private val banner: Banner<String, FloorBannerAdapter>,
    private val buryPointInfo: BuryPointInfo? = null,
) : BannerAdapter<ElementAttribute, FloorBannerAdapter.ImageHolder>(itemData as MutableList<ElementAttribute>) {

    val context: Context = ActivityUtils.getTopActivity().applicationContext

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent?.context)
        val params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.FIT_XY
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
                if (position == 0 && resource?.intrinsicWidth ?: 0 > 0) {
                    val ratio = resource?.intrinsicWidth.toString() + ":" + resource?.intrinsicHeight.toString()
                    banner.layoutParams?.let {
                        it.height = 0
                        (it as ConstraintLayout.LayoutParams).dimensionRatio = ratio
                    }
                }
                return false
            }
        }

        Glide.with(context)
            .load(FloorUtil.getImageUrl(itemData.elementPicture).ifBlank { R.color.empty })
            .listener(requestListener)
            .into(holder.imageView)

        holder.imageView.clickWithTrigger {
            // TODO CLICK ITEM
        }
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView as ImageView
    }
}
