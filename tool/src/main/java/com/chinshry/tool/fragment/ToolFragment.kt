package com.chinshry.tool.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.tool.R
import com.chinshry.base.BaseFragment
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.dialog.MyDialog
import com.chinshry.base.util.DevicesCheckUtil
import com.chinshry.base.util.WindowLevel
import com.chinshry.base.util.WindowManager
import com.chinshry.base.view.clickWithTrigger
import kotlinx.android.synthetic.main.fragment_tool.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe：工具Fragment
 */
@BuryPoint(pageName = "工具Fragment")
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
        btn_test.clickWithTrigger {
            testPriorityQueue()
        }

        btn_root.clickWithTrigger {
            if (!DevicesCheckUtil.checkRootAndEmulator(requireContext())) {
                ToastUtils.showShort("您的设备未root，且为真机")
            }
        }

        btn_code_input.clickWithTrigger {
            ARouter.getInstance().build(Router.PICTURE_CODE_INPUT).navigation()
        }

        btn_picture_select.clickWithTrigger {
            ARouter.getInstance().build(Router.PICTURE_SELECT).navigation()
        }
    }

    private fun testPriorityQueue() {
        WindowManager.addWindow(
            WindowLevel.LOW
        ) {
            MyDialog()
                .setTitle(null)
                .setMsg("内容")
                .setConfirmBtn("ok") { ToastUtils.showShort("toast!!")}
                .setCancelBtn("bye")
                .show()
        }
    }


}