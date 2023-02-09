package com.chinshry.base.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.annotation.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chinshry.base.R


/**
 * Created by chinshry on 2022/01/01.
 * Describe：控件背景效果扩展
 */

@IntDef(value = [ShapeType.RECTANGLE, ShapeType.OVAL])
@Retention(AnnotationRetention.SOURCE)
annotation class ShapeType {
    companion object {
        const val RECTANGLE: Int = GradientDrawable.RECTANGLE
        const val OVAL: Int = GradientDrawable.OVAL
        //TODO  根据需求添加形状
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun View.setBgSelectorByRes(
    @DrawableRes idNormal: Int = -1,
    @DrawableRes idPressed: Int = -1,
    @DrawableRes idFocused: Int = -1,
    @DrawableRes idUnable: Int = -1
) = this.apply {
    val bgSelector = StateListDrawable()
    val normal: Drawable? = if (idNormal == -1) null else context.getDrawable(idNormal)
    val pressed: Drawable? =
        if (idPressed == -1) null else context.getDrawable(idPressed)
    val focused: Drawable? =
        if (idFocused == -1) null else context.getDrawable(idFocused)
    val unable: Drawable? = if (idUnable == -1) null else context.getDrawable(idUnable)
    bgSelector.addState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), pressed)
    bgSelector.addState(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(android.R.attr.state_enabled), normal)
    bgSelector.addState(intArrayOf(android.R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(android.R.attr.state_window_focused), unable)
    bgSelector.addState(intArrayOf(), normal)
    background = bgSelector
}

fun View.setBgSelectorByColor(
    @ColorRes idNormal: Int? = null,
    @ColorRes idPressed: Int? = null,
    @ColorRes idFocused: Int? = null,
    @ColorRes idUnable: Int? = null,
    @FloatRange vararg radius: Float? = Array(4) { 10F },
) = this.apply {
    val bgSelector = StateListDrawable()
    val normal: Drawable? =
        if (idNormal == null) null else getShapeByColor(
            context = context,
            solidColor = idNormal,
            radius = radius
        )
    val pressed: Drawable? =
        if (idPressed == null) null else getShapeByColor(
            context = context,
            solidColor = idPressed,
            radius = radius
        )
    val focused: Drawable? =
        if (idFocused == null) null else getShapeByColor(
            context = context,
            solidColor = idFocused,
            radius = radius
        )
    val unable: Drawable? =
        if (idUnable == null) null else getShapeByColor(
            context = context,
            solidColor = idUnable,
            radius = radius
        )
    bgSelector.addState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), pressed)
    bgSelector.addState(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(android.R.attr.state_enabled), normal)
    bgSelector.addState(intArrayOf(android.R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(android.R.attr.state_window_focused), unable)
    bgSelector.addState(intArrayOf(), normal)
    background = bgSelector
}

fun TextView.setColorSelector(
    @ColorRes normal: Int,
    @ColorRes pressed: Int,
    @ColorRes focused: Int,
    @ColorRes unable: Int
) = this.apply {
    val colors = intArrayOf(pressed, focused, normal, focused, unable, normal)
    val states = arrayOfNulls<IntArray>(6)
    states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
    states[2] = intArrayOf(android.R.attr.state_enabled)
    states[3] = intArrayOf(android.R.attr.state_focused)
    states[4] = intArrayOf(android.R.attr.state_window_focused)
    states[5] = intArrayOf()
    setTextColor(ColorStateList(states, colors))
}
//TODO 可自己根据需求添加属性
fun View.setShape(
    @ShapeType shapeType: Int = ShapeType.RECTANGLE,
    @ColorRes solidColor: Int? = null,
    @ColorInt solidColorRgb: Int? = null,
    strokeWith: Int? = null,
    @ColorRes strokeColor: Int? = null,
    dashWidth: Float? = null,
    dashGap: Float? = null,
    vararg radius: Float? = emptyArray()
) = this.apply {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = shapeType
    gradientDrawable.cornerRadius = 1.0f
    radius.let {
        if (radius.isEmpty()) {
            return@let
        }
        val cornerRadii = if (radius.size == 1) {
            FloatArray(8) {
                radius[0] ?: 0F
            }
        } else {
            FloatArray(8) {
                radius[0];radius[0];
                radius[1];radius[1];
                radius[2];radius[2];
                radius[3];radius[3]!!;
            }
        }
        gradientDrawable.cornerRadii = cornerRadii
    }
    val bgColor = solidColorRgb
        ?: if (solidColor != null) {
            resources.getColor(solidColor)
        } else {
            Color.TRANSPARENT
        }
    bgColor.apply {
        gradientDrawable.setColor(bgColor)
        //gradientDrawable.setColors(bgColors) 渐变色 按需添加
    }
    strokeColor?.apply {
        gradientDrawable.setStroke(
            strokeWith ?: 0,
            resources.getColor(strokeColor),
            dashWidth ?: 0F,
            dashGap ?: 0F
        )
    }
    background = gradientDrawable
    invalidateDrawable(gradientDrawable)
}

