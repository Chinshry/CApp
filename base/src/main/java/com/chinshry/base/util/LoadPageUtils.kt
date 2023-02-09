package com.chinshry.base.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.chinshry.base.databinding.CommonEmptyLayoutBinding
import com.chinshry.base.databinding.CommonNetworkErrorLayoutBinding
import com.chinshry.base.view.clickWithTrigger

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
        val emptyViewBinding = CommonEmptyLayoutBinding.inflate(LayoutInflater.from(context))
        emptyViewBinding.tvEmpty.text = emptyText
        emptyImg?.let {
            emptyViewBinding.imgEmpty.setImageResource(it)
        }
        if (!extraText.isNullOrEmpty()) {
            emptyViewBinding.btnEmpty.visibility = View.VISIBLE
            emptyViewBinding.btnEmpty.text = extraText
            extraTextFun?.let {
                emptyViewBinding.btnEmpty.clickWithTrigger {
                    extraTextFun()
                }
            }
        }
        return emptyViewBinding.root
    }

    fun getNetErrorView(
        context: Context,
        reloadFun: (() -> Unit),
        reloadText: String? = "重试",
        errorImg: Int? = null,
    ): View {
        val netErrorViewBinding = CommonNetworkErrorLayoutBinding.inflate(LayoutInflater.from(context))
        errorImg?.let {
            netErrorViewBinding.imgNet.setImageResource(it)
        }
        netErrorViewBinding.btnReload.text = reloadText
        netErrorViewBinding.btnReload.clickWithTrigger {
            reloadFun()
        }
        return netErrorViewBinding.root
    }

}