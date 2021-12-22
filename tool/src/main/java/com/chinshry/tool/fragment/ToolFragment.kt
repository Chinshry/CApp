package com.chinshry.tool.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.tool.R
import com.chinshry.base.BaseFragment
import com.chinshry.base.bean.Router
import com.chinshry.base.util.DevicesCheckUtil
import com.chinshry.base.util.window.DialogManager
import com.chinshry.base.util.window.MyWindow
import com.chinshry.base.util.window.WindowPriority
import com.chinshry.base.util.window.WindowType
import kotlinx.android.synthetic.main.fragment_tool.*

/**
 * Created by chinshry on 2021/12/23.
 * File Name: UtilFragment.kt
 * Describe：工具Fragment
 */
class ToolFragment : BaseFragment() {
    override fun setLayout(): Int {
        return R.layout.fragment_tool
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun initView() {
        btn_test.setOnClickListener {
            testPriorityQueue()
        }

        btn_root.setOnClickListener {
            if (!DevicesCheckUtil.checkRootAndEmulator(requireContext())) {
                ToastUtils.showShort("您的设备未root，且为真机")
            }
        }

        btn_picture_select.setOnClickListener {
            ARouter.getInstance().build(Router.PICTURE_SELECT).navigation()
        }
    }

    private fun testPriorityQueue() {
        DialogManager.addWindow(
            MyWindow(
                "testData",
                WindowType.Dialog,
                WindowPriority.MIDDLE
            )
        )

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            {
                DialogManager.showWindow()
            }, 3000
        )
    }


}