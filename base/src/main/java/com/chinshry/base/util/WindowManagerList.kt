package com.chinshry.base.util

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    HIGH
}

/** 弹窗枚举 后续迭代为可配置列表 **/
enum class WindowConfig(
    val type: WindowType,
    val priority: Int,
) {
    ACCOUNT(WindowType.HIGH, 99),
    UPDATE(WindowType.LOW, 98),
    DEVICE_CHECK(WindowType.MIDDLE, 97),
    PERMISSION_HINT(WindowType.LOW, 96),
    CIPHER(WindowType.MIDDLE, 95),
    CARING_CHOOSE(WindowType.LOW, 94),
    QUICK_LOGIN(WindowType.LOW, 93),
    HIGH_POPUP(WindowType.MIDDLE, 10),
    LOW_POPUP(WindowType.LOW, 9),
    INSIDE_PUSH(WindowType.MIDDLE, 1);

    override fun toString(): String {
        return "$name type = $type priority = $priority"
    }
}

/** 页面注解 标志其是否为绑定认证流程相关页面 **/
@Retention(AnnotationRetention.RUNTIME)
annotation class MyPage(val isAccountPage: Boolean = false)

object WindowManagerList {
    const val TAG = "MyWindowManagerList"

    /** 窗口展示延时 **/
    private const val WINDOW_SHOW_LONG_DELAY: Long = 3000
    private const val WINDOW_SHOW_DELAY: Long = 500
    const val WINDOW_SHOW_RESUMED_DELAY: Long = 1000

    /** 不展示WindowType.MIDDLE级别窗口的activity列表 **/
    private val disableWindowShowActivityList = listOf(
        ""
    )

    private var windowList: MutableList<MyWindowList>? = mutableListOf()

    private var windowShowJob: Job? = null

    var isDialogShowing: Boolean = false

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
        LogUtils.dTag(TAG, "$windowConfig")

        windowList ?: return

        // 将窗口加入列表
        windowList?.add(MyWindowList(type = windowConfig.type, priority = windowConfig.priority, function = function))

        // 排序
        windowList?.sortByDescending { it.priority }

        val delay =
            if (windowList?.size == 1) {
                // 此次为新窗口入空队 添加延时
                when (windowConfig) {
                    WindowConfig.DEVICE_CHECK, WindowConfig.PERMISSION_HINT -> WINDOW_SHOW_LONG_DELAY
                    else -> WINDOW_SHOW_DELAY
                }
            } else 0
        showWindow(delay)
    }

    /**
     * 准备执行任务 使只有一个任务在执行
     */
    fun showWindow(
        delayTime: Long = WINDOW_SHOW_DELAY
    ) {
        if (windowList.isNullOrEmpty()) return

        // 未初始化的任务 或 初始化后已完成的任务
        if (windowShowJob?.isCompleted != false) {
            windowShowJob = windowShowJob(delayTime)
            windowShowJob?.start()
        }
    }

    /**
     * 延时[WINDOW_SHOW_DELAY]后 遍历展示一个窗口
     */
    private fun windowShowJob(
        delayTime: Long
    ) = MainScope().launch(start = CoroutineStart.LAZY) {
        LogUtils.dTag(TAG, "windowShowJob ready delay = $delayTime")

        delay(delayTime)

        LogUtils.dTag(
            TAG, "windowShowJob delay over \n " +
                    "isDialogShowing = $isDialogShowing \n windowList = ${windowList.toString()}")

        // 无弹窗展示中 任务列表不为空
        // 防止延时过程中应用到后台 页面跳转会失败
        if (isDialogShowing || windowList.isNullOrEmpty() || !AppUtils.isAppForeground()) return@launch

        windowList?.toMutableList()?.forEach { window ->
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
        LogUtils.dTag(TAG, "doWindowShow $window ")

        // 移出列表成功
        if (windowList?.remove(window) == true) {
            // 执行任务
            window.function()
        }
    }

    /**
     * 供function内调用 若弹窗未成功展示 取列表头任务执行
     */
    fun doNextWindowShow() {
        val window = windowList?.getOrNull(0) ?: return
        doWindowShow(window)
    }

    /**
     * 窗口是否可展示
     * @param type WindowType 窗口类型
     * @return Boolean 当前是否可展示
     */
    private fun canShowWindow(type: WindowType): Boolean {
        // 当前activity
        val currentActivity = ContextHelper.getActivity().javaClass

        return when (type) {
            WindowType.LOW -> {
                currentActivity.simpleName == "MainActivity"
            }
            WindowType.MIDDLE -> {
                /** 带[MyPage]注解isAccountPage为true的activity **/
                val isAccountPage = currentActivity?.getAnnotation(MyPage::class.java)?.isAccountPage ?: false
                !(disableWindowShowActivityList.contains(currentActivity?.name) || isAccountPage)
            }
            WindowType.HIGH -> {
                true
            }
        }
    }
}