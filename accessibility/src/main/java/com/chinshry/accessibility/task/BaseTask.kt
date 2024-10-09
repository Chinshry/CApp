package com.chinshry.accessibility.task

import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.auto.core.viewfinder.ConditionGroup
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.accessibility.bean.NodeNotFoundException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

abstract class BaseTask {
    companion object {
        val taskCompletionMap: MutableMap<String, Job> = ConcurrentHashMap()
    }

    lateinit var job: Job
    abstract val name: String
    abstract suspend fun onStartTask()

    private val logTAG = this.javaClass.simpleName

    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            ToastUtils.showShort("执行失败： ${throwable.message ?: "$throwable"}")
            LogUtils.e(logTAG, "run ERROR.", throwable)
        }
    }

    fun start() {
        LogUtils.d(logTAG, "start")
        runCatching {
            if (AccessibilityApi.isServiceEnable) {
                job = MainScope().launch(exceptionHandler) { onStartTask() }
                job.invokeOnCompletion {
                    LogUtils.d(logTAG, "Complete")
                }
                taskCompletionMap[name] = job
            } else {
                ToastUtils.showShort("请先开启无障碍服务")
            }
        }.onFailure {
            LogUtils.e(logTAG, "start ERROR.", it)
        }
    }

    suspend fun ConditionGroup.waitAndClick(waitTime: Long = 3_000) {
        waitFor(waitTime)?.tryClick()?.takeIf { it } ?: throw NodeNotFoundException(this.toString())
    }
}