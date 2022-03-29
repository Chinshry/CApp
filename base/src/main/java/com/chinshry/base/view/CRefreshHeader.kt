package com.chinshry.base.view

import android.content.Context
import android.widget.RelativeLayout
import com.blankj.utilcode.util.BarUtils.getStatusBarHeight
import com.bumptech.glide.Glide
import com.chinshry.base.R
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout


/**
 * Created by chinshry on 2021/12/23.
 * Describe： 下拉刷新
 */
class CRefreshHeader(context: Context?) : ClassicsHeader(context) {

    fun init(): CRefreshHeader {
        val params = mProgressView.layoutParams as LayoutParams
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        params.addRule(RelativeLayout.LEFT_OF, 0)
        params.addRule(RelativeLayout.START_OF, 0)
        mProgressView.layoutParams = params

        this.setArrowDrawable(null)
            .setEnableLastTime(false)
            .setTextSizeTitle(12F)
            .setAccentColorId(R.color.text_color)
            .setProgressResource(R.mipmap.loading)
            .setDrawableMarginRight(0F)
            .setDrawableProgressSize(24F)
        return this
    }

    fun marginStatusBar(): CRefreshHeader {
        this.layoutParams?.let {
            (it as MarginLayoutParams).topMargin = getStatusBarHeight()
        }
        return this
    }

    /**
     * 覆写加载时的动画
     */
    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        // super.onStartAnimator(refreshLayout, height, maxDragHeight)
        if (mProgressView.visibility != VISIBLE) {
            mProgressView.visibility = VISIBLE
            // 显示GIF图片(
            Glide.with(context).load(R.mipmap.loading).into(mProgressView)
        }

    }
}