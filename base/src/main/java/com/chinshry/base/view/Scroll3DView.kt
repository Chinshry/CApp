package com.chinshry.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.chinshry.base.adapter.Scroll3DAdapter
import com.chinshry.base.databinding.LayoutScroll3dBinding
import com.chinshry.base.transformer.Scroll3DTransformer
import com.chinshry.base.util.dp

/**
 * Created by chinshry on 2023/03/11.
 * Describe：3D滑动组件
 */
class Scroll3DView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val viewBinding: LayoutScroll3dBinding
    private val itemWidth = 200.dp

    private val positions = listOf(0, 1, 2)
    private val scales = listOf(1f, 0.6f, 0.3f)
    private val alphas = listOf(1f, 0.6f, 0f)
    private val rotations = listOf(0f, 20f, 20f)
    private val translations = listOf(0f, 80f.dp, 240f.dp)

    init {
        viewBinding = LayoutScroll3dBinding.inflate(LayoutInflater.from(context), this, true)
        setRecyclerViewWidth()

        viewBinding.viewpager2.offscreenPageLimit = 2
        viewBinding.viewpager2.setPageTransformer(Scroll3DTransformer(positions, scales, alphas, rotations, translations))
    }

    fun setData(data: List<Int>) {
        viewBinding.viewpager2.adapter = Scroll3DAdapter(data)
    }

    private fun setRecyclerViewWidth() {
        val recyclerView = (viewBinding.viewpager2.getChildAt(0) as? RecyclerView) ?: return
        recyclerView.clipChildren = false
        recyclerView.clipToPadding = false

        val paddingHorizontal = (ScreenUtils.getAppScreenWidth() - itemWidth) / 2
        recyclerView.setPadding(
            paddingHorizontal,
            recyclerView.paddingTop,
            paddingHorizontal,
            recyclerView.paddingBottom
        )
    }
}