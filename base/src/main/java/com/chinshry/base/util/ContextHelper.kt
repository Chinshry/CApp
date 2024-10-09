package com.chinshry.base.util

import android.app.Activity
import android.app.Application
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils


object ContextHelper {
    fun getApplication(): Application {
        return Utils.getApp()
    }

    fun getActivity(): Activity {
        return ActivityUtils.getTopActivity()
    }
}
