package com.chinshry.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.chinshry.application.bean.Router
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity() {

    private val TAG = "chinshry"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        btn_test.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_TEST).navigation()
        }

        btn_tabView.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_TABVIEW).navigation()
        }

        btn_scrollView.setOnClickListener {
            ARouter.getInstance().build(Router.ACTIVITY_SCROLLVIEW).navigation()
        }

        btn_viewModel.setOnClickListener {
            ARouter.getInstance().build(Router.FRAGMENT_VIEWMODEL).navigation()
        }
    }

    private fun init() {
        ARouter.openLog();
        ARouter.openDebug()
        ARouter.init(application);
    }


}