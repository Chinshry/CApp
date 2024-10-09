package com.chinshry.accessibility.task

import cn.vove7.auto.core.api.containsText
import cn.vove7.auto.core.api.withId
import cn.vove7.auto.core.api.withText
import cn.vove7.auto.core.viewfinder.SF
import cn.vove7.auto.core.viewfinder.clickable
import cn.vove7.auto.core.viewfinder.id
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.delay


class MaiMaiTicket(
    private val session: String,
    private val grade: String,
    private val num: Int
) : BaseTask() {
    override val name = "麦麦"

    override suspend fun onStartTask() {
        ToastUtils.showShort("start task after 3s")
        delay(3_000)
        ToastUtils.showShort("start task！")
        withText("立即购买").waitAndClick()

        // 选票页面
        containsText(session).waitAndClick(10_000)
        containsText(grade).waitAndClick()
        withText("数量").waitAndClick()
        repeat(num) { withId("img_jia").waitAndClick() }
        withText("确定").waitAndClick()

        // 确认订单页面
        withText("提交订单").waitAndClick(10_000)
        (SF where id("checkbox") and clickable()).findAll().forEachIndexed { index, viewNode ->
            if (index < num) {
                viewNode.tryClick()
            }
        }
        withText("提交订单").waitAndClick()
    }
}