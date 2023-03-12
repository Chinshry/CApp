package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.tool.R
import com.chinshry.tool.databinding.ActivityScroll3dBinding

/**
 * Created by chinshry on 2023/03/11.
 * Describe：滚动3D组件
 */
@BuryPoint(pageName = "滚动3D组件")
@Route(path = Router.SCROLL_3D)
class Scroll3DActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityScroll3dBinding.inflate(layoutInflater) }
    private val imageResourceList by lazy {
        listOf(
            R.mipmap.example_1,
            R.mipmap.example_2,
            R.mipmap.example_3,
            R.mipmap.example_4,
            R.mipmap.example_5,
            R.mipmap.example_6
        )
    }

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
        viewBinding.sl3d.setData(imageResourceList)
    }

}