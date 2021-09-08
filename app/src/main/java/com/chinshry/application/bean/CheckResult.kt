package com.chinshry.application.bean

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.application.bean
 * File Name:c
 * Describe：Application
 */
class CheckResult(var result: Int = RESULT_UNKNOWN, var value: String? = "") {
    companion object {
        const val RESULT_MAYBE_EMULATOR = 0 //可能是模拟器
        const val RESULT_EMULATOR = 1 //模拟器
        const val RESULT_UNKNOWN = 2 //可能是真机
    }

}