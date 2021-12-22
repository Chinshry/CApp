package com.chinshry.base.bean

import androidx.fragment.app.Fragment

/**
 * Created by chinshry on 2021/12/23.
 * File Name: Bean.kt
 * Describe：数据类
 */

data class FragmentBean(
    val fragment: Fragment,
    val model: Int
)

data class TabBean(
    var model: Int? = null,
    var title: String? = null,
    var normalIcon: Int? = null,
    var selectIcon: Int? = null,
    var badge: Int? = null
)
