package com.chinshry.base.view.shape

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Shader
import android.graphics.drawable.Drawable

class RadiusDrawable(
    bgColorsList: ColorStateList?,
    bgShader: Shader?,
    path: Path,
    solidWidth: Float,
    solidColorsList: ColorStateList?,
    solidShader: Shader?,
    solidPath: List<Path>?
) : Drawable() {
    // 背景颜色相关
    private var backGroundColor: ColorStateList? = null
    // 渐变优先级更高
    private var mBgShader: Shader?
    private val mBgPath: Path
    private val mBgPaint: Paint
    private var mBgTintFilter: PorterDuffColorFilter? = null
    private var mBgTint: ColorStateList? = null
    private var mBgTintMode: PorterDuff.Mode?

    // 边框线相关
    private val mDrawSolid: Boolean
    private var solidColor: ColorStateList? = null
    // 渐变优先级更高
    private var mSolidShader: Shader? = null
    private var mSolidPath: List<Path>? = null
    private var mSolidPaint: Paint? = null
    private var mSolidTintFilter: PorterDuffColorFilter? = null
    private var mSolidTint: ColorStateList? = null
    private var mSolidTintMode: PorterDuff.Mode? = null

    constructor(bgColorsList: ColorStateList?, bgShader: Shader?, path: Path) : this(
        bgColorsList,
        bgShader,
        path,
        0f,
        null,
        null,
        null
    )

    init {
        mBgPath = path
        mBgTintMode = PorterDuff.Mode.SRC_IN
        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint.isDither = true
        mBgShader = bgShader
        mDrawSolid = solidWidth > 0
        if (mDrawSolid) {
            mSolidPath = solidPath ?: ArrayList()
            mSolidTintMode = PorterDuff.Mode.SRC_IN
            mSolidPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                isAntiAlias = true
                isDither = true
                style = Paint.Style.STROKE
                strokeWidth = solidWidth
            }
            mSolidShader = solidShader
        }
        setBackground(bgColorsList, solidColorsList)
    }

    fun setBackground(bgColorsList: ColorStateList?, solidColorsList: ColorStateList?) {
        if (mBgShader == null) {
            (bgColorsList ?: ColorStateList.valueOf(0)).let {
                backGroundColor = it
                mBgPaint.color = it.getColorForState(this.state, it.defaultColor)
            }
        }
        if (mDrawSolid && mSolidShader == null) {
            (solidColorsList ?: ColorStateList.valueOf(0)).let {
                solidColor = it
                mSolidPaint?.color = it.getColorForState(this.state, it.defaultColor)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        val bgPaint: Paint = mBgPaint
        var bgClearColorFilter = false
        val hasBgShader = mBgShader != null
        if (hasBgShader) {
            bgPaint.shader = mBgShader
        } else {
            bgClearColorFilter = if (mBgTintFilter != null) {
                bgPaint.colorFilter = mBgTintFilter
                true
            } else {
                false
            }
        }
        canvas.drawFilter = PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
        canvas.drawPath(mBgPath, bgPaint)
        if (bgClearColorFilter) {
            bgPaint.colorFilter = null
        }
        if (mDrawSolid) {
            val solidPaint: Paint = mSolidPaint!!
            var solidClearColorFilter = false
            val hasSolidShader = mSolidShader != null
            if (hasSolidShader) {
                solidPaint.shader = mSolidShader
            } else {
                solidClearColorFilter = if (mSolidTintFilter != null) {
                    solidPaint.colorFilter = mSolidTintFilter
                    true
                } else {
                    false
                }
            }
            for (solidPath in mSolidPath!!) {
                canvas.drawPath(solidPath, solidPaint)
            }
            if (solidClearColorFilter) {
                solidPaint.colorFilter = null
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        if (mBgShader == null) {
            mBgPaint.alpha = alpha
        }
        if (mDrawSolid) {
            mSolidPaint?.alpha = alpha
        }
    }

    override fun setColorFilter(cf: ColorFilter?) {
        if (mBgShader == null) {
            mBgPaint.colorFilter = cf
        }
        if (mDrawSolid) {
            mSolidPaint?.colorFilter = cf
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setTintList(tint: ColorStateList?) {
        if (mBgShader == null) {
            mBgTint = tint
            mBgTintFilter = createTintFilter(mBgTint, mBgTintMode)
        }
        if (mDrawSolid) {
            mSolidTint = tint
            mSolidTintFilter = createTintFilter(mSolidTint, mSolidTintMode)
        }
        invalidateSelf()
    }

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        if (mBgShader == null) {
            mBgTintMode = tintMode
            mBgTintFilter = createTintFilter(mBgTint, mSolidTintMode)
        }
        if (mDrawSolid && mSolidShader == null) {
            mSolidTintMode = tintMode
            mSolidTintFilter = createTintFilter(mBgTint, mSolidTintMode)
        }
        invalidateSelf()
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        var bgColorChanged = false
        if (mBgShader == null) {
            val newBgColor =
                backGroundColor!!.getColorForState(stateSet, backGroundColor!!.defaultColor)
            bgColorChanged = newBgColor != mBgPaint.color
            if (bgColorChanged) {
                mBgPaint.color = newBgColor
            }
        }
        var solidColorChanged = false
        if (mDrawSolid && mSolidShader == null) {
            val newSolidColor = solidColor!!.getColorForState(stateSet, solidColor!!.defaultColor)
            solidColorChanged = newSolidColor != mSolidPaint?.color
            if (solidColorChanged) {
                mSolidPaint?.color = newSolidColor
            }
        }
        return if (mBgShader == null && mBgTint != null && mBgTintMode != null) {
            mBgTintFilter = createTintFilter(mBgTint, mBgTintMode)
            invalidateSelf()
            true
        } else if (mSolidShader == null && mSolidTint != null && mSolidTintMode != null) {
            mSolidTintFilter = createTintFilter(mSolidTint, mSolidTintMode)
            true
        } else {
            bgColorChanged || solidColorChanged
        }
    }

    override fun isStateful(): Boolean {
        return if (mDrawSolid) {
            mBgTint != null && mBgTint!!.isStateful || backGroundColor != null && backGroundColor!!.isStateful || mSolidTint != null && mSolidTint!!.isStateful || solidColor != null && solidColor!!.isStateful || super.isStateful()
        } else {
            mBgTint != null && mBgTint!!.isStateful || backGroundColor != null && backGroundColor!!.isStateful || super.isStateful()
        }
    }

    private fun createTintFilter(
        tint: ColorStateList?,
        tintMode: PorterDuff.Mode?
    ): PorterDuffColorFilter? {
        return if (tint != null && tintMode != null) {
            val color = tint.getColorForState(this.state, 0)
            PorterDuffColorFilter(color, tintMode)
        } else {
            null
        }
    }
}