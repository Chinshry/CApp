package com.chinshry.base.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.IntRange
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.view.clickWithTrigger
import com.example.base.R

/**
 * Created by chinshry on 2022/01/01.
 * Describe：基础弹窗
 */
open class BaseDialog(
    context: Context = ActivityUtils.getTopActivity(),
    style: Int = 0,
): Dialog(context, style) {

    var rootView: View? = null

    // dialog宽度
    private var dialogWidth: Int? = null
    // dialog与屏幕边距百分比
    private var dialogWidthPercent: Float? = null
    // dialog gravity
    private var dialogGravity: Int? = null

    fun builder(layoutResID: Int): BaseDialog {
        LayoutInflater.from(context).inflate(layoutResID, null)?.let {
            rootView = it
            setContentView(it)
            setWindowAnimations(R.style.WindowAnimationDefault)
        }
        return this
    }

    fun setWidth(@IntRange(from = -2) width: Int): BaseDialog {
        this.dialogWidth = width
        return this
    }

    fun setWidthPercent(@FloatRange(from = 0.0, to = 1.0) widthPercent: Float): BaseDialog {
        this.dialogWidthPercent = widthPercent
        return this
    }

    fun setGravity(gravity: Int): BaseDialog {
        this.dialogGravity = gravity
        return this
    }

    fun setDialogCancelable(cancel: Boolean): BaseDialog {
        setCancelable(cancel)
        return this
    }

    /**
     * 清除灰色背景
     */
    fun setDialogTransparent(): BaseDialog {
        window?.setDimAmount(0f)
        return this
    }

    fun setWindowAnimations(
        @StyleRes resId: Int = R.style.WindowAnimationBottomToTop
    ): BaseDialog {
        window?.setWindowAnimations(resId)
        return this
    }

    /**
     * function 使用任意布局，此方法获取控键Id设置值以及点击消失等操作
     *      ex: dialog.findViewById(R.id.xx).text="值"
     *          dialog.dismiss()
     */
    fun setExtendMethod(dsl: (dialog: BaseDialog) -> Unit): BaseDialog {
        dsl.invoke(this)
        return this
    }

    fun setViewInDialog(
        @IdRes id: Int,
        name: String? = null,
        visibleWithName: Boolean = false,
        viewFunction: (View) -> Unit = {},
        clickListener: View.OnClickListener? = null
    ): BaseDialog {
        findViewById<TextView>(id)?.apply {
            setNotBlankText(name, visibleWithName)
            viewFunction(this)
            clickWithTrigger { v ->
                clickListener?.onClick(v)
            }
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
        }
    }
}