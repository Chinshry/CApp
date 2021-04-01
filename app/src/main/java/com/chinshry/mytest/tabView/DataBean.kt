package com.chinshry.mytest.tabView

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.mytest.ui.main
 * File Name:DataBean
 * Describe：Application
 */

data class testDataBean(
    val title: String
)

data class testListBean(
    var first: MutableList<testDataBean>,
    var second: MutableList<testDataBean>,
    var third: MutableList<testDataBean>
)

