package com.chinshry.base.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.chinshry.base.holder.ImageHolder

/**
 * Created by chinshry on 2023/3/11.
 * Describe：滚动3d Adapter
 */
class Scroll3DAdapter : RecyclerView.Adapter<ImageHolder>(){
    var data: List<Int> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.adjustViewBounds = true
        return ImageHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.imageView.setImageResource(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}