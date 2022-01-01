package com.chinshry.base.util

import androidx.annotation.IntDef
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

/**
 * Created by chinshry on 2021/12/23.
 * Describe：弹窗管理
 */
data class MyWindow(
    @WindowLevel val level: Int, // window等级 数字越大 优先级更高
    val priority: Int, // window优先级 同level情况下 priority数字越大 优先级更高
    val function: (() -> Unit),
)

/**
 * 弹窗优先级
 */
@IntDef(value = [ShapeType.RECTANGLE, ShapeType.OVAL])
@Retention(AnnotationRetention.SOURCE)
annotation class WindowLevel {
    companion object {
        /**
         * 仅能在首页弹出 eg: 低优先级运营弹窗
         */
        var LOW = 0
        /**
         * 可以在除了登录以外的流程弹出 eg: 高优先级运营弹窗
         */
        var MIDDLE = 1
        /**
         * 在任意位置弹出 eg: 升级弹窗
         */
        var HIGH = 2
        /**
         * 跳转
         */
        var JUMP = 10
    }
}

object WindowManager {

    private const val CHECK_WINDOW_INTERVAL: Long = 1 * 1000

    /**
     * 不展示WindowLevel.MIDDLE级别窗口的activity列表
     */
    private val disableWindowShowActivityList = listOf(
        ""
    )

    /**
     * 优先级比较器
     */
    private val comparator = Comparator<MyWindow> { o1, o2 ->
        if (o2.level == o1.level) {
            (o2.priority).compareTo(o1.priority)
        } else {
            (o2.level).compareTo(o1.level)
        }
    }

    private val windowQueue = PriorityBlockingQueue(10, comparator)

    /**
     * 添加窗口
     * @param level WindowLevel 窗口级别
     * @param priority Int 窗口优先级
     * @param function Function0<Unit> 窗口弹出方法
     */
    fun addWindow(
        @WindowLevel level: Int,
        priority: Int = 0,
        function: (() -> Unit),
    ) {
        windowQueue.put(MyWindow(level = level, priority = priority, function = function))

        // 队列原本为空 此次为新窗口入队 开启循环
        if (windowQueue.size == 1) {
            startWindowQueueIterator()
        }
    }

    /**
     * 窗口展示
     */
    private fun showWindow() {
        // 取队列头
        windowQueue.peek()?.let { window ->
            // 是否可展示
            if (canShowWindow(window.level)) {
                // 出队列 并执行窗口展示
                windowQueue.poll()
                window.function()
            }
        }
    }

    /**
     * 窗口队列展示循环
     */
    private fun startWindowQueueIterator() {
        MainScope().launch(Dispatchers.Main) {
            // 等待弹窗添加完毕开始循环
            // delay(500)

            // 当队列有窗口时 循环 直至窗口全部弹出
            while (isActive && windowQueue.size > 0) {
                showWindow()
                delay(CHECK_WINDOW_INTERVAL)
            }

            // 循环完成取消任务
            cancel()
        }
    }

    /**
     * 窗口是否可展示
     * @param level WindowLevel 窗口级别
     * @return Boolean 当前是否可展示
     */
    private fun canShowWindow(@WindowLevel level: Int): Boolean {
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
            else -> {
                false
            }
        }
    }
}