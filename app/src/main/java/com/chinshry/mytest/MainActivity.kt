package com.chinshry.mytest

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ResourceUtils
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.FragmentBean
import com.chinshry.base.bean.TabBean
import com.chinshry.base.view.CustomTabView
import com.chinshry.home.fragment.HomeFragment
import com.chinshry.tool.fragment.ToolFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 主Activity
 */
open class MainActivity : BaseActivity() {

    private val fragments = listOf(
        FragmentBean(
            fragment = HomeFragment(),
            model = 0
        ),
        FragmentBean(
            fragment = ToolFragment(),
            model = 1
        )
    )

    private val tabData = listOf(
        TabBean(
            model = 1,
            title = "工具",
            normalIcon = R.mipmap.ic_tab_util,
            selectIcon = R.mipmap.ic_tab_util_selected,
        ),
        TabBean(
            model = 0,
            title = "首页",
            normalIcon = R.mipmap.ic_tab_home,
            selectIcon = R.mipmap.ic_tab_home_selected,
            badge = R.drawable.badge_red
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initApp()
        initCustomTab()
    }

    private fun initApp() {
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(application)

        // DoKit.Builder(application)
        //     .productId("")
        //     .build()
    }


    private fun initCustomTab() {
        tabData.forEach {
            if (it.model == null) return@forEach
            custom_tab.addTab(
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

        custom_tab.setCurrentItem(0)
    }

    private fun changeFragment(position: Int) {
        val fragment: Fragment? = getFragmentByModel(position)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

    }

    private fun getFragmentByModel(model: Int?): Fragment? {
        fragments.forEach {
            if (it.model == model) return it.fragment
        }
        return null
    }

}


