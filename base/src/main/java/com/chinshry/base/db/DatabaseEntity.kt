package com.chinshry.base.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by chinshry on 2022/03/21.
 */
@Entity(tableName = "Badge")
data class BadgeDataModel(
    @PrimaryKey
    var id: String,
    var type: String,
    val eliminationLogic: Long, // 消除逻辑 -1：点击后永久不出现  >0: 点击后N个自然日后出现
    var clickHideTime: Long // 开始隐藏的时间
)