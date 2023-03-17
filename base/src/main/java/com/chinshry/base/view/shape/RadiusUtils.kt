package com.chinshry.base.view.shape

import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.IntDef
import kotlin.math.min


object RadiusUtils {
    private const val TYPE_NONE = 0 // 没有圆角
    private const val TYPE_RADIUS = 1 // 圆角形状
    private const val TYPE_CIRCLE = 2 // 整体为圆形
    private const val TYPE_OVAL_HORIZONTAL = 3 // 上下为椭圆
    private const val TYPE_OVAL_VERTICAL = 4 // 左右为椭圆

    @Target(AnnotationTarget.TYPE)
    @IntDef(value = [TYPE_NONE, TYPE_RADIUS, TYPE_CIRCLE, TYPE_OVAL_HORIZONTAL, TYPE_OVAL_VERTICAL])
    annotation class RadiusType

    /**
     * 计算背景路径 ConvexPath<br></br>
     * **ConvexPath 这里简单理解：圆角矩形的圆角度数大于矩形的高度或宽度(上下圆角度数的边长和大于高度或者左右圆角度数的边长大于高度，那么就不是 ConvexPath 了)**
     *
     * @param radius            圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @return 结果Path
     */
    fun calculateBgPath(
        radius: Int,
        width: Float,
        height: Float
    ): Path {
        val type = getType(radius, width, height)

        val resultPath = Path()
        return when(type) {
            TYPE_NONE -> calculateRectBgPath(width, height)
            TYPE_RADIUS -> calculateRadiusBgPath(radius, width, height)
            TYPE_CIRCLE -> {
                val rectF = RectF()
                rectF[0f, 0f, width] = height
                resultPath.addCircle(
                    rectF.centerX(),
                    rectF.centerY(),
                    min(rectF.width(), rectF.height()) / 2f,
                    Path.Direction.CW
                )
                resultPath
            }
            TYPE_OVAL_HORIZONTAL -> {
                // 左右都半圆
                val arcRectF = RectF()
                // 左边半圆
                arcRectF[0f, 0f, height] = height
                resultPath.addArc(arcRectF, 90F, 180F)
                // 上边的线
                resultPath.lineTo(width - height / 2f, 0F)
                // 右边半圆
                val rightRectF = RectF()
                rightRectF[(width - height), 0f, width] = height
                resultPath.addArc(rightRectF, -90F, 180F)
                // 下边的线
                resultPath.lineTo(height / 2f, height)
                resultPath
            }
            TYPE_OVAL_VERTICAL -> {
                // 上下都半圆
                val arcRectF = RectF()
                // 上边半圆
                arcRectF[0f, 0f, width] = width
                resultPath.addArc(arcRectF, 180F, 180F)
                // 右边的线
                resultPath.lineTo(width, height - width / 2f)
                // 下边半圆
                val rightRectF = RectF()
                rightRectF[0f, (height - width), width] = height
                resultPath.addArc(rightRectF, 0F, 180F)
                // 左边的线
                resultPath.lineTo(0F, width / 2f)
                resultPath
            }
            else -> calculateRadiusBgPath(radius, width, height)
        }
    }

    /**
     * 计算圆角矩形背景路径
     */
    private fun calculateRadiusBgPath(
        radius: Int, width: Float, height: Float
    ): Path {
        val leftTopRadiusLeft: Float
        val leftTopRadiusTop: Float // 左上角
        val leftBottomRadiusLeft: Float
        val leftBottomRadiusBottom: Float // 左下角
        val rightTopRadiusRight: Float
        val rightTopRadiusTop: Float // 右上角
        val rightBottomRadiusRight: Float
        val rightBottomRadiusBottom: Float // 右下角
        val sideTop = calculateRadiusLength(radius, radius, width) // 上同边
        val sideBottom = calculateRadiusLength(radius, radius, width) // 下同边
        val sideLeft = calculateRadiusLength(radius, radius, height) // 左同边
        val sideRight = calculateRadiusLength(radius, radius, height) // 右同边
        leftTopRadiusTop = sideTop[0].toFloat()
        rightTopRadiusTop = sideTop[1].toFloat()
        leftBottomRadiusBottom = sideBottom[0].toFloat()
        rightBottomRadiusBottom = sideBottom[1].toFloat()
        leftTopRadiusLeft = sideLeft[0].toFloat()
        leftBottomRadiusLeft = sideLeft[1].toFloat()
        rightTopRadiusRight = sideRight[0].toFloat()
        rightBottomRadiusRight = sideRight[1].toFloat()
        val resultPath = Path()
        // 四个角：右上，右下，左下，左上
        resultPath.moveTo(leftTopRadiusTop, 0f)
        resultPath.lineTo(width - rightTopRadiusTop, 0f)
        resultPath.quadTo(width, 0f, width, rightTopRadiusRight)
        resultPath.lineTo(width, height - rightBottomRadiusRight)
        resultPath.quadTo(width, height, width - rightBottomRadiusBottom, height)
        resultPath.lineTo(leftBottomRadiusBottom, height)
        resultPath.quadTo(0f, height, 0f, height - leftBottomRadiusLeft)
        resultPath.lineTo(0f, leftTopRadiusLeft)
        resultPath.quadTo(0f, 0f, leftTopRadiusTop, 0f)
        return resultPath
    }

