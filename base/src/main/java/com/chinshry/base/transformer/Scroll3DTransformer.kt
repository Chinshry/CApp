package com.chinshry.base.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.LogUtils
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor


/**
 * Created by chinshry on 2023/3/11.
 * Describe：滚动3D 组件 动画
 */
class Scroll3DTransformer(
    private val positions: List<Int>,
    private val scales: List<Float>,
    private val alphas: List<Float>,
    private val rotations: List<Float>,
    private val translations: List<Float>
) : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val scale: Float
        val alpha: Float
        val rotationY: Float
        val translationX: Float
        val elevation: Float

        val targetIndex = positions.indexOfFirst { it > abs(position) }
        val maxOffsetPosition = positions.last()
        if (targetIndex >= 0) {
            scale = scales[targetIndex]  + (scales[targetIndex - 1] - scales[targetIndex]) * (positions[targetIndex] - abs(position))
            alpha = alphas[targetIndex]  + (alphas[targetIndex - 1] - alphas[targetIndex]) * (positions[targetIndex] - abs(position))
            rotationY = (rotations[targetIndex - 1] + (rotations[targetIndex] - rotations[targetIndex - 1]) * (abs(position) - positions[targetIndex - 1])) * (if (position > 0) -1f else 1f)
            translationX = (translations[targetIndex - 1] + (translations[targetIndex] - translations[targetIndex - 1]) * (abs(position) - positions[targetIndex - 1])) * (if (position > 0) -1f else 1f)
            val middleScale = (scales[targetIndex - 1] + scales[targetIndex]) / 2
            elevation = (positions[maxOffsetPosition] - if (scale > middleScale) floor(abs(position)) else ceil(abs(position)))
        } else {
            scale = scales[maxOffsetPosition]
            alpha = alphas[maxOffsetPosition]
            rotationY = rotations[maxOffsetPosition] * (if (position > 0) -1f else 1f)
            translationX = 0F
            elevation = 0F
        }

        LogUtils.d("position=$position elevation=$elevation scale=$scale alpha=$alpha rotationY=$rotationY translationX=$translationX")
        view.elevation = elevation
        view.scaleX = scale
        view.scaleY = scale
        view.alpha = alpha
        view.rotationY = rotationY
        view.translationX = translationX
    }
}
