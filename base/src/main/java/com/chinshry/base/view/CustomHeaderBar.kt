package com.chinshry.base.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.chinshry.base.R
import kotlinx.android.synthetic.main.custom_bar.view.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe：标题栏自定义组件
 * 需要设置id为custom_header_bar才可自动初始化点击监听
 */
open class CustomHeaderBar(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    init {
        if (id == NO_ID) {
            id = R.id.custom_header_bar

        }
        initView(context, attrs)
    }

    companion object {
        fun initCustomHeaderBar(
            parent: Any?,
            headerId: Int = R.id.custom_header_bar
        ) {
            when(parent) {
                is Activity -> {
                    parent.findViewById<CustomHeaderBar>(headerId)?.apply {
                        setBackOnClickListener { parent.finish() }
                        setCloseOnClickListener { parent.finish() }
                    }

                }
                is Fragment -> {
                    parent.view?.findViewById<CustomHeaderBar>(headerId)?.apply {
                        setBackOnClickListener { parent.requireActivity().finish() }
                        setCloseOnClickListener {
                            if (!parent.findNavController().navigateUp()) {
                                parent.requireActivity().finish()
                            }
                        }
                    }

                }
                is Dialog -> {
                    parent.findViewById<CustomHeaderBar>(headerId)?.apply {
                        setBackOnClickListener { parent.dismiss() }
                        setCloseOnClickListener { parent.dismiss() }
                    }

                }
            }
        }
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

    open fun setTitleTextSize(size: Float) {
        header_text_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    open fun setPageTitle(str: String?) {
        if (!str.isNullOrEmpty()) {
            header_text_title?.text = str
        }
    }

    open fun setRightTitle(str: String?) {
        if (!str.isNullOrEmpty()) {
            header_btn_right?.visibility = VISIBLE
            header_btn_right?.text = str
        }
    }

    open fun setLeftImg(drawable: Drawable?) {
        if (drawable != null) {
            header_btn_back?.visibility = VISIBLE
            header_btn_back?.setImageDrawable(drawable)
        }
    }

    open fun setRightImg(drawable: Drawable?) {
        if (drawable != null) {
            header_btn_close?.visibility = VISIBLE
            header_btn_close?.setImageDrawable(drawable)
        }
    }

    open fun setBackVisible(visible: Boolean) {
        header_btn_back?.isVisible = visible
    }

    open fun setCloseVisible(visible: Boolean) {
        header_btn_close?.isVisible = visible
    }

    open fun setRightTextVisible(visible: Boolean) {
        header_btn_right?.isVisible = visible
    }

    open fun setBackOnClickListener(listener: OnClickListener) {
        header_btn_back?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    open fun setCloseOnClickListener(listener: OnClickListener) {
        header_btn_close?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    open fun setRightTextOnClickListener(listener: OnClickListener) {
        header_btn_right?.clickWithTrigger {
            listener.onClick(it)
        }
    }

    open fun setTransparentBarTheme(transparent: Boolean = true) {
        header_bar_container?.setBackgroundColor(
            if (transparent) {
                Color.TRANSPARENT
            } else {
                Color.WHITE
            }
        )
        setShadowLineVisible(!transparent)
    }

    open fun setWhiteBarTheme(white: Boolean = true) {
        val textColor: Int
        val color = if (white) {
            header_bar_container?.setBackgroundColor(Color.TRANSPARENT)
            textColor = Color.WHITE
            Color.WHITE
        } else {
            header_bar_container?.setBackgroundColor(Color.WHITE)
            textColor = Color.parseColor("#333333")
            Color.parseColor("#4F4F4F")
        }
        setShadowLineVisible(!white)
        header_text_title?.setTextColor(textColor)
        header_btn_right?.setTextColor(textColor)
        header_btn_close?.imageTintList = ColorStateList.valueOf(color)
        header_btn_back?.imageTintList = ColorStateList.valueOf(color)
    }

    open fun setShadowLineVisible(visible: Boolean) {
        header_shadow_line.isVisible = visible
    }

    open fun setHeaderMargin() {
        header_bar_container?.layoutParams?.let {
            (it as MarginLayoutParams).topMargin = BarUtils.getStatusBarHeight()
        }
    }

}