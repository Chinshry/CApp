package com.chinshry.mytest

import android.app.Application
import com.chinshry.base.ActivityLifecycleCallbackWrapper


/**
 * Created by chinshry on 2022/1/7.
 * Describe：
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbackWrapper())
    }
}