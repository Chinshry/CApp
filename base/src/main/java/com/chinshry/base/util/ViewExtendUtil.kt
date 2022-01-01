package com.chinshry.base.util

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ColorUtils


/**
 * Created by chinshry on 2022/01/01.
 * Describe：控件背景效果扩展
 */
fun View.setBgSelectorByRes(
    @DrawableRes idNormal: Int = -1,
    @DrawableRes idPressed: Int = -1,
    @DrawableRes idFocused: Int = -1,
    @DrawableRes idUnable: Int = -1
) = this.apply {
    val bgSelector = StateListDrawable()
    val normal: Drawable? = if (idNormal === -1) null else context.resources.getDrawable(idNormal)
    val pressed: Drawable? =
        if (idPressed === -1) null else context.resources.getDrawable(idPressed)
    val focused: Drawable? =
        if (idFocused === -1) null else context.resources.getDrawable(idFocused)
    val unable: Drawable? = if (idUnable === -1) null else context.resources.getDrawable(idUnable)
    bgSelector.addState(intArrayOf(R.attr.state_pressed, R.attr.state_enabled), pressed)
    bgSelector.addState(intArrayOf(R.attr.state_enabled, R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(R.attr.state_enabled), normal)
    bgSelector.addState(intArrayOf(R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(R.attr.state_window_focused), unable)
    bgSelector.addState(intArrayOf(), normal)
    setBackgroundDrawable(bgSelector)
}

fun View.setBgSelectorByColor(
    @ColorRes idNormal: Int? = null,
    @ColorRes idPressed: Int? = null,
    @ColorRes idFocused: Int? = null,
    @ColorRes idUnable: Int? = null,
    @FloatRange vararg radius: Float? = Array(4) { 10F;10F;10F;10F },
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
    bgSelector.addState(intArrayOf(R.attr.state_pressed, R.attr.state_enabled), pressed)
    bgSelector.addState(intArrayOf(R.attr.state_enabled, R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(R.attr.state_enabled), normal)
    bgSelector.addState(intArrayOf(R.attr.state_focused), focused)
    bgSelector.addState(intArrayOf(R.attr.state_window_focused), unable)
    bgSelector.addState(intArrayOf(), normal)
    setBackgroundDrawable(bgSelector)
}

fun TextView.setColorSelector(
    @ColorRes normal: Int,
    @ColorRes pressed: Int,
    @ColorRes focused: Int,
    @ColorRes unable: Int
) = this.apply {
    val colors = intArrayOf(pressed, focused, normal, focused, unable, normal)
    val states = arrayOfNulls<IntArray>(6)
    states[0] = intArrayOf(R.attr.state_pressed, R.attr.state_enabled)
    states[1] = intArrayOf(R.attr.state_enabled, R.attr.state_focused)
    states[2] = intArrayOf(R.attr.state_enabled)
    states[3] = intArrayOf(R.attr.state_focused)
    states[4] = intArrayOf(R.attr.state_window_focused)
    states[5] = intArrayOf()
    setTextColor(ColorStateList(states, colors))
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

//TODO 可自己根据需求添加属性
fun View.setShape(
    @ShapeType
    shapeType: Int = ShapeType.RECTANGLE,
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
    radius.apply {
        if (radius.isEmpty()) {
            return@apply
        }
        val cornerRadii = if (radius.size == 1) {
            FloatArray(8) {
                radius[0];radius[0];
                radius[0];radius[0];
                radius[0];radius[0];
                radius[0];radius[0]!!
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
    vararg radius: Float? = Array(4) { 10F;10F;10F;10F },
    strokeWith: Int? = null,
    @ColorRes strokeColor: Int? = null,
    dashWidth: Float? = null,
    dashGap: Float? = null,
): Drawable {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.shape = shapeType
    radius?.apply {
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


@IntDef(value = [ShapeType.RECTANGLE, ShapeType.OVAL])
@Retention(AnnotationRetention.SOURCE)
annotation class ShapeType {
    companion object {
        const val RECTANGLE: Int = GradientDrawable.RECTANGLE
        const val OVAL: Int = GradientDrawable.OVAL
        //TODO  根据需求添加形状
    }
}