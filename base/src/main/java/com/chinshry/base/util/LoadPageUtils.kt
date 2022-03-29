package com.chinshry.base.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.chinshry.base.R
import com.chinshry.base.view.clickWithTrigger
import kotlinx.android.synthetic.main.common_empty_layout.view.*
import kotlinx.android.synthetic.main.common_network_error_layout.view.*

/**
 * Created by chinshry on 2021/5/23.
 * Describe： 空数据/网络错误
 */
object LoadPageUtils {

    fun getEmptyView(
        context: Context,
        emptyText: String,
        emptyImg: Int? = null,
        extraText: String? = null,
        extraTextFun: (() -> Unit)? = null,
    ): View {
        val emptyView: View = LayoutInflater.from(context).inflate(R.layout.common_empty_layout, null)
        emptyView.tv_empty.text = emptyText
        emptyImg?.let {
            emptyView.img_empty.setImageResource(it)
        }
        if (!extraText.isNullOrEmpty()) {
            emptyView.btn_empty.visibility = View.VISIBLE
            emptyView.btn_empty.text = extraText
            extraTextFun?.let {
                emptyView.btn_empty.clickWithTrigger {
                    extraTextFun()
                }
            }
        }
        return emptyView
    }

    fun getNetErrorView(
        context: Context,
        reloadFun: (() -> Unit),
        reloadText: String? = "重试",
        errorImg: Int? = null,
    ): View {
        val netErrorView: View =
            LayoutInflater.from(context).inflate(R.layout.common_network_error_layout, null)
        errorImg?.let {
            netErrorView.img_net.setImageResource(it)
        }
        netErrorView.btn_reload.text = reloadText
        netErrorView.btn_reload.clickWithTrigger {
            reloadFun()
        }
        return netErrorView
    }

}