package com.chinshry.capp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.chinshry.base.BaseActivity
import com.chinshry.base.bean.Module
import com.chinshry.base.bean.TabBean
import com.chinshry.base.util.getColorById
import com.chinshry.base.util.getDrawableById
import com.chinshry.base.view.CustomTabView
import com.chinshry.capp.databinding.ActivityMainBinding
import com.chinshry.home.fragment.HomeFragment
import com.chinshry.tool.fragment.ToolFragment

/**
 * Created by chinshry on 2021/12/23.
 * Describe： 主Activity
 */
open class MainActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var currentFragment: Fragment
    private val fragments = listOf(HomeFragment(), ToolFragment())

    private val tabData = listOf(
        TabBean(
            model = 2,
            normalTitle = "工具",
            selectTitle = "工具1",
            normalIcon = R.mipmap.ic_tab_util,
            selectIcon = R.mipmap.ic_tab_util_selected,
            badge = R.drawable.badge_red
        ),
        TabBean(
            model = 1,
            normalTitle = "首页",
            selectTitle = "首页1",
            normalIcon = R.mipmap.ic_tab_home,
            selectIcon = R.mipmap.ic_tab_home_selected,
            normalTextColor = R.color.green,
            selectTextColor = R.color.red,
            badge = R.drawable.badge_red
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

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
            viewBinding.customTab.addTab(
                CustomTabView.Tab()
                    .setTabText(
                        normal = it.normalTitle,
                        select = it.selectTitle)
                    .setIcon(
                        normal = it.normalIcon?.let { it1 -> getDrawableById(it1) },
                        select = it.selectIcon?.let { it1 -> getDrawableById(it1) })
                    .setTextColor(
                        normal = it.normalTextColor?.let { it1 -> getColorById(it1) },
                        select = it.selectTextColor?.let { it1 -> getColorById(it1) })
                    .setBadge(it.badge?.let { it1 -> getDrawableById(it1) })
                    .onTabSelect {
                        changeFragment(it.model!!)
                    }
            )
        }

        viewBinding.customTab.setCurrentItem(0)
    }

    private fun changeFragment(model: Int) {
        val fragment: Fragment = getFragmentByModel(model) ?: return

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                .commit()
            currentFragment = fragment

        } else {
            if (currentFragment != fragment) {
                val transaction = supportFragmentManager.beginTransaction()
                if (fragment.isAdded)
                    transaction.hide(currentFragment).show(fragment)
                else
                    transaction.hide(currentFragment).add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
                transaction.commit()
                currentFragment = fragment
            }
        }

    }

    private fun getFragmentByModel(model: Int?): Fragment? {
        val fragmentName = Module.getModuleByModel(model)?.name ?: return null
        fragments.forEach {
            if (it.javaClass.simpleName == fragmentName + "Fragment") return it
        }
        return null
    }

}


