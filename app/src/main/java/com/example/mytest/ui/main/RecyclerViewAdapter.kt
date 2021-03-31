package com.example.mytest.ui.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest.R


class RecyclerViewAdapter(context: Context, data: MutableList<testDataBean>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    //当前上下文对象
    var context: Context = context

    //RecyclerView填充Item数据的List对象
    var datas: MutableList<testDataBean> = data

    //创建ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        //引入item布局并且实例化为对象
        val v: View = View.inflate(context, R.layout.item_layout, null)
        //返回MyViewHolder的对象 这是自己创建的继承RecyclerView.ViewHolder的 类
        return MyViewHolder(v)
    }

    //绑定数据
    // 通过调用布局里面的view对象进行数据方面的绑定
    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.textView.text = datas[position].title
    }

    //告诉系统item的数量
    override fun getItemCount(): Int {
        return datas.size
    }

    //继承RecyclerView.ViewHolder抽象类的自定义ViewHolder
    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.item_text)
    }

}