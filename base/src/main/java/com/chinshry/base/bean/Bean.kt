package com.chinshry.base.bean

import androidx.fragment.app.Fragment

/**
 * Created by chinshry on 2021/12/23.
 * File Name: Bean.kt
 * Describe：数据类
 */

@Retention(AnnotationRetention.RUNTIME)
annotation class BuryPoint(
    val pageName: String = "",
    val pageChannel: String = ""
)

data class BuryPointInfo(
    var pageName: String = "",
    var pageChannel: String = "",
) {

    companion object {

        fun getPageBuryPoint(mClass: Class<*>): BuryPointInfo? {
            return mClass.getAnnotation(BuryPoint::class.java)?.run {
                BuryPointInfo(pageName, pageChannel)
            }
        }
    }
}

data class PageParamsBean(
    var source: String? = null,
    val trackInfo: BuryPointInfo? = null,
)

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
