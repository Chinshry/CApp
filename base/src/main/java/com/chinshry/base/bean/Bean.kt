package com.chinshry.base.bean

import androidx.fragment.app.Fragment
import java.util.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe：数据类
 */
enum class Module(
    val model: Int,
    val buryName: String
) {
    BASE(0, "基础"),
    HOME(1, "首页"),
    TOOL(2, "工具");

    companion object {

        fun getBuryNameByModel(model: Int?): String? {
            values().forEach {
                if (it.model == model) return it.buryName
            }
            return null
        }

        fun getBuryNameByModelName(name: String?): String {
            values().forEach {
                if (it.name.lowercase(Locale.getDefault()) == name) return it.buryName
            }
            return ""
        }

    }
}

data class FragmentBean(
    val fragment: Fragment,
    val model: Int
)

@Retention(AnnotationRetention.RUNTIME)
annotation class BuryPoint(val pageName: String = "")

data class BuryPointInfo(
    var pageName: String = "",
    var pageChannel: String = "",
    var buttonName: String = "",
) {
    fun toLogString(): String {
        var str = ""
        if (pageChannel.isNotBlank()) str += "pageChannel = $pageChannel"
        if (pageName.isNotBlank()) str += "  pageName = $pageName"
        if (buttonName.isNotBlank()) {
            str += "  buttonName = $buttonName"
            str = "BUTTON BuryPoint | $str"
        } else {
            str = "PAGE BuryPoint | $str"
        }
        return str
    }
}

data class PageParamsBean(
    var source: String? = null,
    val trackInfo: BuryPointInfo? = null,
)

data class TabBean(
    var model: Int? = null,
    var title: String? = null,
    var normalIcon: Int? = null,
    var selectIcon: Int? = null,
    var badge: Int? = null
)
