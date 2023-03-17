package com.chinshry.base.util

/**
 * Created by chinshry on 2023/3/23.
 * Describe：数字工具类
 */

/**
 * Array<String?>转Array<Float>
 */
fun Array<String>.toFloatArray(): FloatArray {
    return this.mapNotNull { it.toFloatOrNull() }.toFloatArray()
}