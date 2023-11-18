package com.chinshry.capp

import android.app.Application
import cn.vove7.andro_accessibility_api.AccessibilityApi
import com.blankj.utilcode.util.LogUtils
import com.chinshry.tool.service.AppAccessibilityService
import com.chinshry.base.ActivityLifecycleCallbackWrapper
import com.chinshry.base.view.CRefreshHeader
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * Created by chinshry on 2022/1/7.
 * Describe：
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbackWrapper())
        initLog()
        initSmartFresh()
        AccessibilityApi.init(this, AppAccessibilityService::class.java)
    }

    private fun initLog() {
        LogUtils.getConfig().apply {
            setBorderSwitch(false)
            isLogHeadSwitch = false
        }
    }

    /**
     * 初始化刷新头
     */
    private fun initSmartFresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setHeaderHeight(80f)
            layout.setFooterHeight(80f)

            ClassicsHeader.REFRESH_HEADER_PULLING = "下拉刷新"
            ClassicsHeader.REFRESH_HEADER_RELEASE = "松开刷新"
            ClassicsHeader.REFRESH_HEADER_FINISH = "刷新成功"
            ClassicsHeader.REFRESH_HEADER_REFRESHING = ""
            ClassicsHeader.REFRESH_HEADER_LOADING = ""

            CRefreshHeader(context).init()
        }
    }

}