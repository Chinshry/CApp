package com.chinshry.base.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by chinshry on 2023/3/11.
 */
class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView as ImageView
}