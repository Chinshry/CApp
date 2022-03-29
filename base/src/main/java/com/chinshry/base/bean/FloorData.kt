package com.chinshry.base.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by chinshry on 2022/03/21.
 */
class FloorData(
    val id: String? = null,
    val appVersion: String? = null,
    var displayType: String? = null,  // 楼层类型
    var imageUrl: String?=null, //楼层背景图
    var styleType: String? = null, // 楼层类型
    var gridPlateType: String? = null, // grid楼层元素布局类型
    var gridColumnsNum: Float? = null,
    var gridRowNum: Float? = null,

    val titleFlag: Boolean? = null,
    val title: String? = null,
    val titleColor: String? = null,
    val hasMore: Boolean? = null,
    val hasMoreLink: String? = null,
    val hasMoreText: String? = null,
    val hasMoreTextColor: String? = null,

    var marginTop: Int? = null,
    var marginBottom: Int? = null,
    var dividerH: Float? = null, // 楼层上方分割线高度
    var dividerColor: String? = null, // 楼层上方分割线颜色
    var dividerMarginLeft: Float? = null, // 楼层上方分割线左边距
    var dividerMarginRight: Float? = null, // 楼层上方分割线右边距
    var itemDividerW: Float? = null, // 竖向分割线高度
    var itemDividerH: Float? = null, // 横向分割线高度

    val elementAttributes: List<ElementAttribute>? = null,
): MultiItemEntity {
    override val itemType: Int
        get() {
            return FloorUIData.getFloorType(displayType) ?: -1
        }
}

data class ElementAttribute(
    var occupiesColumns: Int? = null,
    var occupiesRows: Int? = null,
    val elementVisible: Boolean? = null,
    val elementPicture: String? = null,
    var elementBackgroundColor: String? = null,
    val elementTitle: String? = null,
    val elementTitleColor: String? = null,
    var buryPoint: String? = null, //埋点使用

    // 红点角标
    val angleMark: String? = null, // 角标1图片地址
    val angleMarkOne: String? = null, // 角标2图片地址
    val angleMarkId: String? = null, // 角标1 id
    val angleMarkOneId: String? = null, // 角标2 id
    val eliminationLogic: Long? = null, // 消除逻辑 -1：点击后永久不出现 0：强运营红点 >0: 点击后N个自然日后出现
)

// 浮窗
data class FloatingWindowData(
    val id: String? = null, // 浮窗id
    val appVersion: String? = null, // 版本号控制
    val permissions: String? = null, // 权限控制
    val buryPoint: String? = null, // 埋点使用
    val closed: Boolean? = null,//是否可关闭  1-是；0-否  暂不使用

    val imageUrl: String? = null, // 浮窗图片链接
    val jumpLink: String? = null, // 浮窗跳转链接
    val delayShowTime: Int? = null, // 收起后间隔多久展开 秒
)

