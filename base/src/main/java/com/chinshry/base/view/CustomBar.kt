package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.base.R
import kotlinx.android.synthetic.main.custom_bar.view.*

/**
 * Created by chinshry on 2021/12/23.
 * File Name: 标题栏自定义组件
 */
class CustomBar(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        initView(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet) {
        LayoutInflater.from(context).inflate(R.layout.custom_bar, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomBar)


        // 返回按钮 默认显示
        val showBackBtn = typedArray.getBoolean(R.styleable.CustomBar_show_back, true)
        setBackVisible(showBackBtn)

        // 关闭按钮 默认不显示
        val showCloseBtn = typedArray.getBoolean(R.styleable.CustomBar_show_close, false)
        setCloseVisible(showCloseBtn)

        // 标题
        val titleStr = typedArray.getString(R.styleable.CustomBar_title_text)
        setPageTitle(titleStr)

        // 右侧文本按钮文字
        val rightTitleStr = typedArray.getString(R.styleable.CustomBar_right_button_text)
        setRightTitle(rightTitleStr)

        // 左边返回按钮图标
        val leftImgBtn = typedArray.getDrawable(R.styleable.CustomBar_left_back_img)
        setLeftImg(leftImgBtn)

        // 右边关闭按钮图标
        val rightImgBtn = typedArray.getDrawable(R.styleable.CustomBar_right_close_img)
        setRightImg(rightImgBtn)

        // 标题字号
        val titleTextSize = typedArray.getDimensionPixelSize(
            R.styleable.CustomBar_title_text_size,
            SizeUtils.sp2px(17F)
        ).toFloat()
        setTitleTextSize(titleTextSize)

        // 阴影
        val shadow = typedArray.getBoolean(R.styleable.CustomBar_shadow_line, true)
        setShadowLineVisible(shadow)

        // 透明标题栏 黑色ICON 默认false
        val transparent = typedArray.getBoolean(R.styleable.CustomBar_transparent, false)
        if (transparent) {
            setTransparentBarTheme(transparent)
        }

        // 透明标题栏 白色ICON 默认false
        val whiteTheme = typedArray.getBoolean(R.styleable.CustomBar_transparent_white_icon, false)
        if (whiteTheme) {
            setWhiteBarTheme(whiteTheme)
        }

        // 是否margin状态栏 默认false
        val marginStatusBar = typedArray.getBoolean(R.styleable.CustomBar_margin_status_bar, false)
        if (marginStatusBar) {
            setHeaderMargin()
        }

        typedArray.recycle()
    }

    fun setTitleTextSize(size: Float) {
        header_text_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setPageTitle(str: String?) {
        if (!str.isNullOrEmpty()) {
            header_text_title?.text = str
        }
    }

    fun setRightTitle(str: String?) {
        if (!str.isNullOrEmpty()) {
            header_btn_right?.visibility = VISIBLE
            header_btn_right?.text = str
        }
    }

    fun setLeftImg(drawable: Drawable?) {
        if (drawable != null) {
            header_btn_back?.visibility = VISIBLE
            header_btn_back?.setImageDrawable(drawable)
        }
    }

    fun setRightImg(drawable: Drawable?) {
        if (drawable != null) {
            header_btn_close?.visibility = VISIBLE
            header_btn_close?.setImageDrawable(drawable)
        }
    }

    fun setBackVisible(visible: Boolean) {
        header_btn_back?.isVisible = visible
    }

    fun setCloseVisible(visible: Boolean) {
        header_btn_close?.isVisible = visible
    }

    fun setRightTextVisible(visible: Boolean) {
        header_btn_right?.isVisible = visible
    }

    fun setBackOnClickListener(listener: OnClickListener) {
        header_btn_back?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    fun setCloseOnClickListener(listener: OnClickListener) {
        header_btn_close?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    fun setRightTextOnClickListener(listener: OnClickListener) {
        header_btn_right?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    fun setTransparentBarTheme(transparent: Boolean = true) {
        custom_bar?.setBackgroundColor(
            if (transparent) {
                Color.TRANSPARENT
            } else {
                Color.WHITE
            }
        )
        setShadowLineVisible(!transparent)
    }

    fun setWhiteBarTheme(white: Boolean = true) {
        val textColor: Int
        val color = if (white) {
            custom_bar?.setBackgroundColor(Color.TRANSPARENT)
            textColor = Color.WHITE
            Color.WHITE
        } else {
            custom_bar?.setBackgroundColor(Color.WHITE)
            textColor = Color.parseColor("#333333")
            Color.parseColor("#4F4F4F")
        }
        setShadowLineVisible(!white)
        header_text_title?.setTextColor(textColor)
        header_btn_right?.setTextColor(textColor)
        header_btn_close?.imageTintList = ColorStateList.valueOf(color)
        header_btn_back?.imageTintList = ColorStateList.valueOf(color)
    }

    fun setShadowLineVisible(visible: Boolean) {
        header_shadow_line.isVisible = visible
    }

    fun setHeaderMargin() {
        custom_bar?.layoutParams?.let {
            (it as MarginLayoutParams).topMargin = BarUtils.getStatusBarHeight()
        }
    }

}