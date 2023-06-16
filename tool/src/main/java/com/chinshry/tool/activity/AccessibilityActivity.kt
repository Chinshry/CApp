package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.auto.core.AutoApi
import cn.vove7.auto.core.api.withId
import cn.vove7.auto.core.api.withText
import cn.vove7.auto.core.viewfinder.SF
import cn.vove7.auto.core.viewfinder.clickable
import cn.vove7.auto.core.viewfinder.containsText
import cn.vove7.auto.core.viewfinder.id
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.BuryPoint
import com.chinshry.base.bean.Router
import com.chinshry.base.util.dp
import com.chinshry.base.util.getGroupNoneItemView
import com.chinshry.base.util.getGroupSwitchItemView
import com.chinshry.tool.R
import com.chinshry.tool.databinding.ActivityListBaseBinding
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by chinshry on 2023/06/15.
 * Describe：无障碍页面
 */
@BuryPoint(pageName = "无障碍测试页")
@Route(path = Router.ACCESSIBILITY)
class AccessibilityActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityListBaseBinding.inflate(layoutInflater) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.customHeaderBar.setPageTitle(getString(R.string.page_accessibility))
        initView()
    }

    private fun initView() {
        QMUIGroupListView.newSection(this)
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "悬浮窗权限",
                    PermissionUtils.checkPermission(this)
                ) { _, _ ->
                    PermissionUtils.requestPermission(this, object : OnPermissionResult {
                        override fun permissionResult(isOpen: Boolean) {
                            ToastUtils.showShort("权限申请结果 $isOpen")
                        }
                    })
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupSwitchItemView(
                    "无障碍开关",
                    AccessibilityApi.isServiceEnable
                ) { _, _ ->
                    jumpAccessibilityServiceSettings(AccessibilityApi.BASE_SERVICE_CLS)
                },
                null
            )
            .addItemView(
                viewBinding.groupListView.getGroupNoneItemView("抢票")
            ) {
                buyDaMaiTicket("周五", "vip票", 2)
            }
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)
    }

    private fun jumpAccessibilityServiceSettings(
        cls: Class<*>,
        ctx: Context = AutoApi.appCtx
    ) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putComponent(AutoApi.appCtx.packageName, cls)
        ctx.startActivity(intent)
    }

    private fun Intent.putComponent(pkg: String, cls: Class<*>) {
        val cs = ComponentName(pkg, cls.name).flattenToString()
        val bundle = Bundle()
        bundle.putString(":settings:fragment_args_key", cs)
        putExtra(":settings:fragment_args_key", cs)
        putExtra(":settings:show_fragment_args", bundle)
    }

    private fun buyDaMaiTicket(session: String, grade: String, num: Int) = lifecycleScope.launch {
        ToastUtils.showShort("start task after 3s")
        delay(3000)
        ToastUtils.showShort("start task！")
        withText("立即购买").tryClick()
        // 选票页面
        SF.containsText(session).waitFor(10000)?.tryClick()
        SF.containsText(grade).waitFor(10000)?.tryClick()
        withText("数量").waitFor(10000)
        (1 until num).forEach { _ ->
            withId("img_jia").tryClick()
        }
        withText("确定").tryClick()

        // 确认订单页面
        withText("提交订单").waitFor(10000)
        (SF where id("checkbox") and clickable()).findAll().forEachIndexed { index, viewNode ->
            if (index < num) {
                viewNode.tryClick()
            }
        }
        withText("提交订单").waitFor(3000)?.tryClick()
    }
}