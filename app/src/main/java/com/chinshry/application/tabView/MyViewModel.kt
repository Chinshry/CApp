package com.chinshry.application.tabView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val data: LiveData<MutableList<DataBean>>
        get() {
            return _data
        }

    //变量初始化方法1  立即初始化
    private val _data: MutableLiveData<MutableList<DataBean>> = MutableLiveData(mutableListOf())

    fun updateData(data: MutableList<DataBean>?){
        _data.value  = data
    }
}