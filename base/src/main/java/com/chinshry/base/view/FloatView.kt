package com.chinshry.base.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chinshry.base.bean.FloatingWindowData
import com.chinshry.base.databinding.LayoutTabFloatBinding
import com.chinshry.base.util.FloorUtil.getImageUrl

/**
 * Created by chinshry on 2022/03/21.
 * Describe：tab浮窗组件
 */
class FloatView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val viewBinding: LayoutTabFloatBinding

    init {
        viewBinding = LayoutTabFloatBinding.inflate(LayoutInflater.from(context), this, true)
    }

    companion object {
        // 动画执行时间
        const val ANIM_DURATION = 500L
        // 默认隐藏3秒后出现
        const val DEFAULT_DELAY_SHOW_TIME = 3
    }

    // 0 展开 1 收起 2 动画中
    private var state = 0
    private var dataId: String? = null
    private var mHandler = Handler(Looper.getMainLooper())

    /**
     * 初始化浮窗
     * @param recyclerView 同层recyclerView
     * @param floatingWindowData 数据
     */
    @SuppressLint("ClickableViewAccessibility")
    fun initFloat(
        recyclerView: RecyclerView,
        floatingWindowData: FloatingWindowData?
    ) {
        // 同一ID return
        if (floatingWindowData == null || dataId == floatingWindowData.id) {
            return
        } else {
            dataId = floatingWindowData.id
        }

        viewBinding.ivClose.isVisible = floatingWindowData.closed ?: true

        viewBinding.ivClose.clickWithTrigger {
            remove(recyclerView)
        }

        viewBinding.ivImage.contentDescription = floatingWindowData.buryPoint
        viewBinding.ivImage.clickWithTrigger(floatingWindowData.buryPoint) {
            // TODO CLICK ITEM
        }

        // 收起后间隔多久展开
        var delayShowTime = (floatingWindowData.delayShowTime ?: DEFAULT_DELAY_SHOW_TIME) * 1000L

        // Touch监听
        // 移动中: 浮窗收起 向右滚动至图片宽度50% 透明度30%
        // 移动停止/移出 N秒后(可配置): 浮窗展开 恢复初始状态
        val listener = OnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_OUTSIDE -> {
                    // 本次为点击事件 不响应
                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        return@OnTouchListener false
                    }

                    // state == 0 此时是展开状态
                    // else 此时是收起和动画中 设置延时展开任务
                    when (state) {
                        0 -> {
                            return@OnTouchListener false
                        }
                        2 -> {
                            // state == 2 动画中 且间隔为0 加上动画执行时间 使上个动画执行完成
                            if (delayShowTime == 0L) {
                                delayShowTime += ANIM_DURATION
                            }
                        }
                    }
                    // 移除未执行的任务
                    mHandler.removeCallbacksAndMessages(null)
                    mHandler.postDelayed(
                        { if (state == 1) { changeFloatState(isShowAnim = true) } },
                        delayShowTime
                    )
                }
                MotionEvent.ACTION_MOVE,
                MotionEvent.ACTION_SCROLL -> {
                    // 本次为点击事件 不响应
                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        return@OnTouchListener false
                    }
                    // 移除未执行的任务
                    mHandler.removeCallbacksAndMessages(null)
                    // state == 0 此时是展开状态 执行收起动画
                    // else 此时是收起和动画中 不响应
                    if (state == 0) {
                        changeFloatState(isShowAnim = false)
                    }

                }
            }
            return@OnTouchListener false
        }

        initFloatPicture(recyclerView, floatingWindowData.imageUrl, listener)
    }

    private fun initFloatPicture(
        recyclerView: RecyclerView,
        pictureUrl: String?,
        touchListener: OnTouchListener
    ) {
        val requestListener = object : RequestListener<Drawable?> {

            // 图片加载失败 隐藏浮窗
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                remove(recyclerView)
                return false
            }

            // 图片加载成功 显示浮窗
            @SuppressLint("ClickableViewAccessibility")
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                this@FloatView.isVisible = true
                recyclerView.setOnTouchListener(touchListener)
                return false
            }
        }
        Glide.with(context)
            .load(getImageUrl(pictureUrl))
            .listener(requestListener)
            .into(viewBinding.ivImage)
    }

    /**
     * 浮窗动画
     * @param isShowAnim 是否为展开动画
     */
    private fun changeFloatState(isShowAnim: Boolean) {
        // 折叠收起动画
        var moveList = listOf(0f, (viewBinding.ivImage.width / 2 + viewBinding.ivImage.marginEnd).toFloat())
        var alphaList = listOf(1.0f, 0.3f)

        // 若现在非展示状态 要展开 动画反转
        if (isShowAnim) {
            moveList = moveList.reversed()
            alphaList = alphaList.reversed()
        }

        val animatorSet = AnimatorSet()
        val translationY =
            ObjectAnimator.ofFloat(
                this@FloatView,
                "TranslationX",
                moveList[0],
                moveList[1]
            )
        val alphaAnimation = ObjectAnimator.ofFloat(
            this@FloatView,
            "alpha",
            alphaList[0],
            alphaList[1]
        )

        animatorSet.playTogether(translationY, alphaAnimation)
        animatorSet.duration = ANIM_DURATION
        animatorSet.addListener (
            onStart = { state = 2 },
            onEnd = { state = if (isShowAnim) 0 else 1 }
        )

        animatorSet.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun remove(recyclerView: RecyclerView) {
        this.isVisible = false
        recyclerView.setOnTouchListener(null)
        mHandler.removeCallbacksAndMessages(null)
    }

}