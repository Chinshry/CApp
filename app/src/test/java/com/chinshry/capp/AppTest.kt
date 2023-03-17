package com.chinshry.capp

import org.junit.Test

/**
 * Created by chinshry on 2023/2/10.
 * Describe：
 */
class AppTest {
    @Test
    fun main() {
        test(1, 0.4f)
        test(1, 1.4f)
        test(1, 2.4f)
    }

    private fun test(radius: Int, height: Float) {
        val rightOval = radius + radius >= height

        println("rightOval=$rightOval")
    }
}