package com.chinshry.application.tabView

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.chinshry.application.R
import com.chinshry.application.bean.Router
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_tabview.*

@Route(path = Router.ACTIVITY_TABVIEW)
class TabActivity : AppCompatActivity() {
    private var data1 : MutableList<DataBean> = mutableListOf(
        DataBean("1"),
        DataBean("2"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
        DataBean("3"),
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
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabview)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
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

        sectionsPagerAdapter = SectionsPagerAdapter2(fragments, this)

        viewPager.adapter = sectionsPagerAdapter
        viewPager.isUserInputEnabled = false
        viewPager.isSaveEnabled = false
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        for (i in titles.indices) {
            tabs.getTabAt(i)?.text = titles[i]
        }
    }

}