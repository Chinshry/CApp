package com.chinshry.capp

import android.app.Application
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
        initSmartFresh()
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