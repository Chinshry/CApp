package com.chinshry.base.util

import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.R
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView

/**
 * Created by chinshry on 2023/3/10.
 * Describe：View生成工具
 */
fun LinearLayout.addButton(
    name: String,
    layoutParams: LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
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
    newButton.setPadding(20.dp, 0, 20.dp, 0)
    newButton.stateListAnimator = null

    viewFunction?.invoke(newButton)
    addView(newButton, layoutParams)
}

fun QMUIGroupListView.getGroupInputItemView(
    title: String,
    inputType: Int,
    focusFunction: (EditText) -> Unit
): QMUICommonListItemView {
    val editTextView = EditText(this.context)
    editTextView.width = ScreenUtils.getAppScreenWidth() / 3
    editTextView.gravity = Gravity.END
    editTextView.inputType = inputType
    editTextView.background = null

    editTextView.let {
        it.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                focusFunction(it)
            }
        }
    }

    return createItemView(title).apply {
        accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        addAccessoryCustomView(editTextView)
    }
}

fun QMUIGroupListView.getGroupSwitchItemView(
    title: String,
    isChecked: Boolean = false,
    listener: CompoundButton.OnCheckedChangeListener
): QMUICommonListItemView {
    return createItemView(title).apply {
        accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        switch.isChecked = isChecked
        switch.setOnCheckedChangeListener(listener)
    }
}