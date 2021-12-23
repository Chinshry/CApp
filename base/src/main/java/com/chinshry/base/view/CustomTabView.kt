package com.chinshry.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ColorUtils
import com.example.base.R
import java.util.*

/**
 * Created by chinshry on 2021/12/23.
 * File Name: CustomTabView.kt
 * Describe： 自定义tab组件
 */
class CustomTabView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    init {
        initView()
    }

    class Tab(
        var tabText: String? = null,
        var iconNormal: Drawable? = null,
        var iconSelect: Drawable? = null,
        var badge: Drawable? = null,
        @ColorInt var textColorNormal: Int = ColorUtils.getColor(R.color.grey),
        @ColorInt var textColorSelect: Int = ColorUtils.getColor(R.color.black),
        var selectFunction: () -> Unit = {}
    ) {
        fun setTabText(text: String?): Tab {
            this.tabText = text
            return this
        }

        fun setIcon(normal: Drawable?, select: Drawable?): Tab {
            this.iconNormal = normal
            this.iconSelect = select
            return this
        }

        fun setBadge(badgeDrawable: Drawable?): Tab {
            this.badge = badgeDrawable
            return this
        }

        fun setTextColor(@ColorInt normal: Int?, @ColorInt select: Int?): Tab {
            if (normal != null) {
                this.textColorNormal = normal
            }
            if (select != null) {
                this.textColorSelect = select
            }
            return this
        }

        fun onTabSelect(function: () -> Unit): Tab {
            selectFunction = function
            return this
        }
    }


    private var mTabViews: MutableList<View> = mutableListOf()
    private var mTabs: MutableList<Tab> = mutableListOf()

    private fun initView() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        mTabViews = ArrayList()
        mTabs = ArrayList<Tab>()
    }

    /**
     * 添加Tab
     * @param tab
     */
    @SuppressLint("InflateParams")
    fun addTab(tab: Tab) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null)

        val textView = view.findViewById<TextView>(R.id.custom_tab_text)
        val iconView = view.findViewById<ImageView>(R.id.custom_tab_icon)
        val badgeView = view.findViewById<ImageView>(R.id.custom_tab_badge)

        tab.apply {
            iconView.setImageDrawable(tab.iconNormal)

            textView.text = tab.tabText
            textView.setTextColor(tab.textColorNormal)

            badgeView.isVisible = true
            badgeView.setImageDrawable(tab.badge)

            view.tag = mTabViews.size
            view.clickWithTrigger {
                tab.selectFunction()
                updateState(it.tag as Int)
            }

            mTabViews.add(view)
            mTabs.add(tab)

            val layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)

            addView(view, layoutParams)

        }
    }

    /**
     * 设置选中Tab
     * @param position
     */
    fun setCurrentItem(position: Int) {
        var mPosition = position
        if (position >= mTabs.size || position < 0) {
            mPosition = 0
        }
        mTabViews.getOrNull(mPosition)?.performClick()
        updateState(mPosition)
    }

    /**
     * 更新状态
     * @param position
     */
    private fun updateState(position: Int) {
        mTabViews.forEachIndexed { index, view ->
            val textView = view.findViewById<View>(R.id.custom_tab_text) as TextView
            val iconView = view.findViewById<View>(R.id.custom_tab_icon) as ImageView
            val badgeView = view.findViewById<View>(R.id.custom_tab_badge) as ImageView
            if (index == position) {
                mTabs.getOrNull(index)?.apply {
                    badgeView.isVisible = false
                    iconView.setImageDrawable(iconSelect)
                    textView.setTextColor(textColorSelect)
                }
            } else {
                mTabs.getOrNull(index)?.apply {
                    iconView.setImageDrawable(iconNormal)
                    textView.setTextColor(textColorNormal)
                }
            }

        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mTabViews.isNotEmpty()) {
            mTabViews.clear()
        }
        if (mTabs.isNotEmpty()) {
            mTabs.clear()
        }
    }
}