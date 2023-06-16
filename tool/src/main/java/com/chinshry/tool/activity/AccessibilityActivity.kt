package com.chinshry.tool.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.lifecycleScope
import cn.vove7.andro_accessibility_api.AccessibilityApi
import cn.vove7.auto.core.api.withId
import cn.vove7.auto.core.api.withText
import cn.vove7.auto.core.utils.jumpAccessibilityServiceSettings
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
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils
import com.lzf.easyfloat.utils.DisplayUtils
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
    private var floatBuilder: EasyFloat.Builder? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.customHeaderBar.setPageTitle(getString(R.string.page_accessibility))
        initView()
    }

    private fun initView() {
        QMUIGroupListView.newSection(this)
            .setTitle("开关")
            .addItemView(
                getGroupSwitchItemView(
                    "无障碍开关",
                    AccessibilityApi.isServiceEnable
                ) { _, _ ->
                    jumpAccessibilityServiceSettings(AccessibilityApi.BASE_SERVICE_CLS)
                },
                null
            )
            .addItemView(
                getGroupSwitchItemView(
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
                getGroupSwitchItemView(
                    "显示悬浮窗",
                    AccessibilityApi.isServiceEnable
                ) { _, isChecked ->
                    if (isChecked) {
                        showFloatView(true)
                    } else {
                        showFloatView(false)
                    }
                },
                null
            )
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)

        QMUIGroupListView.newSection(this)
            .setTitle("模块")
            .addItemView(

                getGroupNoneItemView("抢票")
            ) {
                buyDaMaiTicket("周五", "vip票", 2)
            }
            .setMiddleSeparatorInset(16.dp, 16.dp)
            .addTo(viewBinding.groupListView)
    }

    private fun showFloatView(show: Boolean) {
        floatBuilder = EasyFloat.with(this)
            // 设置浮窗xml布局文件/自定义View,并可设置详细信息
            .setLayout(R.layout.float_bubble) { }
            // 设置浮窗显示类型,默认只在当前Activity显示,可选一直显示、仅前台显示
            .setShowPattern(ShowPattern.ALL_TIME)
            // 设置吸附方式,共15种模式,详情参考SidePattern
            .setSidePattern(SidePattern.RESULT_HORIZONTAL)
            // 设置浮窗的标签,用于区分多个浮窗
            .setTag(this.javaClass.simpleName)
            // 设置浮窗是否可拖拽
            .setDragEnable(true)
            // 浮窗是否包含EditText,默认不包含
            .hasEditText(false)
            // 设置浮窗的对齐方式和坐标偏移量
            .setGravity(Gravity.START or Gravity.CENTER_VERTICAL, 0, 200)
            // 设置当布局大小变化后,整体view的位置对齐方式
            .setLayoutChangedGravity(Gravity.END)
            // 设置拖拽边界值
            .setBorder(100, 100, 800, 800)
            // 设置宽高是否充满父布局,直接在xml设置match_parent属性无效
            .setMatchParent(widthMatch = false, heightMatch = false)
            // 设置浮窗的出入动画,可自定义,实现相应接口即可（策略模式）,无需动画直接设置为null
            .setAnimator(DefaultAnimator())
            // 设置系统浮窗的不需要显示的页面
            // .setFilter(MainActivity::class.java, SecondActivity::class.java)
            // 设置系统浮窗的有效显示高度（不包含虚拟导航栏的高度）,基本用不到,除非有虚拟导航栏适配问题
            .setDisplayHeight { context -> DisplayUtils.rejectedNavHeight(context) }

        ToastUtils.showShort("showFloatView: $show")
        if (show) {
            floatBuilder?.show()
        } else {
            EasyFloat.dismiss(this.javaClass.simpleName)
        }
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