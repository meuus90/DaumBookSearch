package com.meuus90.base.arch.util.converter

import org.junit.Assert
import org.junit.Test

class StringListTypeConverterTest {
    private val converter = StringListTypeConverter()

    @Test
    fun listToStringTest() {
        val list = listOf("test1", "test2", "test3")
        val converted = converter.listToString(list)

        Assert.assertEquals(converted, "[\"test1\",\"test2\",\"test3\"]")

        println("bigDecimalToStringTest() pass")
    }

    @Test
    fun stringToListTest() {
        val listStr = "[\"test1\",\"test2\",\"test3\"]"
        val converted = converter.stringToList(listStr)

        Assert.assertEquals(converted, listOf("test1", "test2", "test3"))

        println("stringToBigDecimalTest() pass")
    }
}