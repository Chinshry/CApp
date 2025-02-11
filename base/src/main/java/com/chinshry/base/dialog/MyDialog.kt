package com.chinshry.base.dialog

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import com.chinshry.base.BaseDialog
import com.chinshry.base.view.clickWithTrigger
import com.chinshry.base.R
import com.chinshry.base.util.ContextHelper

/**
 * Created by chinshry on 2022/01/01.
 * Describe：通用弹窗
 */
class MyDialog(
    context: Context = ContextHelper.getActivity(),
    style: Int = 0,
) : BaseDialog(context, style) {

    init {
        builder(R.layout.dialog_base)
        setWidthPercent(0.7f)
        setWindowAnimations()
        setConfirmBtn()
        setCancelBtn()
    }

    fun setTitle(text: String?): MyDialog {
        initViewById<TextView>(
            id = R.id.tv_title,
            name = text,
            visibleWithName = true
        )
        return this
    }

    fun setMsg(text: String?): MyDialog {
        initViewById<TextView>(
            id = R.id.tv_content,
            name = text,
            visibleWithName = true,
        ) {
            (it as? TextView)?.movementMethod = ScrollingMovementMethod.getInstance()
        }
        return this
    }

    fun setCancelBtn(
        text: String? = null,
        function: () -> Unit = {},
    ): MyDialog {
        initViewById<Button>(
            id = R.id.btn_cancel,
            name = text
        ) { view ->
            view.clickWithTrigger {
                function()
                dismiss()
            }
        }
        return this
    }

    fun setConfirmBtn(
        text: String? = null,
        function: () -> Unit = {},
    ): MyDialog {
        initViewById<Button>(
            id = R.id.btn_confirm,
            name = text
        ) { view ->
            view.clickWithTrigger {
                function()
                dismiss()
            }
        }
        return this
    }

}