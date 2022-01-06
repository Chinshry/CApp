package com.chinshry.base.util

import com.blankj.utilcode.util.ActivityUtils
import kotlinx.coroutines.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe：弹窗管理
 */
data class MyWindowList(
    /** window类型 **/
    val type: WindowType,
    /** window优先级 priority数字越大 优先级更高 **/
    val priority: Int,
    /** 任务 **/
    val function: (() -> Unit),
)

/** window类型 **/
enum class WindowType {
    /** 仅能在首页弹出 **/
    LOW,
    /** 可以在除了登录以外的流程弹出 **/
    MIDDLE,
    /** 在任意位置弹出 **/
    HIGH,
    /** 跳转 **/
    JUMP
}

/** 弹窗枚举 后续迭代为可配置列表 **/
enum class WindowConfig(
    val type: WindowType,
    val priority: Int,
) {
    JUMP(WindowType.JUMP, 100),
    UPDATE(WindowType.LOW, 99),
    DEVICE_CHECK(WindowType.MIDDLE, 98),
    PERMISSION_HINT(WindowType.LOW, 97),
    CIPHER(WindowType.MIDDLE, 96),
    CARING_CHOOSE(WindowType.LOW, 95),
    QUICK_LOGIN(WindowType.HIGH, 94),
    HIGH_POPUP(WindowType.MIDDLE, 10),
    LOW_POPUP(WindowType.LOW, 9),
    INSIDE_PUSH(WindowType.MIDDLE, 1),
}

object WindowManagerList {
    const val WINDOW_SHOW_SHORT_DELAY: Long = 500
    const val WINDOW_SHOW_NORMAL_DELAY: Long = 1000
    const val WINDOW_SHOW_LONG_DELAY: Long = 3000
    private const val CHECK_WINDOW_INTERVAL: Long = 1000

    /** 不展示WindowType.MIDDLE级别窗口的activity列表 **/
    private val disableWindowShowActivityList = listOf(
        ""
    )

    private var windowList: MutableList<MyWindowList>? = mutableListOf()

    private var windowShowJob: Job? = null

    /** 进入首页前的历史站内推送数据 只保留最新的 **/
    // var historyInsidePushData : InsidePushData? = null

    /**
     * 强更弹窗展示 清空列表 并且不允许再添加
     */
    fun clear() {
        windowList?.clear()
        windowList = null
    }

    /**
     * 添加窗口
     * @param windowConfig WindowConfig
     * @param delayTime Long 延时展示窗口 无接口 [WINDOW_SHOW_LONG_DELAY] 有接口 [WINDOW_SHOW_NORMAL_DELAY]
     * @param function Function0<Unit> 执行方法
     */
    fun addWindow(
        windowConfig: WindowConfig,
        delayTime: Long = WINDOW_SHOW_NORMAL_DELAY,
        function: (() -> Unit),
    ) {
        windowList ?: return

        // 将窗口加入列表
        windowList?.add(MyWindowList(type = windowConfig.type, priority = windowConfig.priority, function = function))

        // 排序
        windowList?.sortByDescending { it.priority }

        // 此次为新窗口入空队 展示窗口
        if (windowList?.size == 1) {
            showWindow(delayTime)
        }
    }

    /**
     * 准备执行任务 延迟一段时间
     * 等待初始化完成 [WINDOW_SHOW_NORMAL_DELAY]
     * 等待上个弹窗结束后的跳转完成 JUMP到页面后展示 [WINDOW_SHOW_SHORT_DELAY]
     * @param delayTime Long
     */
    fun showWindow(
        delayTime: Long = WINDOW_SHOW_SHORT_DELAY
    ) = MainScope().launch {
        windowList ?: return@launch

        delay(delayTime)

        windowShowJob?.apply {
            if (isCompleted) {
                windowShowJob = windowShowJob()
            }
            if (!isActive) {
                start()
            }
        } ?: let {
            windowShowJob = windowShowJob()
        }
    }

    /**
     * 循环遍历列表当前是否有窗口可展示 每次循环间隔[CHECK_WINDOW_INTERVAL]
     */
    private fun windowShowJob() = MainScope().launch {
        if (windowList?.size ?: 0 == 0) return@launch

        while (isActive) {
            windowList?.forEach { window ->
                // 是否可展示
                if (canShowWindow(window.type)) {
                    // 执行窗口展示
                    doWindowShow(window)
                    return@launch
                }
            }

            delay(CHECK_WINDOW_INTERVAL)
        }
    }

    /**
     * 展示窗口
     * @param window MyWindowList 需要循环的窗口
     */
    private fun doWindowShow(window: MyWindowList) {
        windowList ?: return

        // 移出列表 并执行窗口展示
        windowList?.remove(window)
        window.function()

        // 本次为跳转页面 继续展示弹窗
        if (window.type == WindowType.JUMP) {
            showWindow()
        }
    }

    /**
     * 窗口是否可展示
     * @param type WindowType 窗口级别
     * @return Boolean 当前是否可展示
     */
    private fun canShowWindow(type: WindowType): Boolean {
        // 当前activity名
        val currentActivityName = ActivityUtils.getTopActivity().javaClass.name
        return when(type) {
            WindowType.LOW -> {
                currentActivityName == "com.chinshry.application.MainActivity"
            }
            WindowType.MIDDLE -> {
                !(disableWindowShowActivityList.contains(currentActivityName))
            }
            WindowType.HIGH, WindowType.JUMP -> {
                true
            }
        }
    }
}