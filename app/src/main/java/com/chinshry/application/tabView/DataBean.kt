package com.chinshry.application.tabView

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.mytest.ui.main
 * File Name:DataBean
 * Describe：Application
 */

data class DataBean(
    val title: String
)

data class ListDataBean(
    var first: MutableList<DataBean>,
    var second: MutableList<DataBean>,
    var third: MutableList<DataBean>
)

