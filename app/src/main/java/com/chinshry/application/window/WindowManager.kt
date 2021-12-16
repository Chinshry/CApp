package com.chinshry.application.window

import android.app.Dialog
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.Postcard
import com.blankj.utilcode.util.ActivityUtils
import com.chinshry.application.MainActivity
import com.chinshry.application.tabView.TabActivity
import java.util.*

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.application
 * File Name:DialogManager
 * Describe：Application
 */
enum class WindowType {
    Dialog,
    View,
    Activity,
}
enum class WindowPriority {
    LOW, // 仅在首页弹出 eg: 低优先级运营弹窗
    MIDDLE,  // 在除了登录绑定认证以外的流程弹出 eg: 高优先级运营弹窗
    HIGH // 在任意位置弹出 eg: 升级弹窗
}

data class MyWindow(
    val data: Any? = null,
    val type: WindowType,
    val priority: WindowPriority,
)

object DialogManager {

    private val disableWindowShowActivityList = listOf<String>(
        TabActivity::class.java.name
    )

    private val comparator = Comparator<MyWindow> { o1, o2 -> (o2.priority).compareTo(o1.priority) }
    private val windowQueue = PriorityQueue(10, comparator)

    fun addWindow(
        window: MyWindow,
    ) {
        windowQueue.add(window)
    }

    fun showWindow() {
        windowQueue.peek()?.let { window ->
            if (canShowWindow(window.priority)) {
                windowQueue.poll()
            } else {
                return
            }

            when (window.type) {
                WindowType.Dialog -> {
                    val dialog = Dialog(ActivityUtils.getTopActivity())
                    dialog.setTitle(window.data as? String)
                    dialog.show()
                }
                WindowType.View -> {
                    val topActivity = ActivityUtils.getTopActivity()
                    val layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // topActivity?.addContentView(window, layoutParams)
                }
                WindowType.Activity -> {
                    val postcard = window.data as? Postcard
                    postcard?.navigation()
                }
            }


        }
    }

    private fun canShowWindow(priority: WindowPriority): Boolean {
        return when(priority) {
            WindowPriority.LOW -> {
                ActivityUtils.getTopActivity() == MainActivity()
            }
            WindowPriority.MIDDLE -> {
                !(disableWindowShowActivityList.contains(ActivityUtils.getTopActivity().localClassName))
            }
            WindowPriority.HIGH -> {
                true
            }
        }
    }
}