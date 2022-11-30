package com.chinshry.tool.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.tool.R


/**
 * Created by chinshry on 2022/11/30.
 * Describe：测试页面
 */
@BuryPoint(pageName = "测试页面")
@Route(path = Router.TEST)
class TestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

}