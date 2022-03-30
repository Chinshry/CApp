package com.chinshry.base.bean

import java.util.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe：数据类
 */
enum class Module(
    val model: Int,
    val moduleName: String
) {
    Base(0, "基础"),
    Home(1, "首页"),
    Tool(2, "工具");

    companion object {
        fun getModuleByModel(model: Int?): Module? {
            values().forEach {
                if (it.model == model) return it
            }
            return null
        }

        fun getNameByModel(model: Int?): String? {
            values().forEach {
                if (it.model == model) return it.moduleName
            }
            return null
        }

        fun getNameByModelName(name: String?): String {
            values().forEach {
                if (it.name.lowercase(Locale.getDefault()) == name) return it.moduleName
            }
            return ""
        }

    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class BuryPoint(val pageName: String = "")

data class BuryPointInfo(
    var pageName: String = "",
    var pageChannel: String = "",
    var viewName: String = "",
) {
    fun toLogString(): String {
        var str = ""
        if (pageChannel.isNotBlank()) str += "pageChannel = $pageChannel"
        if (pageName.isNotBlank()) str += "  pageName = $pageName"
        if (viewName.isNotBlank()) {
            str += "  viewName = $viewName"
            str = "View BuryPoint | $str"
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
    var normalTitle: String? = null,
    var selectTitle: String? = null,
    var normalIcon: Int? = null,
    var selectIcon: Int? = null,
    var normalTextColor: Int? = null,
    var selectTextColor: Int? = null,
    var badge: Int? = null
)
