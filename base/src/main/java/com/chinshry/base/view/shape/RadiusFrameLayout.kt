package com.chinshry.base.view.shape

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Path
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.chinshry.base.R
import com.chinshry.base.util.getIntArray
import com.chinshry.base.util.getStringArray
import com.chinshry.base.util.toFloatArray
import kotlin.math.max


class RadiusFrameLayout constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), IRadiusShape {
    // 圆角参数
    private var radius = 0
    private var radiusDrawable: RadiusDrawable? = null

    // 背景
    private var bgColorStateList: ColorStateList? = null
    private var bgShaderColors = intArrayOf()
    private var bgShaderColorsPositions = floatArrayOf()

    // 边框
    private var borderWidth = 0
    private var borderColorStateList: ColorStateList? = null
    private var borderShaderColors = intArrayOf()
    private var borderShaderColorsPositions = floatArrayOf()

    // 是否需要强制重新布局
    private var forceRefreshLayout = false

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        setWillNotDraw(false)
        // 读取圆角配置
        val radiusType: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusView)
        radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius, 0)

        // 获取背景信息
        bgColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_rv_background_color) ?: ColorStateList.valueOf(Color.TRANSPARENT)
        bgShaderColors = getIntArray(radiusType.getResourceId(R.styleable.RadiusView_rv_background_color_list, 0))
        bgShaderColorsPositions = getStringArray(radiusType.getResourceId(R.styleable.RadiusView_rv_background_position_list, 0)).toFloatArray()

        // 获取边框信息
        borderWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_border_width, 0)
        borderColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_rv_border_color) ?: ColorStateList.valueOf(Color.TRANSPARENT)
        borderShaderColors = getIntArray(radiusType.getResourceId(R.styleable.RadiusView_rv_border_color_list, 0))
        borderShaderColorsPositions = getStringArray(radiusType.getResourceId(R.styleable.RadiusView_rv_border_position_list, 0)).toFloatArray()
        radiusType.recycle()
    }

    override fun setBackgroundColor(color: Int) {
        bgColorStateList = ColorStateList.valueOf(color)
        radiusDrawable?.setBackground(bgColorStateList, borderColorStateList)
        forceRefreshLayout()
    }

    override fun setBackgroundColor(bgColorStateList: ColorStateList?) {
        this.bgColorStateList = bgColorStateList
        radiusDrawable?.setBackground(this.bgColorStateList, borderColorStateList)
        forceRefreshLayout()
    }

    override fun setBorderColor(color: Int) {
        borderColorStateList = ColorStateList.valueOf(color)
        radiusDrawable?.setBackground(bgColorStateList, borderColorStateList)
        forceRefreshLayout()
    }

    override fun setBorderColor(borderColorStateList: ColorStateList?) {
        this.borderColorStateList = borderColorStateList
        radiusDrawable?.setBackground(bgColorStateList, this.borderColorStateList)
        forceRefreshLayout()
    }

    override fun setRadius(radius: Int) {
        this.radius = radius
        forceRefreshLayout()
    }

    override fun setBackgroundColors(
        colors: IntArray,
        positions: FloatArray
    ) {
        if (isValidShader(colors, positions)) {
            bgShaderColors = colors
            bgShaderColorsPositions = positions
            forceRefreshLayout()
        }
    }

    override fun setBorderColors(
        colors: IntArray,
        positions: FloatArray
    ) {
        if (isValidShader(colors, positions)) {
            borderShaderColors = colors
            borderShaderColorsPositions = positions
            forceRefreshLayout()
        }
    }

    private fun isValidShader(
        colors: IntArray,
        positions: FloatArray
    ): Boolean {
        return colors.isNotEmpty() && positions.isNotEmpty() && colors.size == positions.size
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // 没有发生改变，并且不需要强制刷新就不在重新layout
        if (!changed && !forceRefreshLayout) {
            return
        }
        forceRefreshLayout = false
        val bgPath = setBackground()

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        val elevation = max(elevation, translationZ)
        if (elevation > 0) {
            setElevation(elevation)
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(bgPath)
                    } else {
                        outline.setConvexPath(bgPath)
                    }
                }
            }
            clipToOutline = true
        }
    }

    private fun forceRefreshLayout() {
        forceRefreshLayout = true
        requestLayout()
    }

    private fun setBackground(): Path {
        val bgPath: Path = RadiusUtils.calculateBgPath(
            radius, width.toFloat(), height.toFloat()
        )
        val bgShader = if (isValidShader(bgShaderColors, bgShaderColorsPositions)) {
            createShader(
                width,
                height,
                bgShaderColors,
                bgShaderColorsPositions
            )
        } else {
            null
        }

        // 边框
        if (borderWidth > 0) {
            val borderShader = if (isValidShader(borderShaderColors, borderShaderColorsPositions)) {
                createShader(
                    width,
                    height,
                    borderShaderColors,
                    borderShaderColorsPositions
                )
            } else {
                null
            }
            val borderPath = RadiusUtils.calculateSocketPath(
                radius, width.toFloat(), height.toFloat(), borderWidth
            )
            radiusDrawable = RadiusDrawable(
                bgColorStateList,
                bgShader,
                bgPath,
                borderWidth.toFloat(),
                borderColorStateList,
                borderShader,
                borderPath
            )
        } else {
            radiusDrawable = RadiusDrawable(bgColorStateList, bgShader, bgPath)
        }
        background = radiusDrawable
        return bgPath
    }

    private fun createShader(
        width: Int,
        height: Int,
        colors: IntArray,
        positions: FloatArray,
    ): Shader {
        return LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            colors, positions, Shader.TileMode.CLAMP
        )
    }
}