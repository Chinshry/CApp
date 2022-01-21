package com.chinshry.base.util

/**
 * Created by chinshry on 2021/12/23.
 * Describe：数据脱敏工具类
 */
object MaskingUtil {

    /**
     * 证件号脱敏 后4
     * @param source String?
     * @return String
     */
    fun getMaskingCertNum(source: String?): String {
        return getMaskingString(source, 0, 4)
    }

    /**
     * 姓名脱敏 脱敏规则：姓**，忽略复姓，英文则只保留第一个字母
     * @param source String?
     * @return String
     */
    fun getMaskingName(source: String?): String {
        return getMaskingString(source, 1, 0)
    }

    /**
     * 手机号脱敏 前3后4
     * @param source String?
     * @return String
     */
    fun getMaskingMobile(source: String?): String {
        return getMaskingString(source, 3, 4)
    }

    /**
     * 银行卡脱敏
     * @param source String?
     * @return String
     */
    fun getMaskingBankNum(source: String?): String {
        return getMaskingStringAll(source, 0, 4, "****  ****  ****  ")
    }

    /**
     * 获取末尾字符串
     * 如 getEndString("1234567890", 4) return "7890"
     * @param source 源字符串
     * @param endIndex Int
     * @return String
     */
    fun getEndString(source: String?, endIndex: Int): String {
        return getMaskingStringAll(source, 0, endIndex, "")
    }

    /**
     * 字符串脱敏逐个
     * 如 getMaskingString("1234567890", 3, 3, "*") return "123****890"
     * @param source 源字符串
     * @param startIndex 起始保留数
     * @param endIndex 截止保留数
     * @param replacement 替换字符
     * @return String
     */
    fun getMaskingString(
        source: String?,
        startIndex: Int,
        endIndex: Int,
        replacement: String = "*"
    ): String {
        if (source.isNullOrBlank()) return ""
        return StringBuffer(source).replace(
            Regex("(?<=\\w{$startIndex})\\w(?=\\w{$endIndex})"),
            replacement
        )
    }

    /**
     * 字符串脱敏
     * 如 getMaskingStringAll("1234567890", 0, 4, "****  ****  ****  ") return "****  ****  ****  7890"
     * @param source 源字符串
     * @param startIndex 起始保留数
     * @param endIndex 截止保留数
     * @param replacement 替换字符
     * @return String
     */
    fun getMaskingStringAll(
        source: String?,
        startIndex: Int,
        endIndex: Int,
        replacement: String
    ): String {
        if (source.isNullOrBlank()) return ""
        return StringBuffer(source).replace(
            Regex("(?<=\\w{$startIndex})(\\w+)(?=\\w{$endIndex})"),
            replacement
        )
    }

}