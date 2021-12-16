package com.chinshry.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ResourceUtils
import com.chinshry.application.bean.TabBean
import com.chinshry.application.fragment.HomeFragment
import com.chinshry.application.ui.dashboard.DashboardFragment
import com.chinshry.application.ui.notifications.NotificationsFragment
import com.chinshry.application.view.CustomTabView
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    private val TAG = "chinshry"

    private lateinit var mCustomTabView: CustomTabView
    private val fragments: List<Fragment> = listOf(
        HomeFragment(),
        DashboardFragment(),
        NotificationsFragment()
    )

    val tabData = listOf<TabBean>(
        TabBean(model = 0, title = "首页", normalIcon = R.drawable.ic_tab_strip_icon_feed, selectIcon = R.drawable.ic_tab_strip_icon_feed_selected),
        TabBean(model = 1, title = "dashboard", normalIcon = R.drawable.ic_tab_strip_icon_category, selectIcon = R.drawable.ic_tab_strip_icon_category_selected, badge = R.drawable.red_bubble_bg),
        TabBean(model = 2, title = "通知", normalIcon = R.drawable.ic_tab_strip_icon_pgc, selectIcon = R.drawable.ic_tab_strip_icon_pgc_selected),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        initCustomTab()
    }

    private fun initCustomTab() {
        mCustomTabView = findViewById(R.id.custom_tab_container)
        tabData.forEach {
            if (it.model == null) return@forEach
            mCustomTabView.addTab(
                CustomTabView.Tab()
                    .setTabText(it.title)
                    .setIcon(
                        normal = it.normalIcon?.let { it1 -> ResourceUtils.getDrawable(it1) },
                        select = it.selectIcon?.let { it1 -> ResourceUtils.getDrawable(it1) })
                    .setBadge(it.badge?.let { it1 -> ResourceUtils.getDrawable(it1) })
                    .onTabSelect {
                        changeFragment(it.model!!)
                    }
            )
        }

        mCustomTabView.setCurrentItem(0)
    }

    private fun init() {
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(application)

        // DoKit.Builder(application)
        //     .productId("需要使用平台功能的话，需要到dokit.cn平台申请id")
        //     .build()
    }

    private fun changeFragment(position: Int) {
        val fragment: Fragment? = fragments.getOrNull(position)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .commit()
        }

    }
}


