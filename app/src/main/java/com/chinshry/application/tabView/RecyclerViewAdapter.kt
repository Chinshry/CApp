package com.chinshry.application.tabView

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chinshry.application.R


class RecyclerViewAdapter(val context: Context, var data: MutableList<DataBean>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    //创建ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val v: View = View.inflate(context, R.layout.tab_item_layout, null)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.textView.text = data[position].title
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.item_text)
    }

}