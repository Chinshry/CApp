package com.chinshry.mytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.chinshry.mytest.bean.Router
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        btn_tabView.setOnClickListener {
            println("chengshu btn_tabView")
            ARouter.getInstance().build(Router.ACTIVITY_TABVIEW).navigation()
        }

        btn_scrollView.setOnClickListener {
            println("chengshu btn_scrollView")
            ARouter.getInstance().build(Router.ACTIVITY_SCROLLVIEW).navigation()
        }
    }

    private fun init() {
        ARouter.openLog();
        ARouter.openDebug()
        ARouter.init(application);
    }


}