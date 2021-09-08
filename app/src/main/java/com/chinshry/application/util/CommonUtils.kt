package com.chinshry.application.util

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.application.util
 * File Name:CommonUtils
 * Describe：Application
 */
object CommonUtils {
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
        val BUFFER_SIZE = 512
        val buffer = ByteArray(BUFFER_SIZE)
        val result = StringBuilder()
        try {
            while (true) {
                val read: Int = bufferedInputStream.read(buffer)
                if (read > 0) {
                    result.append(String(buffer, 0, read))
                }
                if (read < BUFFER_SIZE) {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result.toString()
    }
}