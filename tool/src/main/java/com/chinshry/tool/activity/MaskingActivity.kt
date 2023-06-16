package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.util.dp
import com.chinshry.base.util.getGroupInputItemView
import com.chinshry.base.util.getGroupSwitchItemView
import com.chinshry.base.view.MaskingTextView
import com.chinshry.base.view.MaskingTextView.MaskType
import com.chinshry.tool.R
import com.chinshry.tool.databinding.ActivityListBaseBinding
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView


/**
 * Created by chinshry on 2021/12/23.
 * Describe：脱敏组件测试
 */
@BuryPoint(pageName = "脱敏组件测试")
@Route(path = Router.MASKING)
class MaskingActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityListBaseBinding.inflate(layoutInflater) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.customHeaderBar.setPageTitle(getString(R.string.page_masking))
        initGroupListView()

        viewBinding.llActivity.setOnTouchListener { _, _ ->
            viewBinding.llActivity.requestFocus()
        }
    }

    private fun initGroupListView() {
        QMUIGroupListView.newSection(this)
            .setTitle("常用脱敏")
            .addItemView(getGroupMaskingItemView("手机号", MaskType.MOBILE), null)
            .addItemView(getGroupMaskingItemView("姓名", MaskType.NAME), null)
            .addItemView(getGroupMaskingItemView("证件号", MaskType.CRT), null)
            .addItemView(getGroupMaskingItemView("银行卡", MaskType.BANKCARD), null)
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)

        val itemCustomMaskView = getGroupMaskingItemView("脱敏字符串", MaskType.CUSTOM)
        val itemCustomMaskInputView = itemCustomMaskView.accessoryContainerView.getChildAt(0) as? MaskingTextView

        val refreshMaskView = {
            itemCustomMaskInputView?.requestFocus()
            viewBinding.llActivity.requestFocus()
        }

        QMUIGroupListView.newSection(this)
            .setTitle("自定义脱敏")
            .addItemView(itemCustomMaskView, null)
            .addItemView(
                viewBinding.groupListView.getGroupInputItemView(
                    title = "脱敏起始index 从开头起数",
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                ) {
                    itemCustomMaskInputView?.maskStart = it.text.toString().toIntOrNull()
                    refreshMaskView()
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupInputItemView(
                    title = "脱敏结尾index 从末尾起数",
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                ) {
                    itemCustomMaskInputView?.maskEnd = it.text.toString().toIntOrNull()
                    refreshMaskView()
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupInputItemView(
                    title = "脱敏字符",
                    inputType = EditorInfo.TYPE_CLASS_TEXT
                ) {
                    itemCustomMaskInputView?.maskReplacement = it.text.toString()
                    refreshMaskView()
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "脱敏字符替代所有"
                ) { _, isChecked ->
                    itemCustomMaskInputView?.maskReplacementAll = isChecked
                    refreshMaskView()
                },
                null
            )
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)
    }

    private fun getGroupMaskingItemView(title: String, type: MaskType): QMUICommonListItemView {
        val maskingView = MaskingTextView(this)
        maskingView.width = ScreenUtils.getAppScreenWidth() / 2
        maskingView.maskType = type
        maskingView.gravity = Gravity.END

        val groupItemView: QMUICommonListItemView = viewBinding.groupListView.createItemView(title)
        groupItemView.accessoryType = QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM
        groupItemView.addAccessoryCustomView(maskingView)

        return groupItemView
    }
}