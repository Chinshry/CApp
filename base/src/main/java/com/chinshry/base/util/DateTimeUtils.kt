package com.chinshry.base.util

import com.blankj.utilcode.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chinshry on 2022/03/21.
 * Describe：时间工具
 */
object DateTimeUtils {

    const val MMDD = "MM-dd"
    const val YYYYMMDD = "yyyy-MM-dd"
    const val YYYYMMDDHHMM = "yyyy-MM-dd HH:mm"
    const val YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss"
    const val YYYYNIANMYUED = "yyyy年M月d日"
    const val YYYYNIANMMYUEDD = "yyyy年MM月dd日"

    /**
     * 根据传入的pattern格式化String类型
     * @param date
     * @param parsePattern 输入解析的pattern
     * @param formatPattern 输出格式的pattern
     * @return 格式化之后的String类型
     */
    fun parseDate(
        date: String?,
        parsePattern: String = YYYYMMDDHHMMSS,
        formatPattern: String = YYYYMMDDHHMMSS
    ): String {
        if (date == null) {
            return ""
        }
        try {
            val parsedDate = parseToDate(date, parsePattern) ?: return ""
            return formatToString(parsedDate, formatPattern)
        } catch (e: Exception) {
            return ""
        }
    }

    /**
     * 根据传入的pattern解析String类型
     * @param date
     * @param pattern
     * @return 返回解析的Date类型
     */
    fun parseToDate(date: String?, pattern: String = YYYYMMDDHHMMSS): Date? {
        if (date == null) {
            return null
        }
        return try {
            val format = SimpleDateFormat(pattern, Locale.CHINA)
            format.parse(date)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 根据传入的pattern格式化Date类型
     * @param date
     * @param pattern
     * @return 格式化之后的String类型
     */
    fun formatToString(date: Date?, pattern: String): String {
        if (date == null) {
            return ""
        }
        return try {
            val format = SimpleDateFormat(pattern, Locale.CHINA)
            format.format(date) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 获取今天的整日期时间戳 无 时分秒
     */
    fun getTodayMills(): Long {
        val format = SimpleDateFormat(YYYYMMDD, Locale.CHINA)
        val todayString = TimeUtils.getNowString(format)
        return TimeUtils.string2Millis(todayString, YYYYMMDD)
    }

}