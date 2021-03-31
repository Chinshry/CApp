package com.example.mytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mytest.ui.main.PlaceholderFragment
import com.example.mytest.ui.main.SectionsPagerAdapter
import com.example.mytest.ui.main.testDataBean
import com.example.mytest.ui.main.testListBean
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private var data1 : MutableList<testDataBean> = mutableListOf(testDataBean("1"), testDataBean("2"), testDataBean("3"))
    private var data2 : MutableList<testDataBean> = mutableListOf(testDataBean("11"), testDataBean("22"), testDataBean("33"))
    private var data3 : MutableList<testDataBean> = mutableListOf(testDataBean("111"), testDataBean("222"), testDataBean("333"))

    private var data : testListBean = testListBean(data1, data2, data3)
    private val titles = arrayOf("最新", "热门", "我的")
    private val fragments: MutableList<PlaceholderFragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)


        for (i in titles.indices) {
            val dataList: MutableList<testDataBean> = when(i){
                0 -> data.first
                1 -> data.second
                2 -> data.third
                else  -> data.first
            }
            fragments.add(PlaceholderFragment.newInstance(dataList))
            tabs.addTab(tabs.newTab())
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(fragments, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)


        for (i in titles.indices) {
            tabs.getTabAt(i)?.text = titles[i]
        }
    }


}