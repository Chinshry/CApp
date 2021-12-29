package com.chinshry.tool.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.view.VerCodeLayout
import com.chinshry.base.view.clickWithTrigger
import com.chinshry.tool.R
import kotlinx.android.synthetic.main.dialog_code_input.*


/**
 * Created by chinshry on 2021/12/29.
 * File Name: EditInputActivity.kt
 * Describe：验证码输入框组件
 */
@BuryPoint(pageName = "验证码输入框组件")
@Route(path = Router.PICTURE_CODE_INPUT)
class CodeInputActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_code_input)
        initView()
    }

    private fun initView() {
        et_verCode.setOnInputListener(object : VerCodeLayout.OnInputListener {
            override fun onComplete(code: String?) {
                // 提交接口请求
                ToastUtils.showShort(code)
            }

            override fun onInput() {
            }
        })

        tv_get_sms_code.clickWithTrigger {
            ToastUtils.showShort(it.text.toString())
        }
    }

}