    /**
     * 计算直角矩形背景路径
     *
     * @param width  宽
     * @param height 高
     */
    private fun calculateRectBgPath(width: Float, height: Float): Path {
        val result = Path()
        result.moveTo(0F, 0F)
        result.lineTo(width, 0F)
        result.lineTo(width, height)
        result.lineTo(0F, height)
        result.close()
        return result
    }

    /**
     * 计算边框的边框路径 ConvexPath<br></br>
     * **ConvexPath 这里简单理解：圆角矩形的圆角度数大于矩形的高度或宽度(上下圆角度数的边长和大于高度或者左右圆角度数的边长大于高度，那么就不是 ConvexPath 了)**
     *
     * @param radius 圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @param solidWidth        边框宽度
     * @return 结果Path 数组，path[0]：四条边 path[1]：四个角的路径
     */
    fun calculateSocketPath(
        radius: Int,
        width: Float,
        height: Float,
        solidWidth: Int
    ): List<Path> {
        val type = getType(
            radius,
            width,
            height
        )

        return when(type) {
            TYPE_NONE -> {
                listOf(
                    calculateRectSocketPath(width, height, solidWidth)
                )
            }
            TYPE_RADIUS -> {
                calculateRadiusSocketPath(radius, width, height, solidWidth)
            }
            TYPE_CIRCLE -> {
                val resultPath = Path()
                val rectF = RectF()
                rectF[0f, 0f, width] = height
                // 对位置进行偏移线宽的一半，因为直接画线的话，有一半是画到画布外的，
                // 但是因为有圆角，圆角后面还有画布，导致角的线宽比边的线宽要宽
                // 矩形缩小边框的一半
                val newWidth = solidWidth / 2.0f
                rectF.inset(newWidth, newWidth)
                resultPath.addCircle(
                    rectF.centerX(),
                    rectF.centerY(),
                    min(rectF.width(), rectF.height()) / 2f,
                    Path.Direction.CW
                )
                listOf(resultPath)
            }
            TYPE_OVAL_HORIZONTAL -> {
                val resultPath = Path()
                val newWidth = solidWidth / 2.0f
                // 左边半圆
                val leftRectF = RectF()
                leftRectF[newWidth, newWidth, height] = height - newWidth
                resultPath.addArc(leftRectF, 90F, 180F)
                // 增加上边的线
                resultPath.lineTo(width - height / 2f, newWidth)
                // 右边半圆
                val rightRectF = RectF()
                rightRectF[(width - height), newWidth, width - newWidth] = height - newWidth
                resultPath.addArc(rightRectF, -90F, 180F)
                // 增加下边的线
                resultPath.lineTo(height / 2f, height - newWidth)
                listOf(resultPath)
            }
            TYPE_OVAL_VERTICAL -> {
                val resultPath = Path()
                val newWidth = solidWidth / 2.0f
                // 上边半圆
                val leftRectF = RectF()
                leftRectF[newWidth, newWidth, width - newWidth] = width
                resultPath.addArc(leftRectF, 180F, 180F)
                // 增加右边的线
                resultPath.lineTo(width - newWidth, height - width / 2f)
                // 下边半圆
                val rightRectF = RectF()
                rightRectF[newWidth, (height - width), width - newWidth] = height - newWidth
                resultPath.addArc(rightRectF, 0F, 180F)
                // 增加左边的线
                resultPath.lineTo(newWidth, width / 2f)
                listOf(resultPath)
            }
            else -> calculateRadiusSocketPath(radius, width, height, solidWidth)
        }
    }

