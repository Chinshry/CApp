package com.chinshry.base.util

import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.coroutines.*
import java.util.concurrent.PriorityBlockingQueue

/**
 * Created by chinshry on 2021/12/23.
 * Describe：弹窗管理
 */
data class MyWindow(
    /** window等级 数字越大 优先级更高 **/
    val level: WindowLevel,
    /** window优先级 同level情况下 priority数字越大 优先级更高 **/
    val priority: Int,
    /** 任务 **/
    val function: (() -> Unit),
)

/** window等级 **/
enum class WindowLevel {
    /** 仅能在首页弹出 eg: 低优先级运营弹窗 **/
    LOW,
    /** 可以在除了登录以外的流程弹出 eg: 高优先级运营弹窗 **/
    MIDDLE,
    /** 在任意位置弹出 eg: 升级弹窗 **/
    HIGH,
    /** 跳转 **/
    JUMP
}

object WindowManager {

    private const val HIGH_WINDOW_SHOW_DELAY: Long = 2 * 1000
    private const val CHECK_WINDOW_INTERVAL: Long = 1 * 1000
    private const val WINDOW_SHOW_DELAY: Long = 500

    /** 不展示WindowLevel.MIDDLE级别窗口的activity列表 **/
    private val disableWindowShowActivityList = listOf(
        ""
    )

    /** 队列优先级比较器 **/
    private val comparator = Comparator<MyWindow> { o1, o2 ->
        if (o2.level == o1.level) {
            (o2.priority).compareTo(o1.priority)
        } else {
            (o2.level).compareTo(o1.level)
        }
    }

    private val windowQueue = PriorityBlockingQueue(10, comparator)

    /**
     * 队列中是否有JUMP任务
     */
    private fun hasJumpInQueue(): Boolean {
        return windowQueue.peek()?.level == WindowLevel.JUMP
    }

    /**
     * 添加窗口
     * @param level WindowLevel 窗口级别
     * @param priority Int 窗口优先级
     * @param function Function0<Unit> 执行方法
     */
    fun addWindow(
        level: WindowLevel,
        priority: Int = 0,
        function: (() -> Unit),
    ) {
        // 高优先级弹窗 不入栈
        if (level == WindowLevel.HIGH){
            // 若栈中有JUMP类 延迟弹出
            Handler(Looper.getMainLooper()).postDelayed({
                function()
            }, if (hasJumpInQueue()) HIGH_WINDOW_SHOW_DELAY else 0)
            return
        }

        // 将窗口加入队列
        windowQueue.put(MyWindow(level = level, priority = priority, function = function))

        // 此次为新窗口入空队 展示窗口
        if (windowQueue.size == 1) {
            showWindow()
        }
    }

    /**
     * 准备进行窗口展示
     */
    fun showWindow() {
        // 延迟一段时间再取队列
        // 等待初始化完成 或 上个弹窗结束后的跳转完成
        Handler(Looper.getMainLooper()).postDelayed({
            // 取出队列头
            windowQueue.peek()?.let { window ->
                // 是否可展示
                if (canShowWindow(window.level)) {
                    // 执行窗口展示
                    doWindowShow(window)
                } else {
                    // 暂时不能展示 循环任务该窗口
                    startWindowIterator()
                }
            }
        }, WINDOW_SHOW_DELAY)
    }

    /**
     * 开始循环任务 直至将该窗口弹出任务停止
     */
    private fun startWindowIterator() {
        MainScope().launch(Dispatchers.Main) {
            while (isActive) {
                val window = windowQueue.peek() ?: break

                // 是否可展示
                if (canShowWindow(window.level)) {
                    doWindowShow(window)
                    break
                }

                delay(CHECK_WINDOW_INTERVAL)
            }
        }
    }

    /**
     * 展示窗口
     * @param window MyWindow 需要循环的窗口
     */
    private fun doWindowShow(window: MyWindow) {
        // 出队列 并执行窗口展示
        windowQueue.poll()
        window.function()

        // 本次为跳转页面 继续展示弹窗
        if (window.level == WindowLevel.JUMP) {
            showWindow()
        }
    }

    /**
     * 窗口是否可展示
     * @param level WindowLevel 窗口级别
     * @return Boolean 当前是否可展示
     */
    private fun canShowWindow(level: WindowLevel): Boolean {
        // 当前activity名
        val currentActivityName = ActivityUtils.getTopActivity().javaClass.name
        return when(level) {
            WindowLevel.LOW -> {
                currentActivityName == "com.chinshry.application.MainActivity"
            }
            WindowLevel.MIDDLE -> {
                !(disableWindowShowActivityList.contains(currentActivityName))
            }
            WindowLevel.HIGH, WindowLevel.JUMP -> {
                true
            }
        }
    }
}