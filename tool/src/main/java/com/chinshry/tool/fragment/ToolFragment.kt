package com.chinshry.tool.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.tool.R
import com.chinshry.base.BaseFragment
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.dialog.MyDialog
import com.chinshry.base.BaseDialog
import com.chinshry.base.util.DevicesCheckUtil
import com.chinshry.base.util.WindowLevel
import com.chinshry.base.util.WindowManager
import com.chinshry.base.view.VerCodeEditText
import com.chinshry.base.view.VerCodeLayout
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
            WindowManager.addWindow(
                WindowLevel.LOW
            ) {
                showNormalDialog()
            }

        }

        btn_root.clickWithTrigger {
            if (!DevicesCheckUtil.checkRootAndEmulator(requireContext())) {
                ToastUtils.showShort("您的设备未root，且为真机")
            }
        }

        btn_code_input.clickWithTrigger {
            showCodeInputDialog()
        }

        btn_picture_select.clickWithTrigger {
            ARouter.getInstance().build(Router.PICTURE_SELECT).navigation()
        }
    }

    @BuryPoint(pageName = "验证码输入组件弹框")
    private fun showCodeInputDialog() {
        BaseDialog()
            .builder(R.layout.dialog_code_input)
            .setWidthPercent(1.0f)
            .setWindowAnimations()
            .setGravity(Gravity.BOTTOM)
            .initViewById<VerCodeEditText>(
                id = R.id.et_verCode,
            ) { view ->
                view.setOnInputListener(object : VerCodeLayout.OnInputListener {
                    override fun onComplete(code: String?) {
                        // 提交接口请求
                        ToastUtils.showShort(code)
                    }

                    override fun onInput() {}
                })
            }
            .initViewById<TextView>(
                id = R.id.tv_get_sms_code,
            ) { view ->
                view.clickWithTrigger {
                    ToastUtils.showShort(it.text.toString())
                }
            }
            .showInput()
            .show()
    }

    @BuryPoint(pageName = "测试弹窗")
    private fun showNormalDialog() {
        MyDialog()
            .setTitle(null)
            .setMsg("内容内容")
            .setConfirmBtn("ok") { ToastUtils.showShort("toast!!")}
            .setCancelBtn("bye")
            .show()
    }

}