package com.chinshry.base.util

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
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
    /** 可以在除了登录绑定认证以外的流程弹出 **/
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
    JUMP(WindowType.JUMP, 999),
    ACCOUNT(WindowType.HIGH, 99),
    UPDATE(WindowType.LOW, 98),
    DEVICE_CHECK(WindowType.MIDDLE, 97),
    PERMISSION_HINT(WindowType.LOW, 96),
    CIPHER(WindowType.MIDDLE, 95),
    CARING_CHOOSE(WindowType.LOW, 94),
    QUICK_LOGIN(WindowType.LOW, 93),
    HIGH_POPUP(WindowType.MIDDLE, 10),
    LOW_POPUP(WindowType.LOW, 9),
    INSIDE_PUSH(WindowType.MIDDLE, 1),
}

/** 页面注解 标志其是否为绑定认证流程相关页面 **/
@Retention(AnnotationRetention.RUNTIME)
annotation class AccountPage(val isAccountPage: Boolean = false)

object WindowManagerList {

    /** 窗口展示延时 = **/
    private const val WINDOW_SHOW_DELAY: Long = 500

    /** 不展示WindowType.MIDDLE级别窗口的activity列表 **/
    private val disableWindowShowActivityList = listOf(
        ""
    )

    private var windowList: MutableList<MyWindowList>? = mutableListOf()

    private var windowShowJob: Job? = null

    var isDialogShowing: Boolean = false

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
     * @param function Function0<Unit> 执行方法
     */
    fun addWindow(
        windowConfig: WindowConfig,
        function: (() -> Unit),
    ) {
        LogUtils.d("csTest windowConfig $windowConfig")
        windowList ?: return

        // 将窗口加入列表
        windowList?.add(MyWindowList(type = windowConfig.type, priority = windowConfig.priority, function = function))

        // 排序
        windowList?.sortByDescending { it.priority }

        // 此次为新窗口入空队 展示窗口
        if (windowList?.size == 1) {
            showWindow()
        }
    }

    /**
     * 准备执行任务 使只有一个任务在执行
     */
    fun showWindow() {
        windowList ?: return

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
     * 延时[WINDOW_SHOW_DELAY]后 遍历展示一个窗口
     */
    private fun windowShowJob() = MainScope().launch {
        LogUtils.d("csTest windowShowJob")

        delay(WINDOW_SHOW_DELAY)

        LogUtils.d("csTest windowShowJob delay over isDialogShowing = $isDialogShowing windowList = ${windowList.toString()}")

        if (isDialogShowing) return@launch

        if (windowList?.size ?: 0 == 0) return@launch

        windowList?.forEach { window ->
            // 是否可展示
            if (canShowWindow(window.type)) {
                // 执行窗口展示
                doWindowShow(window)
                return@launch
            }
        }
    }

    /**
     * 展示窗口
     * @param window MyWindowList 需要展示的窗口
     */
    private fun doWindowShow(window: MyWindowList) {
        LogUtils.d("csTest window $window ")

        windowList ?: return

        // 移出列表 并执行窗口展示
        windowList?.remove(window)
        window.function()

        // 本次非跳转页面 置弹窗显示标志为true
        if (window.type != WindowType.JUMP) {
            isDialogShowing = true
        }
    }

    /**
     * 窗口是否可展示
     * @param type WindowType 窗口类型
     * @return Boolean 当前是否可展示
     */
    private fun canShowWindow(type: WindowType): Boolean {
        // 当前activity名
        val currentActivity = ActivityUtils.getTopActivity().javaClass
        return when(type) {
            WindowType.LOW -> {
                currentActivity.name == "com.chinshry.application.MainActivity"
            }
            WindowType.MIDDLE -> {
                /** 带[AccountPage]注解为true的activity **/
                val isAccountPage = currentActivity.getAnnotation(AccountPage::class.java)?.isAccountPage ?: false
                /** 阿里活体sdk里的页面 **/
                val isFaceAuthPage = currentActivity.name.contains("com.alibaba.security")
                !(disableWindowShowActivityList.contains(currentActivity.name) || isAccountPage || isFaceAuthPage)
            }
            WindowType.HIGH, WindowType.JUMP -> {
                true
            }
        }
    }
}