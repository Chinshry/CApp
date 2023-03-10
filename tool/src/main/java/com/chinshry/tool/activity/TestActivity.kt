package com.chinshry.tool.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.StringUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.R
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.util.dp
import com.chinshry.tool.databinding.ActivityTestBinding

/**
 * Created by chinshry on 2022/11/30.
 * Describe：测试页面
 */
@BuryPoint(pageName = "测试页面")
@Route(path = Router.TEST)
class TestActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        init()
    }

    private fun init() {
        viewBinding.scrollText.setScrollTextSize(16.dp.toFloat())
        viewBinding.scrollText.setTextLineHeight(20.dp)
        viewBinding.scrollText.setScrollText(StringUtils.getString(R.string.test_string_long))
    }

}