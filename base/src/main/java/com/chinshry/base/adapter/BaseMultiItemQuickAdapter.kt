package com.chinshry.base.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chinshry.base.R
import java.util.ArrayList

/**
 * Created by chinshry on 2022/03/21.
 * Describe：MultiItemAdapter
 */
abstract class BaseMultiItemQuickAdapter<T : MultiItemEntity, VH : BaseViewHolder>(data: MutableList<T>? = null) : BaseDelegateMultiAdapter<T, VH>(data) {

    private var supportTypes = ArrayList<Int>()

    init {
        setMultiTypeDelegate(customBaseMultiTypeDelegate())
        getMultiTypeDelegate()?.addItemType(-1, R.layout.layout_empty)
    }

    private fun customBaseMultiTypeDelegate(): BaseMultiTypeDelegate<T> {
        return object : BaseMultiTypeDelegate<T>() {
            override fun getItemType(data: List<T>, position: Int): Int {
                return getMultiItemType(data, position) ?: data.getOrNull(position)?.itemType ?: -1
            }
        }
    }

    fun addSupportViewType(type: Int, @LayoutRes layoutResId: Int) {
        getMultiTypeDelegate()?.addItemType(type, layoutResId)
        supportTypes.add(type)
    }

    override fun getDefItemViewType(position: Int): Int {
        val itemType = getMultiTypeDelegate()?.getItemType(data, position)
        itemType ?.let {
            if (supportTypes.contains(it)) {
                return it
            }
        }

        return -1
    }

    override fun convert(holder: VH, item: T) {
        if (item.itemType != -1) {
            convertHolder(holder, item, listOf())
        }
    }

    override fun convert(holder: VH, item: T, payloads: List<Any>) {
        if (item.itemType != -1) {
            convertHolder(holder, item, payloads)
        }
    }

    /**
     * 布局holder，不要使用convert方法
     */
    abstract fun convertHolder(holder: VH, item: T, payloads: List<Any>)

    abstract fun getMultiItemType(data: List<T>, position: Int) : Int?
}