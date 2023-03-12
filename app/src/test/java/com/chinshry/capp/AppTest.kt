package com.chinshry.capp

import org.junit.Test
import kotlin.math.abs

/**
 * Created by chinshry on 2023/2/10.
 * Describe：
 */
class AppTest {
    @Test
    fun testMathRound() {
        getElevation(0.4f)
        getElevation(1.4f)
        getElevation(2.4f)
    }

    private fun getElevation(position: Float) {
        val elevation = (2 - abs(position).toInt()).toFloat()
        println("position=$position elevation=$elevation")
    }
}