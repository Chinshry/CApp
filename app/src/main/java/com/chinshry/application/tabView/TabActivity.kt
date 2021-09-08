package com.chinshry.application.tabView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.chinshry.application.R
import com.chinshry.application.bean.Router

@Route(path = Router.ACTIVITY_TABVIEW)
class TabActivity : AppCompatActivity() {
    private var data1 : MutableList<DataBean> = mutableListOf(
        DataBean("1"),
        DataBean("2"),
        DataBean("3")
    )
    private var data2 : MutableList<DataBean> = mutableListOf(
        DataBean("11"),
        DataBean("22"),
        DataBean("33")
    )
    private var data3 : MutableList<DataBean> = mutableListOf(
        DataBean("111"),
        DataBean("222"),
        DataBean("333")
    )

    private var data : ListDataBean =
        ListDataBean(data1, data2, data3)
    private val titles = arrayOf("最新", "热门", "我的")
    private val fragments: MutableList<PlaceholderFragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabview)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)


        for (i in titles.indices) {
            val dataList: MutableList<DataBean> = when(i){
                0 -> data.first
                1 -> data.second
                2 -> data.third
                else  -> data.first
            }
            fragments.add(PlaceholderFragment.newInstance(dataList))
            tabs.addTab(tabs.newTab())
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(
            fragments,
            supportFragmentManager
        )
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)


        for (i in titles.indices) {
            tabs.getTabAt(i)?.text = titles[i]
        }
    }


}