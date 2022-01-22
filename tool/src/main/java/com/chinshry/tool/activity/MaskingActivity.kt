package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.view.MaskingTextView
import com.chinshry.base.view.MaskingTextView.MaskType
import com.chinshry.tool.R
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.android.synthetic.main.activity_masking.*


/**
 * Created by chinshry on 2021/12/23.
 * Describe：脱敏测试
 */
@BuryPoint(pageName = "脱敏测试")
@Route(path = Router.MASKING)
class MaskingActivity : BaseActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masking)
        initGroupListView()

        rl_activity.setOnTouchListener { _, _ ->
            rl_activity.requestFocus()
        }
    }

    private fun initGroupListView() {
        QMUIGroupListView.newSection(this)
            .setTitle("常用脱敏")
            .addItemView(getGroupMaskingItemView("手机号", MaskType.MOBILE), null)
            .addItemView(getGroupMaskingItemView("姓名", MaskType.NAME), null)
            .addItemView(getGroupMaskingItemView("证件号", MaskType.CRT), null)
            .addItemView(getGroupMaskingItemView("银行卡", MaskType.BANKCARD), null)
            .setMiddleSeparatorInset(SizeUtils.dp2px(16F), SizeUtils.dp2px(16F))
            .addTo(groupListView)

        val itemCustomMaskView = getGroupMaskingItemView("脱敏字符串", MaskType.CUSTOM)
        val itemCustomMaskInputView = itemCustomMaskView.accessoryContainerView.getChildAt(0) as? MaskingTextView

        val refreshMaskView = {
            itemCustomMaskInputView?.requestFocus()
            rl_activity.requestFocus()
        }

        val itemWithSwitch = groupListView.createItemView("脱敏字符替代所有")
        itemWithSwitch.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_SWITCH
        itemWithSwitch.switch.setOnCheckedChangeListener { _, isChecked ->
            itemCustomMaskInputView?.maskReplacementAll = isChecked
            refreshMaskView()
        }

        QMUIGroupListView.newSection(this)
            .setTitle("自定义脱敏")
            .addItemView(itemCustomMaskView, null)
            .addItemView(
                getGroupItemView(
                    title = "脱敏起始index 从开头起数",
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                ) {
                    itemCustomMaskInputView?.maskStart = it.text.toString().toIntOrNull()
                    refreshMaskView()
                },
                null
            )
            .addItemView(
                getGroupItemView(
                    title = "脱敏结尾index 从末尾起数",
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                ) {
                    itemCustomMaskInputView?.maskEnd = it.text.toString().toIntOrNull()
                    refreshMaskView()
                },
                null
            )
            .addItemView(
                getGroupItemView(
                    title = "脱敏字符",
                    inputType = EditorInfo.TYPE_CLASS_TEXT
                ) {
                    itemCustomMaskInputView?.maskReplacement = it.text.toString()
                    refreshMaskView()
                },
                null
            )
            .addItemView(itemWithSwitch, null)
            .setMiddleSeparatorInset(SizeUtils.dp2px(16F), SizeUtils.dp2px(16F))
            .addTo(groupListView)
    }

    private fun getGroupMaskingItemView(title: String, type: MaskType): QMUICommonListItemView {
        val maskingView = MaskingTextView(this)
        maskingView.width = ScreenUtils.getAppScreenWidth() / 2
        maskingView.maskType = type
        maskingView.gravity = Gravity.END

        val groupItemView: QMUICommonListItemView = groupListView.createItemView(title)
        groupItemView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        groupItemView.addAccessoryCustomView(maskingView)

        return groupItemView
    }

    private fun getGroupItemView(
        title: String,
        inputType: Int,
        focusFunction: (EditText) -> Unit
    ): QMUICommonListItemView {
        val editTextView = EditText(this)
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

        val groupItemView: QMUICommonListItemView = groupListView.createItemView(title)
        groupItemView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        groupItemView.addAccessoryCustomView(editTextView)

        return groupItemView
    }

}