private fun getShapeByColor(
    context: Context? = null,
    @ColorRes solidColor: Int?,
    @ShapeType
    shapeType: Int = ShapeType.RECTANGLE,
    vararg radius: Float? = Array(4) { 10F },
    strokeWith: Int? = null,
    @ColorRes strokeColor: Int? = null,
    dashWidth: Float? = null,
    dashGap: Float? = null,
): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = shapeType
    radius.apply {
        gradientDrawable.cornerRadii = FloatArray(8) {
            radius[0];radius[0];
            radius[1];radius[1];
            radius[2];radius[2];
            radius[3];radius[3]!!;
        }
    }
    val bgColor = if (solidColor != null) {
        context?.resources?.getColor(solidColor)
    } else {
        Color.TRANSPARENT
    }
    solidColor?.apply {
        gradientDrawable.setColor(bgColor!!)
        //gradientDrawable.setColors(bgColors) 渐变色 按需添加
    }
    strokeColor?.apply {
        gradientDrawable.setStroke(
            strokeWith ?: 0,
            context!!.resources!!.getColor(strokeColor),
            dashWidth ?: 0F,
            dashGap ?: 0F
        )
    }
    return gradientDrawable
}

/**
 * 获取Drawable
 * @param strokeWith Int?
 * @param strokeColor Int?
 * @param solidColor Int?
 * @param radius Float
 * @return Drawable
 */
fun getShape(
    strokeWith: Int? = null,
    @ColorInt strokeColor: Int? = null,
    @ColorInt solidColor: Int? = null,
    radius: Float = 0F
): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.cornerRadius = radius

    gradientDrawable.setColor(solidColor ?: Color.TRANSPARENT)

    strokeWith?.apply {
        gradientDrawable.setStroke(strokeWith, strokeColor ?: Color.TRANSPARENT)
    }

    return gradientDrawable
}

fun TextView.setTextColorByString(
    textColor: String?,
    @ColorInt defaultTextColor: Int? = null
) = this.apply {
    val color: Int? =
        try {
            Color.parseColor(textColor)
        } catch (e: Exception) {
            defaultTextColor ?.let {
                ColorUtils.getColor(it)
            }
        }

    color?.let {
        setTextColor(it)
    }
}

fun TextView.setNotBlankText(
    inputText: String?,
    withVisible: Boolean = false
) = this.apply {
    isVisible = true
    if (!inputText.isNullOrBlank()) {
        text = inputText
    } else {
        if (withVisible) {
            isVisible = false
        }
    }

}

fun LinearLayout.addButton(
    name: String,
    layoutParams: LayoutParams =
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ),
    viewFunction: ((Button) -> Unit)? = null,
) = this.apply {
    val newButton = Button(context)
    newButton.text = name
    newButton.textSize = 16F
    newButton.setTextColor(Color.BLACK)
    newButton.setBackgroundResource(R.drawable.bg_btn)
    newButton.gravity = Gravity.CENTER
    newButton.gravity = Gravity.CENTER
    newButton.setPadding(SizeUtils.dp2px(20F), 0, SizeUtils.dp2px(20F), 0)
    newButton.stateListAnimator = null

    viewFunction?.invoke(newButton)
    addView(newButton, layoutParams)
}

/**
 * 计算RecyclerView滑动的距离
 * @param hasHead 是否有头部
 * @param headerHeight RecyclerView的头部高度
 * @return 滑动的距离
 */
fun RecyclerView.getScrollYHeight(
    hasHead: Boolean = false,
    headerHeight: Int = 0
): Int {
    val layoutManager = layoutManager as? LinearLayoutManager ?: return 0
    val position = layoutManager.findFirstVisibleItemPosition()
    val firstVisibleChildView = layoutManager.findViewByPosition(position)
    val itemHeight = firstVisibleChildView?.height ?: 0
    val childTop = firstVisibleChildView?.top ?: 0
    return if (hasHead) {
        headerHeight + itemHeight * position - childTop
    } else {
        itemHeight * position - childTop
    }
}

inline val Double.dp: Int
    get() = run {
        return toFloat().dp
    }
inline val Int.dp: Int
    get() = run {
        return toFloat().dp
    }
inline val Float.dp: Int
    get() = run {
        val context = ActivityUtils.getTopActivity()
        return CommonUtils.dp2px(context, 6F)
    }
