package com.chinshry.base.util

import android.content.Context
import android.os.Build

/**
 * Created by chinshry on 2022/03/21.
 * Describe：设备工具
 */
object DeviceUtil {
    private const val ANDROID_PLATFORM = "Android"
    private const val AMAZON_PLATFORM = "amazon-fireos"
    private const val AMAZON_DEVICE = "Amazon"

    fun getPlatform(): String {
        return if (isAmazonDevice()) {
            AMAZON_PLATFORM
        } else {
            ANDROID_PLATFORM
        }
    }

    /**
     * Function to check if the device is manufactured by Amazon
     * @return
     */
    fun isAmazonDevice(): Boolean {
        return Build.MANUFACTURER == AMAZON_DEVICE
    }

    /**
     * 获取应用版本信息
     * @return
     */
    fun getVersionName(context: Context): String {
        val packageManager = context.packageManager
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packInfo.versionName.toString()
    }

    fun getVersionCode(context: Context): Int {
        val packageManager = context.applicationContext.packageManager
        val info = packageManager.getPackageInfo(context.packageName, 0)
        return info.longVersionCode.toInt()
    }
}