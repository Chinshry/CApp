package com.chinshry.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
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
        // initializeBottomNavigationBar()
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

    private fun initializeBottomNavigationBar() {
        nav_view.isAutoHideEnabled = false
        // TODO 设置模式
        nav_view.setMode(BottomNavigationBar.MODE_FIXED)
        // TODO 设置背景色样式
        nav_view.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        // nav_view.setBarBackgroundColor(R.color.tabBGColor)

        // //父布局就是我们的 mBottomNavigationBar
        // val parentView: View = LayoutInflater.from(this).inflate(
        //     com.ashokvarma.bottomnavigation.R.layout.bottom_navigation_bar_container,
        //     nav_view,
        //     true
        // )
        // //mTabContainer 是放置item bar 的线性布局，item id 就是我们刚才找了半天找到的
        // val mTabContainer = parentView.findViewById<LinearLayout>(com.ashokvarma.bottomnavigation.R.id.bottom_navigation_bar_item_container)
        // //mTabContainer 是个ViewGroup，通过getChildAt 就能获取对应的子 view
        // val mIconView =  //购物车item 的小红点图片
        //     mTabContainer.getChildAt(2)
        //         .findViewById<View>(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon) as ImageView
        //
        // mIconView.layoutParams = LinearLayout.LayoutParams(
        //     SizeUtils.dp2px(50f),
        //     SizeUtils.dp2px(50f),
        // )

        val shapeBadgeItem = ShapeBadgeItem()
        // (shapeBadgeItem as BadgeTextView).background = ResourceUtils.getDrawable(R.drawable.red_bubble_bg)
        shapeBadgeItem.setShape(ShapeBadgeItem.SHAPE_RECTANGLE)
            .setSizeInDp(this, 14, 41)
            .setEdgeMarginInDp(this, 1)
            .setHideOnSelect(true)


        nav_view
            .addItem(
                BottomNavigationItem(
                    R.color.blue,
                    "HOME"
                ).setActiveColorResource(R.color.blue)
                    // .setInactiveIconResource(R.drawable.tab_home)
                    .setInActiveColorResource(R.color.green)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_dashboard_black_24dp,
                    "Dashboard"
                ).setActiveColorResource(R.color.blue)
                    // .setInactiveIconResource(R.drawable.tab_video)
                    .setInActiveColorResource(R.color.green)
                    .setBadgeItem(shapeBadgeItem)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_notifications_black_24dp,
                    "Notifications"
                ).setActiveColorResource(R.color.blue)
                    // .setInactiveIconResource(R.drawable.tab_mine)
                    .setInActiveColorResource(R.color.green)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_notifications_black_24dp,
                    "Notifications"
                ).setActiveColorResource(R.color.blue)
                    // .setInactiveIconResource(R.drawable.tab_mine)
                    .setInActiveColorResource(R.color.green)
            )
            .addItem(
                BottomNavigationItem(
                    R.drawable.ic_notifications_black_24dp,
                    "Notifications"
                ).setActiveColorResource(R.color.blue)
                    // .setInactiveIconResource(R.drawable.tab_mine)
                    .setInActiveColorResource(R.color.green)
            )
            .setFirstSelectedPosition(0)
            .initialise()
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


