package com.chinshry.base.bean

class FloorData(
    var gridColumnsNum: Float? = null,
    var gridRowNum: Float? = null,
    var itemDividerW: Float? = null, // 竖向分割线高度
    var itemDividerH: Float? = null, // 横向分割线高度
    var scrollOffset: Int? = null, // 每次滚动offset
    val elementAttributes: List<ElementAttribute>? = null,
)

data class ElementAttribute(
    val elementPicture: String? = null,
    val elementTitle: String? = null,
    var occupiesColumns: Int? = null,
    var occupiesRows: Int? = null,
    val elementVisible: Boolean = true
)
