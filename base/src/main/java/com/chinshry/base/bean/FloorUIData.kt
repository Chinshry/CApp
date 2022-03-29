package com.chinshry.base.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by chinshry on 2022/01/23.
 * Describe：楼层data
 */
class FloorUIData(var entity: FloorData, override val itemType: Int) : MultiItemEntity {

    /**
     * styleType
     * @property type 中台配置的displayType styleType
     * @property value 布局itemType
     * @constructor
     */
    enum class FloorType(val type: String, val value: Int) {
        NOTICE(FLOOR_DISPLAY_TYPE_NOTICE, 0), // 公告栏
        GRID(FLOOR_DISPLAY_TYPE_GRID, 1), // 宫格
        CARD(FLOOR_DISPLAY_TYPE_CARD, 2), // 卡片
        BANNER(FLOOR_DISPLAY_TYPE_BANNER, 3), // banner
        QUOTE(FLOOR_DISPLAY_TYPE_QUOTE, 4), // 引用
        INSURANCE_PRODUCE(FLOOR_FLOOR_TYPE_INSURANCE_PRODUCE,101), // 保险-产品
        RECEPTION_INFO(FLOOR_FLOOR_TYPE_RECEPTION_INFO, 102), // 礼遇-头部
        RECEPTION_CUSTOM(FLOOR_FLOOR_TYPE_RECEPTION_CUSTOM, 103), // 礼遇-白色背景带阴影宫格
        RECEPTION_SERVICES(FLOOR_FLOOR_TYPE_RECEPTION_SERVICES ,104), // 礼遇-权益服务
        RECEPTION_TOPIC(FLOOR_FLOOR_TYPE_RECEPTION_TOPIC, 105), // 礼遇-话题PK
        RECEPTION_JOINED_TOPIC(FLOOR_FLOOR_TYPE_RECEPTION_TOPIC, 1051),
        RECEPTION_EXPIRED_TOPIC(FLOOR_FLOOR_TYPE_RECEPTION_TOPIC, 1052);
    }

    companion object {

        /************************************ 中台配置 ****************************************/

        // 楼层类型
        const val FLOOR_DISPLAY_TYPE_QUOTE = "quote"
        const val FLOOR_DISPLAY_TYPE_GRID = "grid"
        const val FLOOR_DISPLAY_TYPE_CARD = "card"
        const val FLOOR_DISPLAY_TYPE_BANNER = "banner"
        const val FLOOR_DISPLAY_TYPE_NOTICE = "notice"

        // quote引用类型楼层布局类型
        const val FLOOR_FLOOR_TYPE_INSURANCE_PRODUCE = "product"
        const val FLOOR_FLOOR_TYPE_RECEPTION_INFO = "info"
        const val FLOOR_FLOOR_TYPE_RECEPTION_CUSTOM = "custom"
        const val FLOOR_FLOOR_TYPE_RECEPTION_SERVICES = "service"
        const val FLOOR_FLOOR_TYPE_RECEPTION_TOPIC = "topic"

        // grid楼层类型宫格布局类型
        const val GRID_PLATE_TYPE_BIG = "big"

        /************************************ 自定义索引 ****************************************/

        // 模块
        const val MODEL_GLOBAL = -1
        const val MODEL_ANNIVERSARY = 0
        const val MODEL_INSURANCE = 1
        const val MODEL_RECEPTION = 2
        const val MODEL_MINE = 3
        const val MODEL_CARING = 4
        const val MODEL_CARING_MORE_SERVICE = 5

        fun getFloorType(type: String?) : Int? {
            FloorType.values().forEach {
                if (it.type == type) {
                    return it.value
                }
            }
            return null
        }

    }

}


