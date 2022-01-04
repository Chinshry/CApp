package com.chinshry.base.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chinshry.base.util.getClickBuryPoint
import com.chinshry.base.util.logBuryPoint
import com.example.base.R


const val CLICK_DELAY = 200L

/**
 * get set
 * 给view添加一个上次触发时间的属性（用来屏蔽连击操作）
 */
private var <T : View>T.triggerLastTime: Long
    get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
    set(value) {
        setTag(R.id.triggerLastTimeKey, value)
    }

/**
 * get set
 * 给view添加一个延迟的属性（用来屏蔽连击操作）
 */
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
    set(value) {
        setTag(R.id.triggerDelayKey, value)
    }

/**
 * 判断时间是否满足再次点击的要求（控制点击）
 */
private fun <T : View> T.clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        clickable = true
    }
    triggerLastTime = currentClickTime
    return clickable
}

/***
 * 带延迟过滤点击事件的 View 扩展
 * @param delay Long 延迟时间，默认200毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */

fun <T : View> T.clickFun(text: String, delay: Long, block: (T) -> Unit) {
    triggerDelay = delay
    val viewBuryPoint = getClickBuryPoint(text)
    setOnClickListener {
        if (clickEnable()) {
            logBuryPoint(viewBuryPoint)
            block(this)
        }
    }
}

fun <T : View> T.clickWithTrigger(
    text: String = "",
    delay: Long = CLICK_DELAY,
    block: (T) -> Unit
) {
    clickFun(text, delay, block)
}

fun <T : TextView> T.clickWithTrigger(
    delay: Long = CLICK_DELAY,
    block: (T) -> Unit
) {
    clickFun(this.text.toString(), delay, block)
}

fun <T : ImageView> T.clickWithTrigger(
    delay: Long = CLICK_DELAY,
    block: (T) -> Unit
) {
    clickFun(this.contentDescription.toString(), delay, block)
}