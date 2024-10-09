package com.chinshry.tool.service

import android.content.Context
import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.auto.core.AppScope
import com.youth.banner.util.LogUtils

/**
 * Created by chinshry on 2023/06/15.
 * Describe：无障碍服务
 */
class AppAccessibilityService : AccessibilityApi() {
    companion object {
        fun initService(context: Context) {
            init(context, AppAccessibilityService::class.java)
        }
    }

    override val enableListenPageUpdate: Boolean = true

    override fun onCreate() {
        baseService = this
        super.onCreate()
    }

    override fun onDestroy() {
        baseService = null
        super.onDestroy()
    }

    //页面更新回调
    override fun onPageUpdate(currentScope: AppScope) {
        LogUtils.d("onPageUpdate: $currentScope")
    }
}