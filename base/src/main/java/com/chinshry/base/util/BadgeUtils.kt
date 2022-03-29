package com.chinshry.base.util

import android.view.View
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.TimeUtils
import com.chinshry.base.bean.ElementAttribute
import com.chinshry.base.db.BadgeDataModel
import com.chinshry.base.db.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by chinshry on 2021/12/8.
 * Describe：角标组件处理
 */
enum class BadgeType {
    FLOOR, TAB
}

object BadgeUtils {
    /**
     * 是否展示红点
     * @param type BadgeType 类型
     * @param data ElementAttribute? 数据
     * @return Boolean
     */
    fun showBadge(
        type: BadgeType,
        data: ElementAttribute?,
    ): Boolean {
        data?.apply {
            // ID 或 图片链接为空 不展示
            if (angleMarkId.isNullOrBlank() || angleMark.isNullOrBlank()) return false
            // eliminationLogic为空或者 == 0 强运营红点 展示
            if (eliminationLogic == null || eliminationLogic == 0L) return true

            // 取该ID的缓存数据
            // 缓存为空 是新的角标 展示
            val lastBadgeData = getBadgeDb(angleMarkId, type.name) ?: return true

            // eliminationLogic = -1 即永久不出现 不展示
            if (eliminationLogic == -1L) return false
            // 此时距上次点击时间负差值
            val timeSpanNow = -TimeUtils.getTimeSpanByNow(lastBadgeData.clickHideTime, TimeConstants.DAY)
            // 大于等于给定差值 展示； 反之 不展示
            return timeSpanNow >= eliminationLogic ?: 0L
        }
        return false
    }

    /**
     * 红点点击处理
     * @param type BadgeType 类型
     * @param badgeView Boolean? 红点view
     * @param data ElementAttribute? 数据
     */
    fun handleBadgeClick(
        type: BadgeType,
        badgeView: View?,
        data: ElementAttribute?,
    ) {
        badgeView ?: return

        data?.apply {
            // 当前红点非显示中
            if (badgeView.visibility != View.VISIBLE) return
            // ID或图片链接为空视为无效红点
            if (angleMarkId.isNullOrBlank() || angleMark.isNullOrBlank()) return
            // eliminationLogic为空或者 == 0 强运营红点 始终显示 不做处理
            if (eliminationLogic == null || eliminationLogic == 0L) return

            // 隐藏红点
            badgeView.visibility = View.GONE

            // 保存该红点ID点击时间
            val badgeData = BadgeDataModel(
                id = angleMarkId,
                type = type.name,
                eliminationLogic = eliminationLogic,
                clickHideTime = DateTimeUtils.getTodayMills()
            )

            MainScope().launch(Dispatchers.Main) {
                // 保存该红点ID点击时间
                saveBadgeDb(badgeData)
            }
        }
    }

    // TODO 清理过期红点数据
    // fun clearExpireBadge() {}

    private suspend fun saveBadgeDb(data: BadgeDataModel) {
        withContext(Dispatchers.IO) {
            val context = ActivityUtils.getTopActivity()
            DatabaseManager.getBadgeDao(context).insert(data)
        }
    }

    private fun getBadgeDb(id: String?, type: String): BadgeDataModel? {
        val context = ActivityUtils.getTopActivity() ?: return null
        id?.let {
            return DatabaseManager.getBadgeDao(context).get(it, type)
        } ?: let {
            return null
        }
    }

}