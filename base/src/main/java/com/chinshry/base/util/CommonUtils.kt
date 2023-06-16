package com.chinshry.base.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.chinshry.base.R
import com.chinshry.base.bean.Constants
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.lang.reflect.Type


/**
 * Created by chinshry on 2021/12/23.
 * Describe：通用工具
 */
object CommonUtils {
    /**
     * 比较版本大小
     * @param targetVersion 目标版本
     * @param targetVersionCode 目标versionCode
     * @return Boolean 当前版本比目标版本小 true 当前版本等于或者大与目标版本 false
     */
    fun compareAppVersion(targetVersion: String?, targetVersionCode: String? = null): Boolean {
        if (targetVersion.isNullOrBlank()) {
            return false
        }
        val targetArr = targetVersion.split(".")
        val currentArr = DeviceUtil.getVersionName(Utils.getApp()).split(".")

        val length = if (currentArr.size < targetArr.size) {
            currentArr.size
        } else {
            targetArr.size
        }

        for (i in 0 until length) {
            when {
                targetArr[i].toInt() > currentArr[i].toInt() -> {
                    return true
                }
                targetArr[i].toInt() < currentArr[i].toInt() -> {
                    return false
                }
                targetArr[i].toInt() == currentArr[i].toInt() -> {
                    if (i == length - 1) {
                        return if (currentArr.size == targetArr.size) {
                            val currentCode = DeviceUtil.getVersionCode(Utils.getApp())
                            val targetCode = targetVersionCode?.toIntOrNull() ?: 0
                            currentCode < targetCode
                        } else {
                            currentArr.size < targetArr.size
                        }
                    }
                }
            }
        }
        return false
    }

    fun getModuleName(className: String): String? {
        return className.split(".").getOrNull(2)
    }

    /**
     * 字符串转对象 捕获异常
     * @param data Any?
     * @param type Class<T>
     * @return T?
     */
    fun <T> string2object(data: Any?, type: Class<T>): T? {
        return try {
            GsonUtils.fromJson(
                if (data is String) data else GsonUtils.toJson(data),
                type
            )
        } catch (e: Exception) {
            LogUtils.e(
                "string2object: " + getStringById(R.string.json_error_msg) + e.message
            )
            null
        }
    }

    fun <T> string2object(data: Any?, type: Type): T? {
        return try {
            GsonUtils.fromJson(
                if (data is String) data else GsonUtils.toJson(data),
                type
            )
        } catch (e: Exception) {
            LogUtils.e(
                "string2object: " + getStringById(R.string.json_error_msg) + e.message
            )
            null
        }
    }

    fun getJsonStringWithIndex(
        args: JSONArray,
        index: Int
    ): String {
        return if (args.length() > index) args[index].toString() else ""
    }

    fun getJsonObjectWithIndex(
        args: JSONArray,
        index: Int
    ): JSONObject {
        return if (args.length() > index) args.getJSONObject(index) else JSONObject()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> parseAndGetValueOrDefault(
        params: String?,
        key: String,
        defValue: T
    ): T {
        return try {
            val data = com.alibaba.fastjson.JSONObject.parseObject(params)
            getValueOrDefault(data, key, defValue)
        } catch (e: Exception) {
            defValue
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> parseAndGetValueOrNull(
        params: String?,
        key: String,
    ): T? {
        return try {
            val data = com.alibaba.fastjson.JSONObject.parseObject(params)
            getValueOrNull(data, key)
        } catch (e: Exception) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValueOrDefault(
        params: Any,
        key: String,
        defValue: T
    ): T {
        try {
            when (params) {
                is JSONObject -> {
                    if (params.has(key)) {
                        when (defValue) {
                            is Int -> return (params.getInt(key) as? T) ?: defValue
                            is Boolean -> return (params.getBoolean(key) as? T) ?: defValue
                            is String -> return (params.getString(key) as? T) ?: defValue
                        }
                    }
                }
                is com.alibaba.fastjson.JSONObject -> {
                    if (params.containsKey(key)) {
                        when (defValue) {
                            is Int -> return (params.getInteger(key) as? T) ?: defValue
                            is Boolean -> return (params.getBoolean(key) as? T) ?: defValue
                            is String -> return (params.getString(key) as? T) ?: defValue
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
        return defValue
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getValueOrNull(
        params: Any,
        key: String
    ): T? {
        try {
            when (params) {
                is JSONObject -> {
                    if (params.has(key)) {
                        when (getType<T>()) {
                            java.lang.Integer::class.java -> return params.getInt(key) as? T
                            java.lang.Boolean::class.java -> return params.getBoolean(key) as? T
                            java.lang.String::class.java -> return params.getString(key) as? T
                        }
                    }
                }
                is com.alibaba.fastjson.JSONObject -> {
                    if (params.containsKey(key)) {
                        when (getType<T>()) {
                            java.lang.Integer::class.java -> return params.getInteger(key) as? T
                            java.lang.Boolean::class.java -> return params.getBoolean(key) as? T
                            java.lang.String::class.java -> return params.getString(key) as? T
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
        return null
    }

    inline fun <reified T> getType(): Class<T> {
        return T::class.java
    }
    
    @SuppressLint("PrivateApi")
    fun getProperty(propName: String?): String? {
        var value: String? = null
        val roSecureObj: Any?
        try {
            roSecureObj = Class.forName("android.os.SystemProperties")
                .getMethod("get", String::class.java)
                .invoke(null, propName)
            if (roSecureObj != null) value = roSecureObj as String?
        } catch (e: Exception) {
            value = null
        }
        return value
    }

    fun exec(command: String): String? {
        var bufferedOutputStream: BufferedOutputStream? = null
        var bufferedInputStream: BufferedInputStream? = null
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("sh")
            bufferedOutputStream = BufferedOutputStream(process.outputStream)
            bufferedInputStream = BufferedInputStream(process.inputStream)
            bufferedOutputStream.write(command.toByteArray())
            bufferedOutputStream.write("\n".toByteArray())
            bufferedOutputStream.flush()
            bufferedOutputStream.close()
            process.waitFor()
            getStrFromBufferInputSteam(bufferedInputStream)
        } catch (e: Exception) {
            null
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            process?.destroy()
        }
    }

    private fun getStrFromBufferInputSteam(bufferedInputStream: BufferedInputStream?): String {
        if (null == bufferedInputStream) {
            return ""
        }
        val buffer = ByteArray(Constants.BUFFER_SIZE)
        val result = StringBuilder()
        try {
            while (true) {
                val read: Int = bufferedInputStream.read(buffer)
                if (read > 0) {
                    result.append(String(buffer, 0, read))
                }
                if (read < Constants.BUFFER_SIZE) {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result.toString()
    }

    private fun jumpAccessibilityServiceSettings(
        cls: Class<*>,
        context: Context = ContextHelper.getApplication()
    ) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putComponent(context.packageName, cls)
        context.startActivity(intent)
    }

    private fun Intent.putComponent(pkg: String, cls: Class<*>) {
        val cs = ComponentName(pkg, cls.name).flattenToString()
        val bundle = Bundle()
        bundle.putString(":settings:fragment_args_key", cs)
        putExtra(":settings:fragment_args_key", cs)
        putExtra(":settings:show_fragment_args", bundle)
    }

    fun dp2px(context: Context?, dp: Float): Float {
        return (QMUIDisplayHelper.getDensity(context) * dp + 0.5f)
    }

}