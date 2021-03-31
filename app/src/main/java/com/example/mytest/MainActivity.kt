package com.example.mytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mytest.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private var data1 :MutableList<String> = mutableListOf("1","2","3")
    private var data2 :MutableList<String> = mutableListOf("11","22","33")
    private var data3 :MutableList<String> = mutableListOf("111","222","333")
    private var data : MutableList<MutableList<String>> = mutableListOf(data1, data2, data3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, data, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }


}