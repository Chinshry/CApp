package com.chinshry.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.chinshry.application.bean.Router
import kotlinx.android.synthetic.main.activity_test.*
import java.io.BufferedReader
import java.util.*
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import com.chinshry.application.util.DevicesCheckUtil


@Route(path = Router.ACTIVITY_TEST)
class TestActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
    }

    private fun initView() {
        tv_msg.movementMethod = ScrollingMovementMethod.getInstance()
        btn_test.setOnClickListener {
            if (!DevicesCheckUtil.checkRootAndEmulator(this)) {
                Toast.makeText(this, "您的设备未root，且为真机", Toast.LENGTH_LONG).show()
            }
        }
    }

}