    /**
     * 计算圆角矩形的边框路径
     */
    private fun calculateRadiusSocketPath(
        radius: Int,
        width: Float,
        height: Float,
        solidWidth: Int
    ): List<Path> {
        val result = mutableListOf<Path>()
        val solidPath = Path()
        val radiusPath = Path()

        val leftTopRadiusLeft: Float
        val leftTopRadiusTop: Float // 左上角
        val leftBottomRadiusLeft: Float
        val leftBottomRadiusBottom: Float // 左下角
        val rightTopRadiusRight: Float
        val rightTopRadiusTop: Float // 右上角
        val rightBottomRadiusRight: Float
        val rightBottomRadiusBottom: Float // 右下角
        val sideTop = calculateRadiusLength(radius, radius, width) // 上同边
        val sideBottom = calculateRadiusLength(radius, radius, width) // 下同边
        val sideLeft = calculateRadiusLength(radius, radius, height) // 左同边
        val sideRight = calculateRadiusLength(radius, radius, height) // 右同边
        leftTopRadiusTop = sideTop[0].toFloat()
        rightTopRadiusTop = sideTop[1].toFloat()
        leftBottomRadiusBottom = sideBottom[0].toFloat()
        rightBottomRadiusBottom = sideBottom[1].toFloat()
        leftTopRadiusLeft = sideLeft[0].toFloat()
        leftBottomRadiusLeft = sideLeft[1].toFloat()
        rightTopRadiusRight = sideRight[0].toFloat()
        rightBottomRadiusRight = sideRight[1].toFloat()

        // 对位置进行偏移线宽的一半，因为直接画线的话，有一半是画到画布外的，
        // 但是因为有圆角，圆角后面还有画布，导致角的线宽比边的线宽要宽
        val newWidth = solidWidth / 2.0f
        // 四条边路径
        solidPath.moveTo(leftTopRadiusTop, newWidth)
        solidPath.lineTo(width - rightTopRadiusTop, newWidth)
        solidPath.moveTo(width - newWidth, rightTopRadiusRight)
        solidPath.lineTo(width - newWidth, height - rightBottomRadiusRight)
        solidPath.moveTo(width - rightBottomRadiusBottom, height - newWidth)
        solidPath.lineTo(leftBottomRadiusBottom, height - newWidth)
        solidPath.moveTo(newWidth, height - leftBottomRadiusLeft)
        solidPath.lineTo(newWidth, leftTopRadiusLeft)

        // 四个角路径
        radiusPath.moveTo(newWidth, leftTopRadiusLeft)
        radiusPath.quadTo(newWidth, newWidth, leftTopRadiusTop, newWidth)
        radiusPath.moveTo(width - rightTopRadiusTop, newWidth)
        radiusPath.quadTo(width, newWidth, width - newWidth, rightTopRadiusRight)
        radiusPath.moveTo(width - newWidth, height - rightBottomRadiusRight)
        radiusPath.quadTo(
            width - newWidth,
            height - newWidth,
            width - rightBottomRadiusBottom,
            height - newWidth
        )
        radiusPath.moveTo(leftBottomRadiusBottom, height - newWidth)
        radiusPath.quadTo(newWidth, height - newWidth, newWidth, height - leftBottomRadiusLeft)
        result.add(solidPath)
        result.add(radiusPath)
        return result
    }

    /**
     * 计算直角矩形边框路径
     *
     * @param width      宽
     * @param height     高
     * @param solidWidth 线宽
     * @return
     */
    private fun calculateRectSocketPath(width: Float, height: Float, solidWidth: Int): Path {
        val newWidth = solidWidth / 2.0f
        val result = Path()
        result.moveTo(newWidth, newWidth)
        result.lineTo(width - newWidth, newWidth)
        result.lineTo(width - newWidth, height - newWidth)
        result.lineTo(newWidth, height - newWidth)
        result.close()
        return result
    }

    /**
     * 根据圆角大小和边框长度将圆角矩形作为什么图形处理
     *
     * @return TYPE_CIRCLE：圆形  TYPE_RADIUS：圆角矩形  TYPE_OVAL：两端作为椭圆
     */
    private fun getType(
        radius: Int,
        width: Float,
        height: Float
    ): @RadiusType Int {
        // 没有圆角
        if (radius <= 0) return TYPE_NONE

        val verticalOval = radius * 2 >= width
        val horizontalOval = radius * 2 >= height

        return if (horizontalOval && verticalOval) {
            TYPE_CIRCLE
        } else if (!horizontalOval && !verticalOval) {
            TYPE_RADIUS
        } else if (horizontalOval) {
            TYPE_OVAL_HORIZONTAL
        } else {
            TYPE_OVAL_VERTICAL
        }
    }

    /**
     * 根据同边的两个圆角的分别长度和边的长度，重新计算两个圆角该有的长度(防止两个圆角的同边长度之后大于总的长度)<br></br>
     * 如：给出左上角上边的长度和右上角上边的长度，以及矩形的上边边长(矩形的长)，重新计算出左上角上边的长度和右上角上边的长度，
     * 防止左上角上边的长度和右上角上边的长度之和大于边长导致出错，如果大于边长时，根据比例计算。
     *
     * @param sameSide1     同边第一个值的原大小
     * @param sameSide2     同边第二个值的原大小
     * @param sameSideWidth 同边长度
     * @return int[]，长度为2，int[0]：同边第一个值的最终大小 int[1]：同边第二个值的最终大小
     */
    private fun calculateRadiusLength(
        sameSide1: Int,
        sameSide2: Int,
        sameSideWidth: Float
    ): IntArray {
        val result = IntArray(2)
        if (sameSide1 > 0 && sameSide2 > 0) {
            val topRadiusWidth = sameSide1 + sameSide2
            if (topRadiusWidth > sameSideWidth) {
                result[0] = (sameSide1 * 1.0 / topRadiusWidth * sameSideWidth).toInt()
                result[1] = (sameSide2 * 1.0 / topRadiusWidth * sameSideWidth).toInt()
            } else {
                result[0] = sameSide1
                result[1] = sameSide2
            }
        } else if (sameSide1 > 0) {
            result[0] = min(sameSide1, sameSideWidth.toInt())
        } else if (sameSide2 > 0) {
            result[1] = min(sameSide2, sameSideWidth.toInt())
        }
        return result
    }
}