package com.chinshry.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.IntRange
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.bean.BuryPointInfo
import com.chinshry.base.util.CommonUtils.getTrackBuryPoint
import com.chinshry.base.util.WindowManager
import com.chinshry.base.util.WindowManagerList
import com.chinshry.base.util.logBuryPoint
import com.chinshry.base.util.setNotBlankText
import com.chinshry.base.view.CustomHeaderBar.Companion.initCustomHeaderBar
import com.example.base.R

/**
 * Created by chinshry on 2022/01/01.
 * Describe：基础弹窗
 */
open class BaseDialog(
    context: Context = ActivityUtils.getTopActivity(),
    style: Int = 0,
): Dialog(context, style) {

    // dialog宽度
    private var dialogWidth: Int? = null
    // dialog与屏幕边距百分比
    private var dialogWidthPercent: Float? = null
    // dialog gravity
    private var dialogGravity: Int? = null
    // 自定义埋点
    private var pageBuryPoint: BuryPointInfo = BuryPointInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化header点击事件
        initCustomHeaderBar(this)

        // 获取堆栈埋点
        getTrackBuryPoint(1)?.let {
            pageBuryPoint = it
        }
    }

    open fun builder(layoutResID: Int): BaseDialog {
        LayoutInflater.from(context).inflate(layoutResID, null)?.let {
            setContentView(it)
            setWindowAnimations(R.style.WindowAnimationDefault)
        }
        return this
    }

    open fun showWindowOnDismiss(): BaseDialog {
        setOnDismissListener {
            // 弹窗消失 置弹窗显示标志为false
            WindowManagerList.isDialogShowing = false
            LogUtils.d("csTest showWindow")
            WindowManager.showWindow()
            WindowManagerList.showWindow()
        }
        return this
    }

    open fun setWidth(@IntRange(from = -2) width: Int): BaseDialog {
        this.dialogWidth = width
        return this
    }

    open fun setWidthPercent(@FloatRange(from = 0.0, to = 1.0) widthPercent: Float): BaseDialog {
        this.dialogWidthPercent = widthPercent
        return this
    }

    open fun setGravity(gravity: Int): BaseDialog {
        this.dialogGravity = gravity
        return this
    }

    open fun setDialogCancelable(cancel: Boolean): BaseDialog {
        setCancelable(cancel)
        return this
    }

    /**
     * 清除灰色背景
     */
    open fun setDialogTransparent(): BaseDialog {
        window?.setDimAmount(0f)
        return this
    }

    open fun setWindowAnimations(
        @StyleRes resId: Int = R.style.WindowAnimationBottomToTop
    ): BaseDialog {
        window?.setWindowAnimations(resId)
        return this
    }

    open fun showInput() : BaseDialog {
        KeyboardUtils.showSoftInput()
        return this
    }

    open fun <T : View?> initViewById(
        @IdRes id: Int,
        name: String? = null,
        visibleWithName: Boolean = false,
        viewFunction: ((T) -> Unit)? = null,
    ): BaseDialog {
        findViewById<T>(id)?.apply {
            if (this is TextView) {
                setNotBlankText(name, visibleWithName)
            }
            viewFunction?.invoke(this)
        }

        return this
    }

    private fun showParams() {
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setBackgroundColor(Color.TRANSPARENT)

            attributes = attributes.apply {
                val screenWidth = ScreenUtils.getAppScreenWidth()
                width = (screenWidth * 0.9).toInt()

                dialogWidth?.let {
                    width = if (it < screenWidth) {
                        it
                    } else {
                        screenWidth
                    }
                }

                dialogWidthPercent?.let {
                    width = (screenWidth * it).toInt()
                }

                gravity = dialogGravity ?: Gravity.CENTER
            }
        }
    }

    override fun show() {
        if (!isShowing) {
            super.show()
            showParams()
            logBuryPoint(pageBuryPoint)
        }
    }

    override fun dismiss() {
        window?.let { KeyboardUtils.hideSoftInput(it) }
        super.dismiss()
    }

}