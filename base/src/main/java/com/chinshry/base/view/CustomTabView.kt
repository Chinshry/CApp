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
import com.blankj.utilcode.util.ColorUtils
import com.chinshry.base.R


/**
 * Created by chinshry on 2021/12/23.
 * Describe： 自定义tab组件
 */
class CustomTabView(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    init {
        initView()
    }

    class Tab(
        var textNormal: String? = null,
        var textSelect: String? = null,
        var iconNormal: Drawable? = null,
        var iconSelect: Drawable? = null,
        var badge: Drawable? = null,
        @ColorInt var textColorNormal: Int = ColorUtils.getColor(R.color.grey),
        @ColorInt var textColorSelect: Int = ColorUtils.getColor(R.color.black),
        var selectFunction: () -> Unit = {}
    ) {
        fun setTabText(normal: String?, select: String?): Tab {
            this.textNormal = normal
            this.textSelect = select
            return this
        }

        fun setIcon(normal: Drawable?, select: Drawable?): Tab {
            this.iconNormal = normal
            this.iconSelect = select
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

        fun setBadge(badgeDrawable: Drawable?): Tab {
            this.badge = badgeDrawable
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
        mTabViews = mutableListOf()
        mTabs = mutableListOf()
    }

    /**
     * 添加Tab
     * @param tab
     */
    @SuppressLint("InflateParams")
    fun addTab(tab: Tab) {
        val tabView: View = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null)
        val position = mTabs.size
        updateTabView(tabView, tab, false)

        tabView.clickWithTrigger{
            tab.selectFunction()
            updateTab(position)
        }

        mTabViews.add(tabView)
        mTabs.add(tab)

        val layoutParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        addView(tabView, layoutParams)

    }

    /**
     * 设置选中Tab
     * @param position
     */
    fun setCurrentItem(position: Int) {
        var mPosition = position
        if (position >= mTabViews.size || position < 0) {
            mPosition = 0
        }
        mTabViews.getOrNull(mPosition)?.performClick()
    }

    /**
     * 更新TAB UI
     * @param view View TabView
     * @param tabData ElementAttribute? Tab数据
     * @param isSelected Boolean 是否Selected
     */
    @SuppressLint("ResourceAsColor")
    private fun updateTabView(view: View, tabData: Tab?, isSelected: Boolean) {
        val textView = view.findViewById<TextView>(R.id.custom_tab_text)
        val iconView = view.findViewById<ImageView>(R.id.custom_tab_icon)
        val badgeView = view.findViewById<ImageView>(R.id.custom_tab_badge)

        tabData?.apply {
            // tab标题文字
            if (isSelected) {
                textView?.text = textSelect
            } else {
                textView?.text = textNormal
            }

            // tab标题颜色
            if (isSelected) {
                textView.setTextColor(textColorSelect)
            } else {
                textView.setTextColor(textColorNormal)
            }

            // tab icon
            if (isSelected) {
                iconView.setImageDrawable(iconSelect)
            } else {
                iconView.setImageDrawable(iconNormal)
            }

            // tab badge
            if (isSelected) {
                badgeView?.visibility = View.GONE
            } else {
                badgeView?.visibility = View.VISIBLE
                badgeView.setImageDrawable(badge)
            }

        }
    }


    /**
     * 更新TAB
     * @param position
     */
    @SuppressLint("ResourceAsColor")
    fun updateTab(position: Int) {
        mTabViews.forEachIndexed { index, tabView ->
            val tabItemBean = mTabs.getOrNull(index) ?: return
            updateTabView(tabView, tabItemBean, index == position)
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