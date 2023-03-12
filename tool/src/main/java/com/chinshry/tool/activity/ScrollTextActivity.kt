package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.util.dp
import com.chinshry.base.util.getGroupInputItemView
import com.chinshry.base.util.getGroupSwitchItemView
import com.chinshry.tool.R
import com.chinshry.tool.databinding.ActivityScrollTextBinding
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView

/**
 * Created by chinshry on 2023/03/10.
 * Describe：滚动文本页面
 */
@BuryPoint(pageName = "滚动文本组件测试页")
@Route(path = Router.SCROLL_TEXT)
class ScrollTextActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityScrollTextBinding.inflate(layoutInflater) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initView()

        viewBinding.llActivity.setOnTouchListener { _, _ ->
            viewBinding.llActivity.requestFocus()
        }
    }

    private fun initView() {
        QMUIGroupListView.newSection(this)
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "设置短文本"
                ) { _, isChecked ->
                    viewBinding.tvScroll.setScrollText(
                        if (isChecked) {
                            getString(R.string.test_string_short)
                        } else {
                            getString(R.string.test_string_long)
                        }
                    )
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "显示边缘渐变",
                    viewBinding.tvScroll.getFadingEdgeEnable()
                ) { _, isChecked ->
                    viewBinding.tvScroll.setFadingEdgeEnable(isChecked)
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "显示滚动条",
                    viewBinding.tvScroll.getScrollBarEnable()
                ) { _, isChecked ->
                    viewBinding.tvScroll.setScrollBarEnable(isChecked)
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupInputItemView(
                    title = "设置最大行数",
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                ) {
                    it.text.toString().toIntOrNull()?.let { inputValue ->
                        viewBinding.tvScroll.apply {
                            setMaxLines(inputValue)
                            adjustScrollOffset()
                        }
                    }
                },
                null
            )
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)
    }
}