package com.chinshry.tool.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.base.BaseDialog
import com.chinshry.base.BaseFragment
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.dialog.MyDialog
import com.chinshry.base.util.DevicesCheckUtil
import com.chinshry.base.util.WindowConfig
import com.chinshry.base.util.WindowManagerList
import com.chinshry.base.util.addButton
import com.chinshry.base.view.VerCodeEditText
import com.chinshry.base.view.VerCodeLayout
import com.chinshry.base.view.clickWithTrigger
import com.chinshry.tool.R
import com.chinshry.tool.databinding.FragmentToolBinding


/**
 * Created by chinshry on 2021/12/23.
 * Describe：工具Fragment
 */
@BuryPoint(pageName = "工具Fragment")
class ToolFragment : BaseFragment<FragmentToolBinding>(FragmentToolBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun initView() {
        viewBinding.llBtn.addButton("无障碍") {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.ACCESSIBILITY).navigation()
            }
        }
        viewBinding.llBtn.addButton("测试弹窗") {
            it.clickWithTrigger {
                WindowManagerList.addWindow(
                    WindowConfig.DEVICE_CHECK,
                ) {
                    showNormalDialog("DEVICE_CHECK")
                }

                WindowManagerList.addWindow(
                    WindowConfig.PERMISSION_HINT,
                ) {
                    showNormalDialog("PERMISSION_HINT")
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    showNormalDialog("JUMP")
                }, 1000)

                Handler(Looper.getMainLooper()).postDelayed({
                    WindowManagerList.addWindow(
                        WindowConfig.CIPHER
                    ) {
                        showNormalDialog("CIPHER")
                    }
                }, 2000)

                Handler(Looper.getMainLooper()).postDelayed({
                    WindowManagerList.addWindow(
                        WindowConfig.UPDATE
                    ) {
                        showNormalDialog("UPDATE")
                    }
                }, 3000)

                // Handler(Looper.getMainLooper()).postDelayed({
                //     WindowManagerList.clear()
                //     showNormalDialog("UPDATE HIGH")
                // }, 2000)

                Handler(Looper.getMainLooper()).postDelayed({
                    WindowManagerList.addWindow(
                        WindowConfig.CARING_CHOOSE
                    ) {
                        showNormalDialog("CARING_CHOOSE")
                    }
                }, 4000)

            }
        }

        viewBinding.llBtn.addButton("系统设置") {
            it.clickWithTrigger {
                AppUtils.launchAppDetailsSettings()
            }
        }

        viewBinding.llBtn.addButton("ROOT和模拟器检测") {
            it.clickWithTrigger {
                if (!DevicesCheckUtil.checkRootAndEmulator(requireContext())) {
                    ToastUtils.showShort("您的设备未root，且为真机")
                }
            }
        }

        viewBinding.llBtn.addButton("验证码输入框组件") {
            it.clickWithTrigger {
                showCodeInputDialog()
            }
        }

        viewBinding.llBtn.addButton(getString(R.string.page_scroll_text)) {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.SCROLL_TEXT).navigation()
            }
        }

        viewBinding.llBtn.addButton(getString(R.string.page_scroll_3d)) {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.SCROLL_3D).navigation()
            }
        }

        viewBinding.llBtn.addButton(getString(R.string.page_picture_select)) {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.PICTURE_SELECT).navigation()
            }
        }

        viewBinding.llBtn.addButton(getString(R.string.page_masking)) {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.MASKING).navigation()
            }
        }

        viewBinding.llBtn.addButton(getString(R.string.page_test)) {
            it.clickWithTrigger {
                ARouter.getInstance().build(Router.TEST).navigation()
            }
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

                    override fun onInput() = Unit
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
    private fun showNormalDialog(
        message: String? = null,
        title: String? = null,
    ) {
        MyDialog()
            .setTitle(title)
            .setMsg(message)
            .setConfirmBtn("OK") {
                ARouter.getInstance().build(Router.PICTURE_SELECT).navigation()
            }
            .setCancelBtn("CANCEL")
            .setWindowShow(true)
            .show()
    }

}