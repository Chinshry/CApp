package com.chinshry.application

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bingoogolapple.badgeview.annotation.BGABadge


/**
 * Created by chinshry on 2018/3/14.
 * Project Name:MyTest
 * Package Name:com.chinshry.application
 * File Name:BGABadgeInit
 * Describe：Application
 */
@BGABadge(
    View::class,  // 对应 cn.bingoogolapple.badgeview.BGABadgeView，不想用这个类的话就删了这一行
    ImageView::class,  // 对应 cn.bingoogolapple.badgeview.BGABadgeImageView，不想用这个类的话就删了这一行
    TextView::class,  // 对应 cn.bingoogolapple.badgeview.BGABadgeFloatingTextView，不想用这个类的话就删了这一行
    LinearLayout::class,  // 对应 cn.bingoogolapple.badgeview.BGABadgeLinearLayout，不想用这个类的话就删了这一行
    RelativeLayout::class,  // 对应 cn.bingoogolapple.badgeview.BGABadgeRelativeLayout，不想用这个类的话就删了这一行
)
class BGABadgeInit{

}