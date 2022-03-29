package com.chinshry.base.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils.getColor
import com.chinshry.base.adapter.FloorAdapter
import com.chinshry.base.util.CommonUtils.dp2px
import com.chinshry.base.R


/**
 * Created by chinshry on 2022/02/14.
 * Describe：FloorRecycleView分割线
 */
class FloorDivider : RecyclerView.ItemDecoration() {

    private var mPaint: Paint = Paint()

    init {
        mPaint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        var dividerHeight = 0

        (parent.adapter as? FloorAdapter)?.let { floorAdapter ->
            var position = parent.getChildAdapterPosition(view)
            if (floorAdapter.hasHeaderLayout()) position -= 1
            if (floorAdapter.itemCount > position + 1) {
                floorAdapter.data.getOrNull(position)?.dividerH ?.let {
                    dividerHeight = dp2px(parent.context, it)
                }
            }
        }

        outRect.set(0, dividerHeight, 0, 0)
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        drawDivider(c, parent)
    }

    private fun drawDivider(canvas: Canvas, parent: RecyclerView) {
        parent.layoutManager ?: return
        val floorAdapter = (parent.adapter as? FloorAdapter) ?: return

        canvas.save()

        floorAdapter.data.forEachIndexed { position, itemData ->
            val dividerHeight = itemData.dividerH ?.let { dp2px(parent.context, it) } ?: return@forEachIndexed
            // 默认颜色f9f9f
            val dividerColor = try { Color.parseColor(itemData.dividerColor) } catch (e: Exception) {
                getColor(R.color.empty)
            }

            val adapterPosition = if (floorAdapter.hasHeaderLayout()) {
                position + 1
            } else {
                position
            }

            // 在楼层上方绘制分割线
            floorAdapter.getViewByPosition(adapterPosition, R.id.common_floor) ?.let { childView ->
                mPaint.color = dividerColor
                canvas.drawRect(
                    childView.left.toFloat() + dp2px(parent.context, (itemData.dividerMarginLeft ?: 0).toFloat()),
                    childView.top.toFloat() - dividerHeight,
                    childView.right.toFloat() - dp2px(parent.context, (itemData.dividerMarginRight ?: 0).toFloat()),
                    childView.top.toFloat(),
                    mPaint
                )
            }

        }
        canvas.restore()
    }

}
