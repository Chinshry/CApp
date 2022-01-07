package com.chinshry.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.chinshry.base.util.WindowManagerList


/**
 * Created by chinshry on 2022/1/7.
 * File Name: ActivityLifecycleCallbackWrapper
 * Describe：
 */
class ActivityLifecycleCallbackWrapper : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.d("csTest showWindow")
        WindowManagerList.showWindow()
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}