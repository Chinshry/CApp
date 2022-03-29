package com.chinshry.base.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * Created by chinshry on 2022/3/14.
 * Describe：滚动组件滑动
 */
class CustomSnapHelper : LinearSnapHelper() {
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null
    private var mRecyclerView: RecyclerView? = null

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper!!
    }
    
    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }

    /**
     * 计算出View对齐到指定位置，所需的x、y轴上的偏移量。
     */
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)

        // 水平方向滑动时计算x方向，否则偏移为0
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(layoutManager, targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        // 垂直方向滑动时计算y方向，否则偏移为0
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(layoutManager, targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }

        return out
    }

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        mRecyclerView = recyclerView
        mRecyclerView?.onFlingListener = null
        super.attachToRecyclerView(recyclerView)
    }

    private fun distanceToStart(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View,
        helper: OrientationHelper
    ): Int {
        //如果已经滚动到尽头 直接返回0 不用多余的滚动
        mRecyclerView ?.let {
            (layoutManager as? LinearLayoutManager) ?.let { lm ->
                it.canScrollHorizontally(1)
                if (lm.findLastVisibleItemPosition() == (it.adapter?.itemCount ?: 0) - 1 && !it.canScrollHorizontally(1)) return 0
            }
        }

        // RecyclerView的边界x值 也就是左侧Padding值
        // return helper.getDecoratedStart(targetView) - (if (layoutManager.clipToPadding) helper.startAfterPadding else 0)
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    /**
     * 查找需要对齐的View
     */
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager.canScrollVertically()) {
            findStartView(layoutManager, getVerticalHelper(layoutManager))
        } else {
            findStartView(layoutManager, getHorizontalHelper(layoutManager))
        }
    }

    private fun findStartView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        var closestChild: View? = null
        var absClosest = Int.MAX_VALUE

        for (i in 0 until childCount) {
            val child: View? = layoutManager.getChildAt(i)
            // ItemView 的左侧坐标
            val childStart = helper.getDecoratedStart(child)
            // 计算此ItemView 与 RecyclerView左侧的距离
            val absDistance = abs(childStart - helper.startAfterPadding)

            // 找到那个最靠近左侧的ItemView然后返回
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

}