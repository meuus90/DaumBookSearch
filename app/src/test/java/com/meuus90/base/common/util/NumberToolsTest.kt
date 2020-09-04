package com.meuus90.base.common.util

import com.meuus90.base.common.util.NumberTools.Companion.customStripTrailingZeros
import com.meuus90.base.common.util.NumberTools.Companion.setCustomScale
import org.junit.Assert
import org.junit.Test
import java.math.RoundingMode

class NumberToolsTest {

    @Test
    fun convertToStringTest() {
        val amount = 1000000.toBigDecimal()

        val converted = NumberTools.convertToString(amount)

        Assert.assertEquals(converted, "1,000,000")

        println("convertToStringTest() pass")
    }

    @Test
    fun customStripTrailingZerosTest() {
        val amount = 0.00001.toBigDecimal().setScale(3, RoundingMode.DOWN)
        Assert.assertEquals(amount.toString(), "0.000")

        val converted = amount.customStripTrailingZeros()
        Assert.assertEquals(converted.toString(), "0")

        println("customStripTrailingZerosTest() pass")
    }

    @Test
    fun setCustomScaleTest() {
        val converted = 0.000001.toBigDecimal().setCustomScale(2, RoundingMode.DOWN)
        Assert.assertEquals(converted.toString(), "0")

        println("setCustomScaleTest() pass")
    }
}