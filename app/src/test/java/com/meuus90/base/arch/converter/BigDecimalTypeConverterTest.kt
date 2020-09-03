package com.meuus90.base.arch.converter

import org.junit.Assert
import org.junit.Test

class BigDecimalTypeConverterTest {
    private val converter = BigDecimalTypeConverter()

    @Test
    fun bigDecimalToStringTest() {
        val converted = converter.bigDecimalToString(1000.toBigDecimal())

        Assert.assertEquals(converted, "1000")

        println("bigDecimalToStringTest() pass")
    }

    @Test
    fun stringToBigDecimalTest() {
        val converted = converter.stringToBigDecimal("1000")

        Assert.assertEquals(converted, 1000.toBigDecimal())

        println("stringToBigDecimalTest() pass")
    }
}