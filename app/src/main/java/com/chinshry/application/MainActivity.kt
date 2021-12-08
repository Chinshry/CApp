package com.chinshry.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.alibaba.android.arouter.launcher.ARouter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {

    private val TAG = "chinshry"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        initializeBottomNavigationBar()
        // nav_view.setupWithNavController(navController)

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


}


